package me.anilkc.hacknight.alexa.intent.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;

import me.anilkc.hacknight.alexa.util.AlexaConstants;
import me.anilkc.hacknight.alexa.util.AlexaUtil;
import me.anilkc.hacknight.domain.Voter;
import me.anilkc.hacknight.service.VoterService;

@Component("whoDidIVoteIntent")
public class WhoDidIVoteIntent implements AlexaIntentHandler {

  @Autowired
  private VoterService voterService;

  @Override
  public SpeechletResponse handle(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {

    Session session = requestEnvelope.getSession();
    StringBuilder speechText = new StringBuilder();
    int voterUniqueId = (Integer) session.getAttribute(AlexaConstants.CURRENT_VOTER);
    Voter voter = voterService.getByUiqueId(voterUniqueId);
    if (voter == null) {
      speechText.append("We couldn't find your record.");
    } else if (voter.getLightningTalk() == null) {
      speechText.append("You haven't cast your vote yet.");
    } else {
      speechText.append("You have voted for " + voter.getLightningTalk().getTopic() + " by " + voter.getLightningTalk().getPresenter());
      speechText.append(". ");
      speechText.append("Is there anything else you need?");
    }

    requestEnvelope.getSession().setAttribute(AlexaConstants.PREVIOUS_INTENT, requestEnvelope.getRequest().getIntent().getName());
    return AlexaUtil.getSpeechletResponse("My Vote", speechText.toString(), false);
  }
}
