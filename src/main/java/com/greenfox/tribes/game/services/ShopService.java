package com.greenfox.tribes.game.services;

import com.greenfox.tribes.gameitems.dtos.EquipmentDTO;
import com.greenfox.tribes.gameitems.models.Equipment;
import com.greenfox.tribes.misc.models.repositories.CharacterEquipmentRepo;
import com.greenfox.tribes.gameitems.repositories.EquipmentRepo;
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
          characterEquipmentRepo.countAllByEquipment_IdAndPersona_Id(
              equipment.getId(), character_id);
      shoppingList.add(EquipmentDTO.fromEquipment(equipment, numberOwned));
    }
    return shoppingList;
  }
}
