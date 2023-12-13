package com.greenfox.tribes.gameitems.dtos;

import com.greenfox.tribes.gameitems.models.Equipment;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EquipmentDTO {

  private String name;
  private String type;
  private Integer price;
  private Integer atkBonus;
  private Integer defBonus;
  private Integer dmgBonus;
  private Integer hpBonus;
  private Integer lckBonus;
  private Integer nrOwned;

  public static EquipmentDTO fromEquipment(Equipment eq, int numberOwned) {
    return new EquipmentDTO(
        eq.getName(),
        eq.getType(),
        eq.getPrice(),
        eq.getAtkBonus(),
        eq.getDefBonus(),
        eq.getDmgBonus(),
        eq.getHpBonus(),
        eq.getLckBonus(),
        numberOwned);
  }
}
