package com.greenfox.tribes.models;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class Combatant {
  private Long id;
  private Integer atk; // chance to hit
  private Integer def; // chance to not be hit
  private Integer dmg; // how much damage is dealt in case of hit
  private Integer lck; // chance to crit, i.e. deal double damage
  private Integer hp;
  private String name;
  private Integer pullRing;
}
