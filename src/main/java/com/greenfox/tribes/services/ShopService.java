package com.greenfox.tribes.services;

import com.greenfox.tribes.dtos.EquipmentDTO;
import com.greenfox.tribes.models.Equipment;
import com.greenfox.tribes.repositories.CharacterEquipmentRepo;
import com.greenfox.tribes.repositories.EquipmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ShopService {

  @Autowired EquipmentRepo equipmentRepo;

  @Autowired CharacterEquipmentRepo characterEquipmentRepo;

  public ArrayList<EquipmentDTO> getShoppingList(Long character_id) {
    ArrayList<EquipmentDTO> shoppingList = new ArrayList<>();
    for (Equipment equipment : equipmentRepo.findAll()) {
      int numberOwned =
          characterEquipmentRepo.countAllByEquipment_IdAndPlayerCharacter_Id(
              equipment.getId(), character_id);
      shoppingList.add(EquipmentDTO.fromEquipment(equipment, numberOwned));
    }
    return shoppingList;
  }
}
