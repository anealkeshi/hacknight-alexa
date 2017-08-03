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
import me.anilkc.hacknight.domain.Voter;
import me.anilkc.hacknight.service.VoterService;


@Component("provideVoterUniqueIdIntent")
public class ProvideVoterUniqueIdIntent implements AlexaIntentHandler {

  private static final Logger LOG = LoggerFactory.getLogger(ProvideVoterUniqueIdIntent.class);

  @Autowired
  private VoterService voterService;

  @Override
  public SpeechletResponse handle(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {

    Session session = requestEnvelope.getSession();
    Voter voter = null;
    if (session.getAttribute(AlexaConstants.CURRENT_VOTER) != null) {
      int voterUniqueId = (Integer) session.getAttribute(AlexaConstants.CURRENT_VOTER);
      voter = voterService.getByUiqueId(voterUniqueId);
      StringBuilder alreadyVotedSpeechText = new StringBuilder();
      alreadyVotedSpeechText.append("We have already identified you as ");
      alreadyVotedSpeechText.append(voter.getName());
      alreadyVotedSpeechText.append(" .");
      alreadyVotedSpeechText.append("Is there anything else you need?");
      return AlexaUtil.getSpeechletResponse("Welcome, " + voter.getName(), alreadyVotedSpeechText.toString(), false);
    }

    StringBuilder speechText = new StringBuilder();
    Intent intent = requestEnvelope.getRequest().getIntent();
    Slot voterUniqueIdSlot = intent.getSlot("voterUniqueId");

    try {
      int voterUniqueId = Integer.valueOf(voterUniqueIdSlot.getValue());
      voter = voterService.getByUiqueId(voterUniqueId);

      if (voter == null) {
        StringBuilder recordNotFoundSpeechText = new StringBuilder();
        recordNotFoundSpeechText.append("I can't find your record. ");
        recordNotFoundSpeechText.append("Please provide your 4 digit voter number.");
        return AlexaUtil.getSpeechletResponse("Voter number required", recordNotFoundSpeechText.toString(), false);
      }

      session.setAttribute(AlexaConstants.CURRENT_VOTER, voter.getUniqueId());
      requestEnvelope.getSession().setAttribute(AlexaConstants.PREVIOUS_INTENT, intent.getName());

      speechText.append("Great!! ");
      speechText.append("Is your name ");
      speechText.append(voter.getName());
      speechText.append("? ");
      speechText.append("Please respond by saying Yes or No.");
      return AlexaUtil.getSpeechletResponse("Confirm name", speechText.toString(), false);

    } catch (NumberFormatException e) {
      LOG.error("NumberFormatException while get voterUniqueId", e);
      return AlexaUtil.getSpeechletResponse("Voter Number Required", "I didn't get that. Please provide your 4 digit voter number.", false);
    }
  }
}
