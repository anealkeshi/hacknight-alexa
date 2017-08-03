package me.anilkc.hacknight.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import me.anilkc.hacknight.domain.LightningTalk;
import me.anilkc.hacknight.exception.HackNightException;
import me.anilkc.hacknight.service.LightningTalkService;
import me.anilkc.hacknight.service.VotingService;

@RestController
@RequestMapping("/talks")
class LightningTalkController {

  @Autowired
  private LightningTalkService lightningTalkService;

  @Autowired
  private Validator validator;

  @Autowired
  private VotingService votingService;

  @RequestMapping(value = "/{uniqueTalkId}", method = RequestMethod.GET)
  public ResponseEntity<LightningTalk> getLightningTalk(@PathVariable(name = "uniqueTalkId", required = true) int uniqueId) {
    LightningTalk lightningTalk = lightningTalkService.getByUiqueId(uniqueId);
    return ResponseEntity.ok(lightningTalk);
  }

  @RequestMapping(value = "/{uniqueTalkId}", method = RequestMethod.DELETE)
  public ResponseEntity<Map<String, String>> deleteLightningTalk(@PathVariable(name = "uniqueTalkId", required = true) int uniqueId) {
    lightningTalkService.remove(uniqueId);
    Map<String, String> responseMap = new HashMap<>();
    responseMap.put("action", "success");
    return ResponseEntity.ok(responseMap);
  }

  @RequestMapping(value = "/", method = RequestMethod.POST)
  public ResponseEntity<LightningTalk> addLightningTalk(@Valid @RequestBody LightningTalk talk) {
    talk = lightningTalkService.addLightningTalk(talk);
    return ResponseEntity.ok(talk);
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ResponseEntity<List<LightningTalk>> getAllLightningTalks() {
    return ResponseEntity.ok(lightningTalkService.getAllLightningTalks());
  }

  @RequestMapping(value = "/{uniqueTalkId}", method = RequestMethod.PUT)
  public ResponseEntity<LightningTalk> updateLightningTalk(@PathVariable(name = "uniqueTalkId", required = true) int uniqueId,
      @RequestBody LightningTalk talk) throws HackNightException {
    LightningTalk lightningTalk = lightningTalkService.updateLightningTalk(talk);
    return ResponseEntity.ok(lightningTalk);
  }

  @RequestMapping(value = "/talks-collection", method = RequestMethod.POST)
  public ResponseEntity<?> addMultipleLightningTalks(@RequestBody List<LightningTalk> talks) throws HackNightException {

    if (!talks.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(talks.get(0), "lightningTalk");
      talks.forEach(t -> validator.validate(t, bindingResult));
      if (bindingResult.hasErrors()) {
        throw new HackNightException("Lightningtalks can't be empty");
      }
    }
    return ResponseEntity.ok(lightningTalkService.addAll(talks));
  }

  @RequestMapping(value = "/{uniqueTalkId}/votes", method = RequestMethod.PUT)
  public ResponseEntity<LightningTalk> voteLightningTalk(@PathVariable(name = "uniqueTalkId", required = true) int talkUniqueId,
      @RequestBody Map<String, Integer> requestMap) throws HackNightException {
    int voterUniqueId = requestMap.get("voterId");
    votingService.vote(voterUniqueId, talkUniqueId);
    return ResponseEntity.ok(lightningTalkService.getByUiqueId(talkUniqueId));
  }
}
