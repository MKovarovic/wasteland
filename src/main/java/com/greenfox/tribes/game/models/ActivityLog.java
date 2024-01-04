package com.greenfox.tribes.game.models;

import com.greenfox.tribes.gameitems.models.Equipment;
import com.greenfox.tribes.misc.models.CharacterEquipment;
import com.greenfox.tribes.persona.models.Persona;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity
public class ActivityLog {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

    private String type; // WORK, PvP, PvE, etc
    private Long timestamp;
    private int time = 5; // number of minutes to complete
    private int pullRings = 10; // money part of reward

    private Boolean givesItem; // if true, gives item upon completion

  @OneToOne
  @JoinColumn(name = "persona_id")
  Persona persona;



}
