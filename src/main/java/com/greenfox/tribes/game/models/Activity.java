package com.greenfox.tribes.game.models;

import com.greenfox.tribes.gameitems.models.Equipment;
import com.greenfox.tribes.gameitems.services.EquipmentService;
import com.greenfox.tribes.misc.models.CharacterEquipment;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Entity
public class Activity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String name;
  private Long timestamp;
  private int time = 5; //number of minutes to complete
  private int pullRings = 10; // money part of reward


  @ToString.Exclude
  @OneToMany(mappedBy = "activity", fetch = FetchType.EAGER)
  private List<Equipment> rewards; //item part of reward, if any

  Object rewardItem = rewards.get(0);//tbc

  @ToString.Exclude
  @OneToMany(mappedBy = "activity", fetch = FetchType.EAGER)
  private List<ActivityLog> activityLogs;

}
