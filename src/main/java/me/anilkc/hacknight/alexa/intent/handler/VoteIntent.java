package me.anilkc.hacknight.alexa.intent.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;

import me.anilkc.hacknight.alexa.util.AlexaConstants;
import me.anilkc.hacknight.alexa.util.AlexaUtil;
import me.anilkc.hacknight.domain.LightningTalk;
import me.anilkc.hacknight.domain.Voter;
import me.anilkc.hacknight.service.LightningTalkService;
import me.anilkc.hacknight.service.VoterService;

@Component("voteIntent")
public class VoteIntent implements AlexaIntentHandler {

  private static final Logger LOG = LoggerFactory.getLogger(VoteIntent.class);

  @Autowired
  private LightningTalkService lightningTalkService;

  @Autowired
  private VoterService voterService;

  @Override
  public SpeechletResponse handle(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {

    Session session = requestEnvelope.getSession();
    int voterUniqueId = (Integer) session.getAttribute(AlexaConstants.CURRENT_VOTER);
    Voter voter = voterService.getByUiqueId(voterUniqueId);

    if (voter != null && voter.getLightningTalk() != null) {
      StringBuilder speechText = new StringBuilder();
      speechText.append("You can't vote twice. You have already voted for ");
      speechText.append(voter.getLightningTalk().getTopic());
      speechText.append(" by ");
      speechText.append(voter.getLightningTalk().getPresenter());
      speechText.append(". ");
      speechText.append("Is there anything else you need?");
      return AlexaUtil.getSpeechletResponse("Vote Status", speechText.toString(), false);
    }

    Intent intent = requestEnvelope.getRequest().getIntent();
    Slot talkUniqueIdSlot = intent.getSlot("talkUniqueId");

    try {
      int talkUniqueId = Integer.parseInt(talkUniqueIdSlot.getValue());
      LightningTalk lightningTalk = lightningTalkService.getByUiqueId(talkUniqueId);

      if (lightningTalk == null) {
        StringBuilder speechText = new StringBuilder();
        speechText.append("I am sorry. ");
        speechText.append("I can't find Lightning talk with provided unique number. ");
        speechText.append("Please provide lightning talk 4 digit unique number. ");
        return AlexaUtil.getSpeechletResponse("Vote Status", speechText.toString(), false);
      }

      session.setAttribute(AlexaConstants.VOTED_LIGHTNING_TALK, lightningTalk.getUniqueId());
      requestEnvelope.getSession().setAttribute(AlexaConstants.PREVIOUS_INTENT, intent.getName());
      StringBuilder speechText = getLightningTalkConfirmation(lightningTalk);
      return AlexaUtil.getSpeechletResponse("Vote Status", speechText.toString(), false);

    } catch (NumberFormatException e) {
      LOG.error("NumberFormatException while trying to get talkUniqueId " + talkUniqueIdSlot.getValue(), e);
      StringBuilder speechText = new StringBuilder();
      speechText.append("I didn't get that. ");
      speechText.append("Please provide the 4 digit unique number you wish to vote for.");
      return AlexaUtil.getSpeechletResponse("Vote Status", speechText.toString(), false);
    }
  }

  private StringBuilder getLightningTalkConfirmation(LightningTalk lightningTalk) {
    StringBuilder speechText = new StringBuilder();
    speechText.append("Great!! ");
    speechText.append("Please, confirm that you want to vote for ");
    speechText.append(lightningTalk.getTopic());
    speechText.append(" presented by ");
    speechText.append(lightningTalk.getPresenter());
    speechText.append(". ");
    speechText.append("Please, respond by saying Yes or No.");
    return speechText;
  }

}
