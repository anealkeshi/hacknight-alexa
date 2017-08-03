package me.anilkc.hacknight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import me.anilkc.hacknight.domain.LightningTalk;

@Repository
public interface LightningTalkRepository extends CrudRepository<LightningTalk, Integer> {

  LightningTalk findByUniqueId(int uniqueId);

  boolean deleteByUniqueId(int uniqueId);

  LightningTalk findByTopic(String topic);

  @Query("SELECT l FROM lightning_talk l ORDER BY l.voteCount DESC")
  List<LightningTalk> findAllOrderByVoteCount();
}
