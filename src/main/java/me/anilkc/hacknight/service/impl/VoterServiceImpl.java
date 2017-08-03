package me.anilkc.hacknight.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.anilkc.hacknight.domain.Voter;
import me.anilkc.hacknight.exception.HackNightException;
import me.anilkc.hacknight.repository.VoterRepository;
import me.anilkc.hacknight.service.VoterService;
import me.anilkc.hacknight.util.LambdaExceptionWrapper;
import me.anilkc.hacknight.util.UniqueIdGenerator;

@Service
@Transactional(rollbackOn = Exception.class)
public class VoterServiceImpl implements VoterService {

  @Autowired
  private VoterRepository voterRepository;

  @Override
  public Voter addVoter(Voter voter) {
    int uniqueId = 0;
    Voter newVoter = null;
    while (true) {
      uniqueId = UniqueIdGenerator.generate4DigitRandomNumber();
      newVoter = getByUiqueId(uniqueId);
      if (newVoter == null) {
        voter.setUniqueId(uniqueId);
        newVoter = voterRepository.save(voter);
        break;
      }
    }
    return newVoter;
  }

  @Override
  public Voter updateVoter(Voter voter) throws HackNightException {
    Voter oldVoter = getByUiqueId(voter.getUniqueId());
    try {
      BeanUtils.copyProperties(oldVoter, voter);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new HackNightException("Exception while updating Voter");
    }
    return voterRepository.save(oldVoter);
  }

  @Override
  public Voter getByUiqueId(int uniqueId) {
    return voterRepository.findByUniqueId(uniqueId);
  }

  @Override
  public List<Voter> addAll(List<Voter> voters) {
    List<Voter> addedVoters = new ArrayList<>();
    voters.forEach(LambdaExceptionWrapper.acceptWithException(v -> addedVoters.add(addVoter(v))));
    return addedVoters;
  }

  @Override
  public List<Voter> getAllVoters() {
    Iterable<Voter> votersIterable = voterRepository.findAll();
    List<Voter> voters = new ArrayList<>();
    votersIterable.forEach(voters::add);
    return voters;
  }

  @Override
  public boolean remove(int uniqueId) {
    voterRepository.deleteByUniqueId(uniqueId);
    return true;
  }

  @Override
  public Voter getVoterByEmail(String email) {
    if (email == null) {
      throw new IllegalArgumentException("Email can't be null");
    }
    return voterRepository.findByEmail(email);
  }


}
