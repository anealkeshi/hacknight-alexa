package me.anilkc.hacknight.alexa.util;

import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;

public class AlexaUtil {

  public AlexaUtil() {

  }


  public static SpeechletResponse getSpeechletResponse(String title, String speechText, Boolean endSession) {

    SimpleCard card = new SimpleCard();
    card.setTitle(title);
    card.setContent(speechText);

    PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
    speech.setText(speechText);

    SpeechletResponse response = new SpeechletResponse();
    response.setCard(card);
    response.setOutputSpeech(speech);
    response.setNullableShouldEndSession(endSession);
    return response;
  }

  public static String ordinalNo(int value) {
    int hunRem = value % 100;
    int tenRem = value % 10;
    if (hunRem - tenRem == 10) {
      return "th";
    }
    switch (tenRem) {
      case 1:
        return "st";
      case 2:
        return "nd";
      case 3:
        return "rd";
      default:
        return "th";
    }
  }
}
