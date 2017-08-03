package me.anilkc.hacknight.service;

import me.anilkc.hacknight.domain.VotingStat;

public interface StatsService {

  VotingStat getLightningTalkVotingStat(int lightningTalkUniqueId);

}
