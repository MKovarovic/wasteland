package com.greenfox.tribes.game.dtos;

import com.greenfox.tribes.game.enums.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ActivityDTO {
  private ActivityType type;
  private Long timestamp;
  private int time;
  private int pullRings;
  private Boolean givesItem;
  private Long enemyID;
  Long personaID;
}
