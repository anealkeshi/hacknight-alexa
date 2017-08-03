package me.anilkc.hacknight.alexa.speechlet;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.SpeechletV2;

import me.anilkc.hacknight.alexa.intent.handler.AlexaIntentHandler;
import me.anilkc.hacknight.alexa.util.AlexaConstants;
import me.anilkc.hacknight.alexa.util.AlexaUtil;

@Component
public class HackNightSpeechlet implements SpeechletV2 {


  private static final Logger LOG = LoggerFactory.getLogger(HackNightSpeechlet.class);

  @Resource(name = "confirmNoIntent")
  private AlexaIntentHandler confirmNoIntent;

  @Resource(name = "confirmYesIntent")
  private AlexaIntentHandler confirmYesIntent;

  @Resource(name = "listLightningTalkIntent")
  private AlexaIntentHandler listLightningTalkIntent;

  @Resource(name = "provideVoterUniqueIdIntent")
  private AlexaIntentHandler provideVoterUniqueIdIntent;

  @Resource(name = "voteIntent")
  private AlexaIntentHandler voteIntent;

  @Resource(name = "votingStatusIntent")
  private AlexaIntentHandler votingStatusIntent;

  @Resource(name = "whoDidIVoteIntent")
  private AlexaIntentHandler whoDidIVoteIntent;

  @Resource(name = "cancelIntent")
  private AlexaIntentHandler cancelIntent;

  @Resource(name = "helpIntent")
  private AlexaIntentHandler helpIntent;

  @Resource(name = "stopIntent")
  private AlexaIntentHandler stopIntent;

  private static final Map<String, AlexaIntentHandler> DISPATCHER = new HashMap<>();

  private static final Map<String, AlexaIntentHandler> ALEXA_BUILT_IN_INTENTS = new HashMap<>();

  @Override
  public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
    LOG.info("onSessionStarted requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
        requestEnvelope.getSession().getSessionId());
  }

  @Override
  public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
    LOG.info("onLaunch requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
        requestEnvelope.getSession().getSessionId());
    return getWelcomeResponse();
  }

  @Override
  public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
    IntentRequest intentRequest = requestEnvelope.getRequest();
    Session session = requestEnvelope.getSession();
    LOG.info("onIntent requestId={}, sessionId={}, intent={}, slots={}", intentRequest.getRequestId(), session.getSessionId(),
        intentRequest.getIntent().getName(), intentRequest.getIntent().getSlots());

    Intent intent = intentRequest.getIntent();

    if (ALEXA_BUILT_IN_INTENTS.containsKey(intent.getName())) {
      return ALEXA_BUILT_IN_INTENTS.get(intent.getName()).handle(requestEnvelope);
    }

    if (session.getAttribute(AlexaConstants.CURRENT_VOTER) == null && !"ProvideVoterUniqueIdIntent".equals(intent.getName())) {
      return AlexaUtil.getSpeechletResponse("Welcome", "You need to provide your 4 digit unique number before you can proceed.", false);
    }

    return DISPATCHER.get(intent.getName()).handle(requestEnvelope);
  }

  @Override
  public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
    LOG.info("onSessionEnd requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
        requestEnvelope.getSession().getSessionId());
    requestEnvelope.getSession().removeAttribute(AlexaConstants.CURRENT_VOTER);
    requestEnvelope.getSession().removeAttribute(AlexaConstants.PREVIOUS_INTENT);
    requestEnvelope.getSession().removeAttribute(AlexaConstants.VOTED_LIGHTNING_TALK);
  }

  private SpeechletResponse getWelcomeResponse() {
    String speechText = "Welcome to ICC Hack Night Lightning Talks voting. Please provide your 4 digit unique number.";
    return AlexaUtil.getSpeechletResponse("Welcome", speechText, false);
  }

  @PostConstruct
  public void setupHandler() {
    DISPATCHER.put("ConfirmNoIntent", confirmNoIntent);
    DISPATCHER.put("ConfirmYesIntent", confirmYesIntent);
    DISPATCHER.put("ListLightningTalkIntent", listLightningTalkIntent);
    DISPATCHER.put("ProvideVoterUniqueIdIntent", provideVoterUniqueIdIntent);
    DISPATCHER.put("VoteIntent", voteIntent);
    DISPATCHER.put("VotingStatusIntent", votingStatusIntent);
    DISPATCHER.put("ConfirmNoIntent", confirmNoIntent);
    DISPATCHER.put("WhoDidIVoteIntent", whoDidIVoteIntent);
    DISPATCHER.put("AMAZON.StopIntent", stopIntent);
    DISPATCHER.put("AMAZON.CancelIntent", cancelIntent);
    DISPATCHER.put("AMAZON.HelpIntent", helpIntent);

    ALEXA_BUILT_IN_INTENTS.put("AMAZON.StopIntent", stopIntent);
    ALEXA_BUILT_IN_INTENTS.put("AMAZON.CancelIntent", cancelIntent);
    ALEXA_BUILT_IN_INTENTS.put("AMAZON.HelpIntent", helpIntent);
  }
}
