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

import me.anilkc.hacknight.domain.Voter;
import me.anilkc.hacknight.exception.HackNightException;
import me.anilkc.hacknight.service.VoterService;

@RestController
@RequestMapping("/voters")
public class VoterController {

  @Autowired
  private VoterService voterService;

  @Autowired
  private Validator validator;

  @RequestMapping(value = "/{uniqueVoterId}", method = RequestMethod.GET)
  public ResponseEntity<Voter> getVoter(@PathVariable(name = "uniqueVoterId", required = true) int uniqueId) {
    Voter voter = voterService.getByUiqueId(uniqueId);
    return ResponseEntity.ok(voter);
  }

  @RequestMapping(value = "/{uniqueVoterId}", method = RequestMethod.DELETE)
  public ResponseEntity<Map<String, String>> deleteVoter(@PathVariable(name = "uniqueVoterId", required = true) int uniqueId) {
    voterService.remove(uniqueId);
    Map<String, String> responseMap = new HashMap<>();
    responseMap.put("action", "success");
    return ResponseEntity.ok(responseMap);
  }

  @RequestMapping(value = "/", method = RequestMethod.POST)
  public ResponseEntity<Voter> addVoter(@Valid @RequestBody Voter voter) {
    voter = voterService.addVoter(voter);
    return ResponseEntity.ok(voter);

  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ResponseEntity<List<Voter>> getAllVoters() {
    return ResponseEntity.ok(voterService.getAllVoters());
  }

  @RequestMapping(value = "/{uniqueVoterId}", method = RequestMethod.PUT)
  public ResponseEntity<Voter> updateVoter(@PathVariable(name = "uniqueVoterId", required = true) int uniqueId,
      @Valid @RequestBody Voter voter) throws HackNightException {
    voter = voterService.updateVoter(voter);
    return ResponseEntity.ok(voter);
  }

  @RequestMapping(value = "/voters-collection", method = RequestMethod.POST)
  public ResponseEntity<?> addMultipleVoters(@RequestBody List<Voter> voters) throws HackNightException {
    if (!voters.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(voters.get(0), "voters");
      voters.forEach(t -> validator.validate(t, bindingResult));
      if (bindingResult.hasErrors()) {
        throw new HackNightException("Voters list can't be empty");
      }
    }
    return ResponseEntity.ok(voterService.addAll(voters));
  }
}
