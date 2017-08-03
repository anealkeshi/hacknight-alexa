package me.anilkc.hacknight.alexa.intent.handler;

import org.springframework.stereotype.Component;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;

import me.anilkc.hacknight.alexa.util.AlexaUtil;

@Component("helpIntent")
public class HelpIntent implements AlexaIntentHandler {

  @Override
  public SpeechletResponse handle(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {

    StringBuilder speechText = new StringBuilder();
    speechText.append("You must have 4 digit voter number to use this skill. ");
    speechText.append("You can get the list of lightning talks and vote any talk you like. ");
    speechText.append("You will need to provide 4 digit lightning talk number to cast your vote. ");
    speechText.append("You can also say, give me the voting status to get latest results. ");
    speechText.append("Tell me what you like to do");

    return AlexaUtil.getSpeechletResponse("Help", speechText.toString(), false);
  }

}
