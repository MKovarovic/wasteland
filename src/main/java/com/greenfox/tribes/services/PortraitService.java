package com.greenfox.tribes.services;

import com.greenfox.tribes.dtos.PortraitDTO;
import com.greenfox.tribes.mappers.PortraitMapper;
import com.greenfox.tribes.models.Portrait;
import com.greenfox.tribes.repositories.PersonaRepository;
import com.greenfox.tribes.repositories.PortraitRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PortraitService {

  private PersonaRepository personaRepository;

  private PortraitRepository portraitRepository;

  public Long createPortrait(
      String head,
      String hair,
      String eyes,
      String nose,
      String mouth,
      String eyebrows,
      Long personId) {
    Portrait portrait = new Portrait();
    portrait.setHead(head);
    portrait.setHair(hair);
    portrait.setEyes(eyes);
    portrait.setNose(nose);
    portrait.setEyebrows(eyebrows);
    portrait.setMouth(mouth);
    portrait.setPersona(personaRepository.findById(personId).orElse(null));
    portraitRepository.save(portrait);
    return portrait.getId();
  }

  public PortraitDTO findPortrait(long id) {
    Portrait portrait = portraitRepository.findById(id).get();
    return PortraitMapper.remap(portrait);
  }
}
