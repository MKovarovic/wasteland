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
  private Integer ATKbonus;
  private Integer DEFbonus;
  private Integer DMGbonus;
  private Integer HPbonus;
  private Integer LCKbonus;
  private Integer nrOwned;

  public static EquipmentDTO fromEquipment(Equipment eq, int numberOwned) {
    return new EquipmentDTO(
        eq.getName(),
        eq.getType(),
        eq.getPrice(),
        eq.getATKbonus(),
        eq.getDEFbonus(),
        eq.getDMGbonus(),
        eq.getHPbonus(),
        eq.getLCKbonus(),
        numberOwned);
  }
}
