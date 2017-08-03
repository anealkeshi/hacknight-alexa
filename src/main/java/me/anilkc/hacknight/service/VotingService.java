package me.anilkc.hacknight.service;

import me.anilkc.hacknight.domain.Voter;
import me.anilkc.hacknight.domain.VotingStat;
import me.anilkc.hacknight.exception.HackNightException;

public interface VotingService {

  Voter vote(int voterUniqueId, int talkUniqueId) throws HackNightException;

  VotingStat getVotingStat();
}
