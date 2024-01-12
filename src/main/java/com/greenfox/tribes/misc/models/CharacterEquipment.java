package com.greenfox.tribes.misc.models;

import com.greenfox.tribes.gameitems.models.Equipment;
import com.greenfox.tribes.persona.models.Persona;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Data
public class CharacterEquipment {
  @Id @GeneratedValue private Long id;

  @ManyToOne
  @JoinColumn(name = "persona_id")
  Persona persona;

  @ManyToOne
  @JoinColumn(name = "equipment_id")
  Equipment equipment;

  //@ColumnDefault("false")
  Boolean isEquipped = false;

  public void setPair(Persona character, Equipment equipment) {
    this.equipment = equipment;
    this.persona = character;
  }
}

// each entry includes both objects, which causes kinda entry-ception with equipment list. Fix it,
// maybe??
