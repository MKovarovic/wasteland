package com.greenfox.tribes.models;

import com.greenfox.tribes.enums.Faction;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Monster extends Combatant {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id;

  // COMBAT STATS
  private Integer atk; // chance to hit
  private Integer def; // chance to not be hit
  private Integer dmg; // how much damage is dealt in case of hit
  private Integer lck; // chance to crit, i.e. deal double damage
  private Integer hp;
  private String name;

  private Faction faction = Faction.MONSTER;
  // INVENTORY
  private Integer pullRing;
}
