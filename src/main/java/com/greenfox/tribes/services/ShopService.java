package com.greenfox.tribes.services;

import com.greenfox.tribes.dtos.EquipmentDTO;
import com.greenfox.tribes.dtos.PersonaDTO;
import com.greenfox.tribes.dtos.ShopItemDTO;
import com.greenfox.tribes.models.Equipment;
import com.greenfox.tribes.models.WastelandUser;
import com.greenfox.tribes.repositories.UserRepository;
import com.greenfox.tribes.models.CharacterEquipment;
import com.greenfox.tribes.repositories.CharacterEquipmentRepository;
import com.greenfox.tribes.repositories.EquipmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ShopService {

  private EquipmentRepository equipmentRepository;
  private PersonaService personaService;
  private EquipmentService equipmentService;
  private UserRepository userRepository;
  private CharacterEquipmentRepository characterEquipmentRepository;

  public ArrayList<ShopItemDTO> getShoppingList() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    ArrayList<ShopItemDTO> shoppingList = new ArrayList<>();
    PersonaDTO personaDTO = personaService.readCharacter();
    List<EquipmentDTO> backpackItems = personaService.showEquipment(personaDTO);
    List<Equipment> allEquipment = equipmentRepository.findAll();

    for (Equipment equipment : allEquipment) {
      int numberOwned = 0;
      for (EquipmentDTO e : backpackItems) {
        CharacterEquipment characterEquipment =
            characterEquipmentRepository.findById(e.getId()).orElse(null);
        if (characterEquipment != null && !characterEquipment.getIsEquipped()) {
          numberOwned++;
        }
      }

      shoppingList.add(ShopItemDTO.fromEquipment(equipment, numberOwned));
    }
    return shoppingList;
  }

  public void buyStuff(Long id) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    CharacterEquipment characterEquipment = new CharacterEquipment();
    characterEquipment.setPair(user.getPersona(), equipmentService.getItem(id));
    user.getPersona()
        .setPullRing(user.getPersona().getPullRing() - equipmentService.getItem(id).getPrice());
    characterEquipmentRepository.save(characterEquipment);
  }

  public void sellStuff(Long id) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    Equipment equipment = equipmentService.getItem(id);
    CharacterEquipment characterEquipment =
        characterEquipmentRepository.findFirstByEquipmentAndPersona(equipment, user.getPersona());
    if (characterEquipment != null) {
      characterEquipmentRepository.delete(characterEquipment);
      user.getPersona().setPullRing(user.getPersona().getPullRing() + (equipment.getPrice() / 2));
    }

    userRepository.save(user);
  }
}
