package com.greenfox.tribes.services;

import com.greenfox.tribes.dtos.EquipmentDTO;
import com.greenfox.tribes.models.Equipment;
import com.greenfox.tribes.repositories.EquipmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EquipmentService {
  @Autowired EquipmentRepo equipmentRepo;

  public void newItem(EquipmentDTO dto) {
    Equipment equipment = new Equipment();
    equipment.setName(dto.getName());
    equipment.setType(dto.getType());
    equipment.setPrice(dto.getPrice());
    equipment.setATKbonus(dto.getATKbonus());
    equipment.setDEFbonus(dto.getDEFbonus());
    equipment.setDMGbonus(dto.getDMGbonus());
    equipment.setHPbonus(dto.getHPbonus());
    equipment.setLCKbonus(dto.getLCKbonus());
    equipmentRepo.save(equipment);
  }
}

