package me.anilkc.hacknight.alexa.intent.handler;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;

import me.anilkc.hacknight.alexa.util.AlexaConstants;
import me.anilkc.hacknight.alexa.util.AlexaUtil;

@Component("confirmNoIntent")
public class ConfirmNoIntent implements AlexaIntentHandler {

  @Resource(name = "stopIntent")
  private AlexaIntentHandler stopIntent;

  @Override
  public SpeechletResponse handle(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
    Session session = requestEnvelope.getSession();
    StringBuilder speechText = new StringBuilder();

    String previousIntent = (String) session.getAttribute(AlexaConstants.PREVIOUS_INTENT);

    if (previousIntent == null) {
      speechText.append("I am sorry. I don't understand.");
      speechText.append(". ");
      speechText.append("Is there anything else you need?");
    } else if (StringUtils.equals(previousIntent, "ProvideVoterUniqueIdIntent")) {
      session.removeAttribute(AlexaConstants.CURRENT_VOTER);
      speechText.append("In that case, please, provide your 4 digit unique voter number.");

    } else if (StringUtils.equals(previousIntent, "VoteIntent")) {
      session.removeAttribute(AlexaConstants.VOTED_LIGHTNING_TALK);
      speechText.append("In that case, please, provide 4 digit Lightning talk number you want to vote for.");
    } else {
      return stopIntent.handle(requestEnvelope);
    }
    return AlexaUtil.getSpeechletResponse("Confirm Action", speechText.toString(), false);
  }

}
