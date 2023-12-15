package com.greenfox.tribes.game.services;

import com.greenfox.tribes.gameitems.dtos.EquipmentDTO;
import com.greenfox.tribes.gameitems.models.Equipment;
import com.greenfox.tribes.gameitems.services.EquipmentService;
import com.greenfox.tribes.gameuser.models.WastelandUser;
import com.greenfox.tribes.gameuser.repositories.UserRepository;
import com.greenfox.tribes.misc.models.CharacterEquipment;
import com.greenfox.tribes.misc.repositories.CharacterEquipmentRepo;
import com.greenfox.tribes.gameitems.repositories.EquipmentRepo;
import com.greenfox.tribes.persona.repositories.PersonaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ShopService {

  @Autowired EquipmentRepo equipmentRepo;
  @Autowired EquipmentService equipmentService;
  @Autowired UserRepository userRepository;
  @Autowired
  PersonaRepo personaRepo;
  @Autowired CharacterEquipmentRepo characterEquipmentRepo;

  public ArrayList<EquipmentDTO> getShoppingList() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    ArrayList<EquipmentDTO> shoppingList = new ArrayList<>();
    for (Equipment equipment : equipmentRepo.findAll()) {
      int numberOwned =
          characterEquipmentRepo.countAllByEquipmentAndPersona(
              equipment, userRepository.findByUsername(auth.getName()).get().getPersona());

      shoppingList.add(EquipmentDTO.fromEquipment(equipment, numberOwned));
    }
    return shoppingList;
  }

  public void buyStuff(Long id) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    CharacterEquipment pair = new CharacterEquipment();
    pair.setPair(user.getPersona(), equipmentService.returnItem(id));
    user.getPersona().setPullRing(user.getPersona().getPullRing() - equipmentService.returnItem(id).getPrice());
    characterEquipmentRepo.save(pair);
  }

  public void sellStuff(Long id) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    CharacterEquipment pair = new CharacterEquipment();
    Equipment equipment = equipmentService.returnItem(id);
    user.getPersona().getInventory().remove(equipment);
    user.getPersona().setPullRing(user.getPersona().getPullRing() + (equipment.getPrice()/2));
    userRepository.save(user);
  }


}
