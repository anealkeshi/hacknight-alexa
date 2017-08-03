package me.anilkc.hacknight.alexa.intent.handler;

import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;

import me.anilkc.hacknight.alexa.util.AlexaConstants;
import me.anilkc.hacknight.alexa.util.AlexaUtil;
import me.anilkc.hacknight.domain.LightningTalk;
import me.anilkc.hacknight.domain.Voter;
import me.anilkc.hacknight.exception.HackNightException;
import me.anilkc.hacknight.service.LightningTalkService;
import me.anilkc.hacknight.service.VotingService;

@Component("confirmYesIntent")
public class ConfirmYesIntent implements AlexaIntentHandler {

  private static final Logger LOG = LoggerFactory.getLogger(ConfirmYesIntent.class);

  @Autowired
  private VotingService votingService;

  @Autowired
  private LightningTalkService lightningTalkService;

  @Override
  public SpeechletResponse handle(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {

    Session session = requestEnvelope.getSession();
    StringBuilder speechText = new StringBuilder();
    String previousIntent = (String) session.getAttribute(AlexaConstants.PREVIOUS_INTENT);
    boolean shouldEndSession = false;

    if (previousIntent == null) {
      speechText.append("I am sorry. I don't understand.");
      speechText.append(". ");
      speechText.append("Is there anything else you need?");
    } else if (StringUtils.equals(previousIntent, "ProvideVoterUniqueIdIntent")) {
      speechText.append("Thank you for confirming. ");
      speechText.append("You can ask to list lightning talks or voting status. ");
      speechText.append("You can also vote using talk unique number.");
    } else if (StringUtils.equals(previousIntent, "VoteIntent")) {
      int talkUniqueId = (Integer) session.getAttribute(AlexaConstants.VOTED_LIGHTNING_TALK);
      int voterId = (Integer) session.getAttribute(AlexaConstants.CURRENT_VOTER);
      try {
        voteAndGetResponse(speechText, talkUniqueId, voterId);
      } catch (HackNightException e) {
        LOG.error("HackNightException when trying to vote", e);
        shouldEndSession = true;
        speechText.append("Something happened. Your voting wasn't successful. Please try again later.");
      }
    } else {
      speechText.append("I am sorry. I don't understand.");
      speechText.append(". ");
      speechText.append("Is there anything else you need?");
    }

    requestEnvelope.getSession().setAttribute(AlexaConstants.PREVIOUS_INTENT, requestEnvelope.getRequest().getIntent().getName());
    return AlexaUtil.getSpeechletResponse("Confirm Action", speechText.toString(), shouldEndSession);
  }

  private void voteAndGetResponse(StringBuilder speechText, int talkUniqueId, int voterId) throws HackNightException {
    Voter vote = votingService.vote(voterId, talkUniqueId);
    List<LightningTalk> lightningTalks = lightningTalkService.getAllLightningTalks();
    // @formatter:off
      int votedTalkPosition = IntStream.range(0, lightningTalks.size())
                                       .filter(index -> talkUniqueId == lightningTalks.get(index).getUniqueId())
                                       .findFirst()
                                       .getAsInt();
      // @formatter:on
    LightningTalk votedTalk = lightningTalks.stream().filter(l -> l.getUniqueId() == talkUniqueId).findFirst().get();
    LightningTalk winningTalk = lightningTalks.stream().findFirst().get();

    speechText.append("You have successfully voted for ");
    speechText.append(vote.getLightningTalk().getTopic());
    speechText.append(" by ");
    speechText.append(vote.getLightningTalk().getPresenter());
    speechText.append(". ");

    if (votedTalk.getUniqueId() == winningTalk.getUniqueId()) {
      speechText.append(votedTalk.getTopic());
      speechText.append(" by ");
      speechText.append(votedTalk.getPresenter());
      speechText.append(" is leading by ");
      speechText.append(votedTalk.getVoteCount());
      speechText.append(". ");
    } else {
      speechText.append(votedTalk.getTopic());
      speechText.append(" by ");
      speechText.append(votedTalk.getPresenter());
      speechText.append(" is sitting at ");
      speechText.append(votedTalkPosition);
      speechText.append(AlexaUtil.ordinalNo(votedTalkPosition));
      speechText.append(" position with ");
      speechText.append(votedTalk.getVoteCount());
      speechText.append(" votes");
      speechText.append(". ");
      speechText.append(winningTalk.getTopic());
      speechText.append(" by ");
      speechText.append(winningTalk.getPresenter());
      speechText.append(" is leading with total ");
      speechText.append(winningTalk.getVoteCount());
      speechText.append(" votes. ");
    }
    speechText.append(". ");
    speechText.append("Is there anything else you need?");
  }
}
