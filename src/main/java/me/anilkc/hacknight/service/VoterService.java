package me.anilkc.hacknight.service;

import java.util.List;

import me.anilkc.hacknight.domain.Voter;
import me.anilkc.hacknight.exception.HackNightException;

public interface VoterService {

  Voter addVoter(Voter voter);

  Voter updateVoter(Voter voter) throws HackNightException;

  Voter getByUiqueId(int uniqueId);

  List<Voter> addAll(List<Voter> voters);

  List<Voter> getAllVoters();

  boolean remove(int uniqueId);

  Voter getVoterByEmail(String email);
}
