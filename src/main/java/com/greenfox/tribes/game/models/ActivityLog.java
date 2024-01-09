package com.greenfox.tribes.game.models;

import com.greenfox.tribes.game.enums.ActivityType;
import com.greenfox.tribes.persona.models.Persona;
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

  @OneToOne
  @JoinColumn(name = "persona_id")
  Persona persona;
}
