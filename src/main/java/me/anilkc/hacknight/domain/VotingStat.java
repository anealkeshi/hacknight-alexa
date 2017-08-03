package me.anilkc.hacknight.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class VotingStat implements Serializable {

  private static final long serialVersionUID = -7934946220838543543L;

  private int totalNumberOfTalks;

  private long totalTalksVoted;

  private double votePercent;

  private int totalNumberOfVoters;

  private long totalVotersWhoVoted;
}
