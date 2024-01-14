package com.greenfox.tribes.services;

import com.greenfox.tribes.dtos.EquipmentDTO;
import com.greenfox.tribes.models.Equipment;
import com.greenfox.tribes.models.WastelandUser;
import com.greenfox.tribes.repositories.UserRepository;
import com.greenfox.tribes.models.CharacterEquipment;
import com.greenfox.tribes.repositories.CharacterEquipmentRepository;
import com.greenfox.tribes.repositories.EquipmentRepository;
import com.greenfox.tribes.repositories.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ShopService {

  @Autowired
  EquipmentRepository equipmentRepository;
  @Autowired EquipmentService equipmentService;
  @Autowired UserRepository userRepository;
  @Autowired
  PersonaRepository personaRepository;
  @Autowired
  CharacterEquipmentRepository characterEquipmentRepository;

  public ArrayList<EquipmentDTO> getShoppingList() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    ArrayList<EquipmentDTO> shoppingList = new ArrayList<>();
    for (Equipment equipment : equipmentRepository.findAll()) {
      int numberOwned =
          characterEquipmentRepository.countAllByEquipmentAndPersona(
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
    user.getPersona()
        .setPullRing(user.getPersona().getPullRing() - equipmentService.returnItem(id).getPrice());
    characterEquipmentRepository.save(pair);
  }

  public void sellStuff(Long id) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    Equipment equipment = equipmentService.returnItem(id);
    CharacterEquipment pair =
        characterEquipmentRepository.findFirstByEquipmentAndPersona(equipment, user.getPersona());
    characterEquipmentRepository.delete(pair);
    user.getPersona().setPullRing(user.getPersona().getPullRing() + (equipment.getPrice() / 2));
    userRepository.save(user);
  }
}
