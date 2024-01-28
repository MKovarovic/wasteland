package com.greenfox.tribes.mappers;

import com.greenfox.tribes.dtos.EquipmentDTO;
import com.greenfox.tribes.models.Equipment;

public class EquipmentMapping {

  public static EquipmentDTO remap(Equipment eq) {

    return EquipmentDTO.fromEquipment(eq);
  }
}
