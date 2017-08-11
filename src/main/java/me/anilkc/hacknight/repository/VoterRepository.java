package me.anilkc.hacknight.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import me.anilkc.hacknight.domain.Voter;

@Repository
public interface VoterRepository extends CrudRepository<Voter, Integer> {

  Voter findByUniqueId(int uniqueId);

  int deleteByUniqueId(int uniqueId);

  Voter findByEmail(String email);
}
