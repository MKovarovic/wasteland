package com.greenfox.tribes.dtos;

import com.greenfox.tribes.models.Equipment;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShopItemDTO {

  private String name;
  private Long id;
  private String type;
  private Integer price;
  private Integer atkBonus;
  private Integer defBonus;
  private Integer dmgBonus;
  private Integer hpBonus;
  private Integer lckBonus;
  private Integer nrOwned;

  public static ShopItemDTO fromEquipment(Equipment eq, int numberOwned) {
    return new ShopItemDTO(
        eq.getName(),
        eq.getId(),
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
