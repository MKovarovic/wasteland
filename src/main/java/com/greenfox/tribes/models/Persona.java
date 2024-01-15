package com.greenfox.tribes.models;

import com.greenfox.tribes.models.ActivityLog;
import com.greenfox.tribes.models.WastelandUser;
import com.greenfox.tribes.models.CharacterEquipment;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity
public class Persona {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "persona_id")
  private Long id;

  private String characterName;
  private String faction;

  @OneToOne(mappedBy = "persona")
  @ToString.Exclude
  WastelandUser player;

  // COMBAT STATS
  private Integer atk; // chance to hit
  private Integer def; // chance to not be hit
  private Integer dmg; // how much damage is dealt in case of hit
  private Integer lck; // chance to crit, i.e. deal double damage
  private Integer hp;

  // INVENTORY
  private Integer pullRing;

  @ToString.Exclude
  @OneToMany(mappedBy = "persona", fetch = FetchType.EAGER)
  private List<CharacterEquipment> inventory;

  @ToString.Exclude
  @OneToOne
  @JoinColumn(name = "activity_log_id")
  ActivityLog activityLog;

  // TECHNICAL STUFF
  private Boolean isBusy;
  Boolean isPremium = false;
}
