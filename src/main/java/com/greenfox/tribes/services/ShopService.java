package com.greenfox.tribes.services;

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

  public ArrayList<Object> getShoppingList(Long character_id){
    ArrayList<Object> shoppingList = new ArrayList<>();
    for (Equipment equipment : equipmentRepo.findAll()) {
      ArrayList<Object> shoppingItem = new ArrayList<>();
      shoppingItem.add(equipment.getName());
      shoppingItem.add(equipment.getType());
      shoppingItem.add(equipment.getATKbonus());
      shoppingItem.add(equipment.getDEFbonus());
      shoppingItem.add(equipment.getDMGbonus());
      shoppingItem.add(equipment.getHPbonus());
      shoppingItem.add(equipment.getLCKbonus());
      shoppingItem.add(equipment.getPrice());
      shoppingItem.add(characterEquipmentRepo.findAllByEquipment_IdAndPlayerCharacter_Id(equipment.getId(), character_id));
      shoppingList.add(shoppingItem);
      System.out.println(shoppingItem);
    }
    return shoppingList;
  }
}
