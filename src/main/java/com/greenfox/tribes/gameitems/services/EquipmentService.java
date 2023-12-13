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
    equipment.setAtkBonus(dto.getAtkBonus());
    equipment.setDefBonus(dto.getDefBonus());
    equipment.setDmgBonus(dto.getDmgBonus());
    equipment.setHpBonus(dto.getHpBonus());
    equipment.setLckBonus(dto.getLckBonus());
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
    equipment.setAtkBonus(ATKbonus);
    equipment.setDefBonus(DEFbonus);
    equipment.setDmgBonus(DMGbonus);
    equipment.setHpBonus(HPbonus);
    equipment.setLckBonus(LCKbonus);
    equipmentRepo.save(equipment);
  }

  public Equipment returnItem(Long id) {
    return equipmentRepo.findById(id).get();
  }

  public void deleteItem(Long id){
    equipmentRepo.deleteById(id);
  }

  public void updateItem(EquipmentDTO dto, Long id){
    Equipment equipment = equipmentRepo.findById(id).get();
    equipment.setName(dto.getName());
    equipment.setType(dto.getType());
    equipment.setPrice(dto.getPrice());
    equipment.setAtkBonus(dto.getAtkBonus());
    equipment.setDefBonus(dto.getDefBonus());
    equipment.setDmgBonus(dto.getDmgBonus());
    equipment.setHpBonus(dto.getHpBonus());
    equipment.setLckBonus(dto.getLckBonus());
    equipmentRepo.save(equipment);
  }
}
