package com.greenfox.tribes.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
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
  private Integer ATKbonus;
  private Integer DEFbonus;
  private Integer DMGbonus;
  private Integer HPbonus;
  private Integer LCKbonus;

  @ToString.Exclude
  @OneToMany(mappedBy = "equipment", fetch = FetchType.EAGER)
  private List<CharacterEquipment> characters;
}
