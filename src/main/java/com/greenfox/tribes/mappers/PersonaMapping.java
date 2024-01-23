package com.greenfox.tribes.mappers;

import com.greenfox.tribes.dtos.PersonaDTO;
import com.greenfox.tribes.models.Persona;

public class PersonaMapping {
  public static PersonaDTO remap(Persona persona) {
    PersonaDTO dto = new PersonaDTO();
    dto.setId(persona.getId());
    dto.setCharacterName(persona.getCharacterName());
    dto.setFaction(persona.getFaction().toString());
    dto.setAtk(persona.getAtk());
    dto.setDmg(persona.getDmg());
    dto.setDef(persona.getDef());
    dto.setLck(persona.getLck());
    dto.setHp(persona.getHp());
    dto.setPullRing(persona.getPullRing());
    dto.setInventory(persona.getInventory());
    dto.setEquipedItems(persona.getInventory());
    return dto;
  }

}
