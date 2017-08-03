package me.anilkc.hacknight.alexa.intent.handler;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;

import me.anilkc.hacknight.alexa.util.AlexaConstants;
import me.anilkc.hacknight.alexa.util.AlexaUtil;
import me.anilkc.hacknight.domain.LightningTalk;
import me.anilkc.hacknight.domain.VotingStat;
import me.anilkc.hacknight.service.LightningTalkService;
import me.anilkc.hacknight.service.VotingService;

@Component("votingStatusIntent")
public class VotingStatusIntent implements AlexaIntentHandler {

  @Autowired
  private VotingService votingService;

  @Autowired
  private LightningTalkService lightningTalkService;

  @Override
  public SpeechletResponse handle(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {

    StringBuilder speechText = new StringBuilder();
    VotingStat stat = votingService.getVotingStat();
    List<LightningTalk> allTalks = lightningTalkService.getAllLightningTalks();
    if (!allTalks.isEmpty()) {
      // @formatter:off
      LightningTalk winningTalk = allTalks.stream()
                                          .sorted(
                                              Comparator
                                              .comparing(LightningTalk::getVoteCount)
                                              .reversed())
                                          .collect(Collectors.toList())
                                          .get(0);
      // @formatter:on
      if (stat.getTotalVotersWhoVoted() == stat.getTotalNumberOfVoters() && stat.getTotalNumberOfVoters() != 0) {
        speechText.append(winningTalk.getTopic());
        speechText.append(" by ");
        speechText.append(winningTalk.getPresenter());
        speechText.append(" won with ");
        speechText.append(winningTalk.getVoteCount());
        speechText.append(" votes. ");
      } else {
        speechText.append(winningTalk.getTopic());
        speechText.append(" by ");
        speechText.append(winningTalk.getPresenter());
        speechText.append(" is leading with ");
        speechText.append(winningTalk.getVoteCount());
        speechText.append(" votes. ");
      }
    }
    
    speechText.append("Turn out is " + stat.getVotePercent());
    speechText.append(" percent, ");
    speechText.append("with " + stat.getTotalNumberOfVoters() + " total number of voters. ");
    speechText.append("Is there anything else you need?");

    requestEnvelope.getSession().setAttribute(AlexaConstants.PREVIOUS_INTENT, requestEnvelope.getRequest().getIntent().getName());
    return AlexaUtil.getSpeechletResponse("Voting Stats", speechText.toString(), false);
  }
}
