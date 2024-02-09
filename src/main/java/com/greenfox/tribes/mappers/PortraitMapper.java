package com.greenfox.tribes.mappers;

import com.greenfox.tribes.dtos.PortraitDTO;
import com.greenfox.tribes.models.Portrait;

public class PortraitMapper {
  public static PortraitDTO remap(Portrait portrait) {
    PortraitDTO dto = new PortraitDTO();
    dto.setPersonaId(portrait.getPersona().getId());
    dto.setHead(portrait.getHead());
    dto.setHair(portrait.getHair());
    dto.setEyes(portrait.getEyes());
    dto.setNose(portrait.getNose());
    dto.setMouth(portrait.getMouth());
    return dto;
  }
}
