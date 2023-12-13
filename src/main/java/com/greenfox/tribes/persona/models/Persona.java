package com.greenfox.tribes.persona.models;

import com.greenfox.tribes.misc.models.CharacterEquipment;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity
public class Persona {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "character_id")
      private Long id;

  private String characterName;
  private String faction;

  //    Player player;

  // COMBAT STATS
  private Integer atk; // chance to hit
  private Integer def; // chance to not be hit
  private Integer dmg; // how much damage is dealt in case of hit
  private Integer lck; // chance to crit, i.e. deal double damage
  private Integer hp;

  // INVENTORY
  private Integer gold;

  @ToString.Exclude
  @OneToMany(mappedBy = "playerCharacter", fetch = FetchType.EAGER)
  private List<CharacterEquipment> inventory;

  // TECHNICAL STUFF
  private Boolean isBusy;
  Boolean isPremium = false;
}
