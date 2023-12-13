package com.greenfox.tribes.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CharacterEquipment {
  @Id @GeneratedValue private Long id;

  @ManyToOne
  @JoinColumn(name = "character_id")
  PlayerCharacter playerCharacter;

  @ManyToOne
  @JoinColumn(name = "equipment_id")
  Equipment equipment;

  public void setPair(PlayerCharacter character, Equipment equipment) {
    this.equipment = equipment;
    this.playerCharacter = character;
  }
}
