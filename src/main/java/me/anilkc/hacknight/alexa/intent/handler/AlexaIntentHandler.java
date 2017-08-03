package me.anilkc.hacknight.alexa.intent.handler;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;

public interface AlexaIntentHandler {

  SpeechletResponse handle(SpeechletRequestEnvelope<IntentRequest> requestEnvelope);
}
