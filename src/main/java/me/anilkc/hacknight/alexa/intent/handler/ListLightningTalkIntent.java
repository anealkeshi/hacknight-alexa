package me.anilkc.hacknight.alexa.intent.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;

import me.anilkc.hacknight.alexa.util.AlexaConstants;
import me.anilkc.hacknight.alexa.util.AlexaUtil;
import me.anilkc.hacknight.domain.LightningTalk;
import me.anilkc.hacknight.service.LightningTalkService;

@Component("listLightningTalkIntent")
public class ListLightningTalkIntent implements AlexaIntentHandler {

  @Autowired
  private LightningTalkService lightningTalkService;

  @Override
  public SpeechletResponse handle(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {

    List<LightningTalk> allTalks = lightningTalkService.getAllLightningTalks();

    StringBuilder speechText = new StringBuilder();

    if (!allTalks.isEmpty()) {
      speechText.append("Here is the list of today's talk. Please, note unique number in order to vote. ");
      allTalks.forEach(t -> {
        speechText.append(t.getTopic());
        speechText.append(", by ");
        speechText.append(t.getPresenter());
        speechText.append(", unique number is ");
        speechText.append(t.getUniqueId());
        speechText.append(". ");
      });
      speechText.append("Is there anything else you need?");
    } else {
      speechText.append("Currently, there are no talks listed.");
      speechText.append(". ");
      speechText.append("Is there anything else you need?");
    }

    requestEnvelope.getSession().setAttribute(AlexaConstants.PREVIOUS_INTENT, requestEnvelope.getRequest().getIntent().getName());
    return AlexaUtil.getSpeechletResponse("List LightningTalk", speechText.toString(), false);
  }

}
