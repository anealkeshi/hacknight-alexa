package me.anilkc.hacknight.alexa.intent.handler;

import org.springframework.stereotype.Component;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;

import me.anilkc.hacknight.alexa.util.AlexaUtil;

@Component("stopIntent")
public class StopIntent implements AlexaIntentHandler {

  @Override
  public SpeechletResponse handle(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
    return AlexaUtil.getSpeechletResponse("Bye Hack Night", "Thank you for using Hack Night.", true);
  }
}
