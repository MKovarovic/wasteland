package com.greenfox.tribes.models;

import com.greenfox.tribes.enums.ActivityType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ActivityLog {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private ActivityType type; // WORK, PvP, PvE, etc
  private Long timestamp;
  private int time = 5; // number of minutes to complete
  private int pullRings = 10; // money part of reward
  private Boolean givesItem; // if true, gives item upon completion
  private Long enemyID;

  @OneToOne
  @JoinColumn(name = "persona_id")
  Persona persona;
}
