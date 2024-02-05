package com.greenfox.tribes.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MonsterDTO extends CombatantDTO {
  private Long id;
  private String characterName;
  private int hp;
  private int atk;
  private int dmg;
  private int def;
  private int lck;
  private int pullRing;
  private String picture;
}
