package com.greenfox.tribes.services;

import com.greenfox.tribes.dtos.EquipmentDTO;
import com.greenfox.tribes.enums.Faction;
import com.greenfox.tribes.mappers.PersonaMapping;
import com.greenfox.tribes.models.CharacterEquipment;
import com.greenfox.tribes.repositories.CharacterEquipmentRepository;
import com.greenfox.tribes.models.Persona;
import com.greenfox.tribes.dtos.PersonaDTO;
import com.greenfox.tribes.repositories.PersonaRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class PersonaService {

  private PersonaRepository playerCharacters;
  private CharacterEquipmentRepository pairingRepo;
  private EquipmentService equipmentService;

  public Persona addCharacter(
      String name, int hp, int atk, int dmg, int def, int lck, Faction faction, int pullRing) {
    if (hp + atk + dmg + def + lck != 100) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    Persona character = new Persona(name, faction, atk, def, dmg, lck, hp, pullRing);
    playerCharacters.save(character);
    return character;
  }

  public PersonaDTO readCharacter(Long id) {
    Persona character = playerCharacters.findById(id).get();
    return PersonaMapping.remap(character);
  }

  public PersonaDTO readCharacter() {
    Persona loggedCharacter = getLoggedInPersona();
    return readCharacter(loggedCharacter.getId());
  }

  public List<EquipmentDTO> showEquipment(PersonaDTO personaDTO) {
    List<EquipmentDTO> result = new ArrayList<>();
    for (EquipmentDTO e : personaDTO.getBackpackItems()) {
      CharacterEquipment characterEquipment = equipmentService.findCharacterEquipment(e.getId());
      e.setIsEquipped(characterEquipment != null && characterEquipment.getIsEquipped());
      result.add(e);
    }

    return result;
  }

  public Persona getPersona(Long id) {
    return playerCharacters.findById(id).get();
  }

  public void toggleEquip(Long equipmentId) {

    Optional<CharacterEquipment> equipmentOptional = pairingRepo.findById(equipmentId);
    equipmentOptional.ifPresent(
        equipment -> {
          if (equipment.getIsEquipped() || canBeEquipped(equipment.getEquipment().getType())) {
            equipment.setIsEquipped(!equipment.getIsEquipped());
            pairingRepo.save(equipment);
          }
        });
  }

  public List<EquipmentDTO> getInventory() {
    Persona loggedCharacter = getLoggedInPersona();
    List<CharacterEquipment> list = loggedCharacter.getInventory();
    List<EquipmentDTO> inventory = new ArrayList<>();
    for (CharacterEquipment e : list) {
      EquipmentDTO dto = EquipmentDTO.fromEquipment(e.getEquipment());
      dto.setIsEquipped(e.getIsEquipped());
      inventory.add(dto);
    }
    return inventory;
  }

  public Boolean canBeEquipped(String type) {
    return getLoggedInPersona().getInventory().stream()
        .filter(CharacterEquipment::getIsEquipped)
        .noneMatch(e -> e.getEquipment().getType().equals(type));
  }

  public Persona getLoggedInPersona() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return playerCharacters
        .findPersonaByPlayer_Username(auth.getName())
        .orElseThrow(
            () ->
                new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "No logged in character found"));
  }

  public int[] getBonuses(PersonaDTO dto) {
    int atkBonus = 0;
    int defBonus = 0;
    int hpBonus = 0;
    int lckBonus = 0;
    int dmgBonus = 0;
    if (dto.getEquipedItems() != null) {

      for (EquipmentDTO e : dto.getEquipedItems()) {
        atkBonus += e.getAtkBonus();
        defBonus += e.getDefBonus();
        hpBonus += e.getHpBonus();
        lckBonus += e.getLckBonus();
        dmgBonus += e.getDmgBonus();
      }
    }

    int[] bonuses = {atkBonus, defBonus, hpBonus, lckBonus, dmgBonus};
    return bonuses;
  }
}
