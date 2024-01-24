package com.greenfox.tribes.services;

import com.greenfox.tribes.enums.Faction;
import com.greenfox.tribes.mappers.PersonaMapping;
import com.greenfox.tribes.models.CharacterEquipment;
import com.greenfox.tribes.models.Equipment;
import com.greenfox.tribes.repositories.CharacterEquipmentRepository;
import com.greenfox.tribes.models.Persona;
import com.greenfox.tribes.dtos.PersonaDTO;
import com.greenfox.tribes.repositories.PersonaRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class CharacterService {

  private PersonaRepository playerCharacters;
  private CharacterEquipmentRepository pairingRepo;

  public Persona addCharacter(
      String name, int hp, int atk, int dmg, int def, int lck, String faction, int pullRing) {
    if (hp + atk + dmg + def + lck != 100) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    Faction actualFaction = Faction.valueOf(faction.toUpperCase());

    Persona character = new Persona(name, actualFaction, atk, def, dmg, lck, hp, pullRing);
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

  public Persona returnCharacter(Long id) {
    return playerCharacters.findById(id).get();
  }

  public void toggleEquip(Long id) {
    Persona character = getLoggedInPersona();
    Optional<CharacterEquipment> equipmentOptional =
        character.getInventory().stream()
            .filter(e -> Objects.equals(e.getEquipment().getId(), id))
            .findFirst();
    if (equipmentOptional.isPresent()) {
      CharacterEquipment equipment = equipmentOptional.get();
      if (equipment.getIsEquipped() || canBeEquipped(equipment.getEquipment().getType())) {
        equipment.setIsEquipped(!equipment.getIsEquipped());
        pairingRepo.save(equipment);
      }
    }
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

      for (Equipment e : dto.getEquipedItems()) {
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
