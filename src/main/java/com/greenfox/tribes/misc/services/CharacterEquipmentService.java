package com.greenfox.tribes.misc.services;

import com.greenfox.tribes.misc.models.repositories.CharacterEquipmentRepo;
import com.greenfox.tribes.gameitems.models.Equipment;
import com.greenfox.tribes.misc.models.CharacterEquipment;
import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CharacterEquipmentService {
    @Autowired
    CharacterEquipmentRepo repo;

/*    public List<Equipment> findMyStuff(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CharacterEquipment bundle = repo.findById(aut)
    }*/

}
