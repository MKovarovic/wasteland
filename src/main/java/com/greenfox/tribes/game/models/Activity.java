package com.greenfox.tribes.game.models;

import com.greenfox.tribes.gameitems.models.Equipment;
import com.greenfox.tribes.gameitems.services.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;

public class Activity {
  private String name;
  private Long timestamp;
  @Autowired EquipmentService equipmentService;
  public Object[] getReward() {
    Object[] rewards = new Object[2];
    Integer pullRings = 10;
    Equipment rewardItem = equipmentService.randomEquipment();
    rewards[0] = pullRings;
    rewards[1] = rewardItem;
    return rewards;
  }
}
