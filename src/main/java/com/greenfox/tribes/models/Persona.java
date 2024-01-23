package com.greenfox.tribes.models;

import com.greenfox.tribes.enums.Faction;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Persona extends Combatant {

  public Persona(
      String characterName,
      Faction faction,
      Integer atk,
      Integer def,
      Integer dmg,
      Integer lck,
      Integer hp,
      Integer pullRing) {
    this.characterName = characterName;
    this.faction = faction;
    this.atk = atk;
    this.def = def;
    this.dmg = dmg;
    this.lck = lck;
    this.hp = hp;
    this.pullRing = pullRing;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "persona_id")
  private Long id;

  private String characterName;

  private Faction faction;

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

  @ToString.Exclude
  @OneToOne
  @JoinColumn(name = "portrait_id")
  Portrait portrait;

  // TECHNICAL STUFF

  Boolean isPremium = false;
}
