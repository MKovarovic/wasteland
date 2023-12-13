package com.greenfox.tribes.persona.services;

import com.greenfox.tribes.persona.models.Persona;
import com.greenfox.tribes.persona.dtos.PersonaDTO;
import com.greenfox.tribes.persona.repositories.PersonaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {

  @Autowired
  PersonaRepo playerCharacters;

  public void addCharacter(PersonaDTO dto) {
    Persona character = new Persona();
    character.setCharacterName(dto.getCharacterName());
    character.setHp(dto.getHp());
    character.setAtk(dto.getAtk());
    character.setDmg(dto.getDmg());
    character.setDef(dto.getDef());
    character.setLck(dto.getLck());
    character.setFaction(dto.getFaction());
    character.setGold(dto.getGold());
    playerCharacters.save(character);
  }

  public void addCharacter(
      String name, int hp, int atk, int dmg, int def, int lck, String faction, int gold) {
    Persona character = new Persona();
    character.setCharacterName(name);
    character.setHp(hp);
    character.setAtk(atk);
    character.setDmg(dmg);
    character.setDef(def);
    character.setLck(lck);
    character.setFaction(faction);
    character.setGold(gold);
    playerCharacters.save(character);
  }

  public PersonaDTO readCharacter(Long id) {
    Persona character = playerCharacters.findById(id).get();
    PersonaDTO dto = new PersonaDTO();
    dto.setId(character.getId());
    dto.setCharacterName(character.getCharacterName());
    dto.setFaction(character.getFaction());
    dto.setAtk(character.getAtk());
    dto.setDmg(character.getDmg());
    dto.setDef(character.getDef());
    dto.setLck(character.getLck());
    dto.setHp(character.getHp());
    dto.setGold(character.getGold());
    // dto.setInventory(character.getInventory());

    return dto;
  }

  public Persona returnCharacter(Long id) {
    return playerCharacters.findById(id).get();
  }
}
