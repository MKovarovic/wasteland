package com.greenfox.tribes.services;

import com.greenfox.tribes.models.Portrait;
import com.greenfox.tribes.repositories.PersonaRepository;
import com.greenfox.tribes.repositories.PortraitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PortraitService {

    @Autowired
    PersonaRepository personaRepository;

    @Autowired
    PortraitRepository portraitRepository;

    public void createPortrait(String head, String hair, String eyes, String nose, String mouth, Long personId) {
        Portrait portrait = new Portrait();
        portrait.setHead(head);
        portrait.setHair(hair);
        portrait.setEyes(eyes);
        portrait.setNose(nose);
        portrait.setMouth(mouth);
        portrait.setPersona(personaRepository.findById(personId).orElse(null));
        portraitRepository.save(portrait);

    }

}
