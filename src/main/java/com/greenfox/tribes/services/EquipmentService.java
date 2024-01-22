package com.greenfox.tribes.services;

import com.greenfox.tribes.dtos.EquipmentDTO;
import com.greenfox.tribes.models.Equipment;
import com.greenfox.tribes.repositories.EquipmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class EquipmentService {
  EquipmentRepository equipmentRepository;

  public Equipment randomEquipment() {
    List<Equipment> equipmentList = equipmentRepository.findAll();
    Random random = new Random();
    int index = random.nextInt(equipmentList.size());
    return equipmentList.get(index);
  }

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
    equipmentRepository.save(equipment);
  }

  public void newItem(
      String name,
      String type,
      int price,
      int atkBonus,
      int defBonus,
      int dmgBonus,
      int hpBonus,
      int lckBonus) {
    Equipment equipment = new Equipment();
    equipment.setName(name);
    equipment.setType(type);
    equipment.setPrice(price);
    equipment.setAtkBonus(atkBonus);
    equipment.setDefBonus(defBonus);
    equipment.setDmgBonus(dmgBonus);
    equipment.setHpBonus(hpBonus);
    equipment.setLckBonus(lckBonus);
    equipmentRepository.save(equipment);
  }

  public Equipment returnItem(Long id) {
    return equipmentRepository.findById(id).get();
  }

  public void deleteItem(Long id) {
    equipmentRepository.deleteById(id);
  }

  public void updateItem(EquipmentDTO dto, Long id) {
    Equipment equipment = equipmentRepository.findById(id).get();
    equipment.setName(dto.getName());
    equipment.setType(dto.getType());
    equipment.setPrice(dto.getPrice());
    equipment.setAtkBonus(dto.getAtkBonus());
    equipment.setDefBonus(dto.getDefBonus());
    equipment.setDmgBonus(dto.getDmgBonus());
    equipment.setHpBonus(dto.getHpBonus());
    equipment.setLckBonus(dto.getLckBonus());
    equipmentRepository.save(equipment);
  }
}
