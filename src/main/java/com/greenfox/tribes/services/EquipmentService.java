package com.greenfox.tribes.services;

import com.greenfox.tribes.models.CharacterEquipment;
import com.greenfox.tribes.models.Equipment;
import com.greenfox.tribes.repositories.CharacterEquipmentRepository;
import com.greenfox.tribes.repositories.EquipmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class EquipmentService {
  private EquipmentRepository equipmentRepository;
  private CharacterEquipmentRepository characterEquipmentRepository;

  public Equipment randomEquipment() {
    List<Equipment> equipmentList = equipmentRepository.findAll();
    Random random = new Random();
    int index = random.nextInt(equipmentList.size());
    return equipmentList.get(index);
  }

  public CharacterEquipment findCharacterEquipment(Long id) {

    return characterEquipmentRepository.findById(id).orElse(null);
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

  public Equipment getItem(Long id) {
    return equipmentRepository.findById(id).get();
  }
}
