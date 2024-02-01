package com.greenfox.tribes.dtos;

import com.greenfox.tribes.models.Equipment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class EquipmentDTO {
  private String name;

  private Long id;
  private String type;
  private Integer price;
  private Integer atkBonus;
  private Integer defBonus;
  private Integer dmgBonus;
  private Integer hpBonus;
  private Integer lckBonus;

  public static EquipmentDTO fromEquipment(Equipment eq) {
    return new EquipmentDTO(
        eq.getName(),
        eq.getId(),
        eq.getType(),
        eq.getPrice(),
        eq.getAtkBonus(),
        eq.getDefBonus(),
        eq.getDmgBonus(),
        eq.getHpBonus(),
        eq.getLckBonus());
  }

}
