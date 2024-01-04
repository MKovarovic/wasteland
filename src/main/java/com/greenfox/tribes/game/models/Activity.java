package com.greenfox.tribes.game.models;

import com.greenfox.tribes.gameitems.models.Equipment;
import com.greenfox.tribes.gameitems.services.EquipmentService;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Entity
public class Activity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id;
  private String name;
  private Long timestamp;
  private int time = 5; //number of minutes to complete
  private int pullRings = 10; // money part of reward
  Object rewardItem; //item part of reward, if any


}
