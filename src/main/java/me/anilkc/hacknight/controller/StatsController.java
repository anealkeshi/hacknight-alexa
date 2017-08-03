package me.anilkc.hacknight.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import me.anilkc.hacknight.domain.VotingStat;
import me.anilkc.hacknight.exception.HackNightException;
import me.anilkc.hacknight.service.VotingService;

@RestController
@RequestMapping("/stats")
public class StatsController {

  @Autowired
  private VotingService votingService;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public VotingStat getOverallStat() throws HackNightException {
    return votingService.getVotingStat();
  }

}
