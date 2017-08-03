package me.anilkc.hacknight.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity(name = "lightning_talk")
@Table(name = "lightning_talk")
public class LightningTalk implements Serializable {

  private static final long serialVersionUID = 1664673929977585764L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonIgnore
  private int talkId;

  @NotBlank
  private String topic;

  private String description;

  @NotBlank
  private String presenter;

  @NotBlank
  private String email;

  private int voteCount;

  private int uniqueId;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "lightningTalk")
  @JsonIgnore
  private List<Voter> voters = new ArrayList<>();

}
