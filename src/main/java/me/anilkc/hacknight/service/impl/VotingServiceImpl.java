package me.anilkc.hacknight.service.impl;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.anilkc.hacknight.domain.LightningTalk;
import me.anilkc.hacknight.domain.Voter;
import me.anilkc.hacknight.domain.VotingStat;
import me.anilkc.hacknight.exception.HackNightException;
import me.anilkc.hacknight.service.LightningTalkService;
import me.anilkc.hacknight.service.VoterService;
import me.anilkc.hacknight.service.VotingService;

@Service
@Transactional(rollbackOn = Exception.class)
public class VotingServiceImpl implements VotingService {

  @Autowired
  private VoterService voterService;

  @Autowired
  private LightningTalkService lightningTalkService;

  @Override
  public Voter vote(int voterUniqueId, int talkUniqueId) throws HackNightException {
    Voter voter = voterService.getByUiqueId(voterUniqueId);
    if (voter != null && voter.getLightningTalk() == null) {
      LightningTalk talk = lightningTalkService.getByUiqueId(talkUniqueId);
      talk.setVoteCount(talk.getVoteCount() + 1);
      lightningTalkService.updateLightningTalk(talk);
      voter.setLightningTalk(talk);
      return voterService.updateVoter(voter);
    }
    return voter;
  }

  @Override
  public VotingStat getVotingStat() {

    List<LightningTalk> talks = lightningTalkService.getAllLightningTalks();
    List<Voter> voters = voterService.getAllVoters();

    long totalTalksVoted = talks.stream().filter(t -> t.getVoteCount() > 0).count();
    long totalVotersWhoVoted = voters.stream().filter(v -> null != v.getLightningTalk()).count();
    double voterPercent = (voters.size() != 0) ? ((double) totalVotersWhoVoted / voters.size()) * 100 : 0;

    VotingStat votingStat = new VotingStat();
    votingStat.setTotalNumberOfTalks(talks.size());
    votingStat.setTotalNumberOfVoters(voters.size());
    votingStat.setTotalTalksVoted(totalTalksVoted);
    votingStat.setTotalVotersWhoVoted(totalVotersWhoVoted);

    DecimalFormat df = new DecimalFormat();
    df.setMaximumFractionDigits(2);
    try {
      votingStat.setVotePercent(df.parse(df.format(voterPercent)).doubleValue());
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return votingStat;
  }

}
