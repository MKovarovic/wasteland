package com.greenfox.tribes.game.models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;


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
  @OneToOne
  @JoinColumn(name = "id")
  ActivityLog activityLog;


}
