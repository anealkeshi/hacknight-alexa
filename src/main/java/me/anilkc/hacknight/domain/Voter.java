package me.anilkc.hacknight.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity(name = "voter")
@Table(name = "voter")
public class Voter {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonIgnore
  private int voterId;

  private String name;

  private String email;

  private int uniqueId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "lightning_talk_id")
  private LightningTalk lightningTalk;
}
