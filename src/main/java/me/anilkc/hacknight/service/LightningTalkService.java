package me.anilkc.hacknight.service;

import java.util.List;

import me.anilkc.hacknight.domain.LightningTalk;
import me.anilkc.hacknight.exception.HackNightException;

public interface LightningTalkService {

  LightningTalk addLightningTalk(LightningTalk lightningTalk);

  LightningTalk updateLightningTalk(LightningTalk lightningTalk) throws HackNightException;

  LightningTalk getByUiqueId(int uniqueId);

  List<LightningTalk> addAll(List<LightningTalk> lightningTalks);

  List<LightningTalk> getAllLightningTalks();

  boolean remove(int uniqueId);

  LightningTalk getLightningTalkByTopic(String topic);
}
