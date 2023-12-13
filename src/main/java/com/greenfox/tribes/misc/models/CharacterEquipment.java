package com.greenfox.tribes.misc.models;

import com.greenfox.tribes.gameitems.models.Equipment;
import com.greenfox.tribes.persona.models.Persona;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CharacterEquipment {
  @Id @GeneratedValue private Long id;

  @ManyToOne
  @JoinColumn(name = "character_id")
  Persona persona;

  @ManyToOne
  @JoinColumn(name = "equipment_id")
  Equipment equipment;

  public void setPair(Persona character, Equipment equipment) {
    this.equipment = equipment;
    this.persona = character;
  }
}
