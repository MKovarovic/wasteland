package com.greenfox.tribes.gameitems.services;

import com.greenfox.tribes.gameitems.dtos.EquipmentDTO;
import com.greenfox.tribes.gameitems.models.Equipment;
import com.greenfox.tribes.gameitems.repositories.EquipmentRepo;
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

  public void newItem(
      String name,
      String type,
      int price,
      int ATKbonus,
      int DEFbonus,
      int DMGbonus,
      int HPbonus,
      int LCKbonus) {
    Equipment equipment = new Equipment();
    equipment.setName(name);
    equipment.setType(type);
    equipment.setPrice(price);
    equipment.setATKbonus(ATKbonus);
    equipment.setDEFbonus(DEFbonus);
    equipment.setDMGbonus(DMGbonus);
    equipment.setHPbonus(HPbonus);
    equipment.setLCKbonus(LCKbonus);
    equipmentRepo.save(equipment);
  }

  public Equipment returnItem(Long id) {
    return equipmentRepo.findById(id).get();
  }
}
