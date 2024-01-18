package com.greenfox.tribes.dtos;

import lombok.Data;

@Data
public class CombatantDTO {
  private String name;
  private int hp;
  private int atk;
  private int dmg;
  private int def;
  private int lck;
  private int pullRing;
}
