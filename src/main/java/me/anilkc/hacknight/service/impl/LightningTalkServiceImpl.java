package me.anilkc.hacknight.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.anilkc.hacknight.domain.LightningTalk;
import me.anilkc.hacknight.exception.HackNightException;
import me.anilkc.hacknight.repository.LightningTalkRepository;
import me.anilkc.hacknight.service.LightningTalkService;
import me.anilkc.hacknight.util.LambdaExceptionWrapper;
import me.anilkc.hacknight.util.UniqueIdGenerator;

@Service
@Transactional(rollbackOn = Exception.class)
public class LightningTalkServiceImpl implements LightningTalkService {

  @Autowired
  private LightningTalkRepository lightningTalkRepository;

  @Override
  public LightningTalk addLightningTalk(LightningTalk lightningTalk) {
    int uniqueId = 0;
    LightningTalk talk = null;
    while (true) {
      uniqueId = UniqueIdGenerator.generate4DigitRandomNumber();
      talk = getByUiqueId(uniqueId);
      if (talk == null && lightningTalkRepository.findByTopic(lightningTalk.getTopic()) == null) {
        lightningTalk.setUniqueId(uniqueId);
        talk = lightningTalkRepository.save(lightningTalk);
        break;
      }
    }
    return talk;
  }

  @Override
  public LightningTalk updateLightningTalk(LightningTalk lightningTalk) throws HackNightException {
    LightningTalk oldTalk = getByUiqueId(lightningTalk.getUniqueId());
    try {
      BeanUtils.copyProperties(oldTalk, lightningTalk);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new HackNightException("Exception while updating LightningTalk");
    }
    return lightningTalkRepository.save(oldTalk);
  }

  @Override
  public LightningTalk getByUiqueId(int uniqueId) {
    return lightningTalkRepository.findByUniqueId(uniqueId);
  }

  @Override
  public List<LightningTalk> addAll(List<LightningTalk> lightningTalks) {
    List<LightningTalk> addedTalks = new ArrayList<>();
    lightningTalks.forEach(LambdaExceptionWrapper.acceptWithException(lt -> addedTalks.add(addLightningTalk(lt))));
    return addedTalks;
  }

  @Override
  public List<LightningTalk> getAllLightningTalks() {
    Iterable<LightningTalk> talksIterable = lightningTalkRepository.findAllOrderByVoteCount();
    List<LightningTalk> talks = new ArrayList<>();
    talksIterable.forEach(talks::add);
    return talks;
  }

  @Override
  public LightningTalk getLightningTalkByTopic(String topic) {
    if (topic == null) {
      throw new IllegalArgumentException("Topic can't be null");
    }
    return lightningTalkRepository.findByTopic(topic);
  }

  @Override
  public boolean remove(int uniqueId) {
    lightningTalkRepository.deleteByUniqueId(uniqueId);
    return true;
  }

}
