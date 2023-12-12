package com.greenfox.tribes.services;

import com.greenfox.tribes.models.PlayerCharacter;
import com.greenfox.tribes.dtos.CharacterDTO;
import com.greenfox.tribes.repositories.PlayerCharacterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {

  @Autowired PlayerCharacterRepo playerCharacters;

  public void addCharacter(CharacterDTO dto) {
    PlayerCharacter character = new PlayerCharacter();
    character.setCharacterName(dto.getCharacterName());
    character.setHP(dto.getHP());
    character.setATK(dto.getATK());
    character.setDMG(dto.getDMG());
    character.setDEF(dto.getDEF());
    character.setLCK(dto.getLCK());
    character.setFaction(dto.getFaction());
    character.setGold(dto.getGold());
    playerCharacters.save(character);
  }

  public CharacterDTO readCharacter(Long id) {
    PlayerCharacter character = playerCharacters.findById(id).get();
    CharacterDTO dto = new CharacterDTO();
    dto.setId(character.getId());
    dto.setCharacterName(character.getCharacterName());
    dto.setFaction(character.getFaction());
    dto.setATK(character.getATK());
    dto.setDMG(character.getDMG());
    dto.setDEF(character.getDEF());
    dto.setLCK(character.getLCK());
    dto.setHP(character.getHP());
    dto.setGold(character.getGold());
   // dto.setInventory(character.getInventory());

    return dto;
  }
}
