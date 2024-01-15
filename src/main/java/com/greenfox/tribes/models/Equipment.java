package com.greenfox.tribes.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity
public class Equipment {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "equipment_id")
  private Long id;

  private String name;
  private String type;
  private Integer price;
  private Integer atkBonus;
  private Integer defBonus;
  private Integer dmgBonus;
  private Integer hpBonus;
  private Integer lckBonus;

  @ToString.Exclude
  @OneToMany(mappedBy = "equipment", fetch = FetchType.EAGER)
  private List<CharacterEquipment> characters;



}
