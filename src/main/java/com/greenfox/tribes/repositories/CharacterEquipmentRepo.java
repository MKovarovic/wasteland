package com.greenfox.tribes.repositories;

import com.greenfox.tribes.models.CharacterEquipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterEquipmentRepo extends JpaRepository<CharacterEquipment, Long>{


    Integer findAllByEquipmentAndPlayerCharacter(Long equipment_id, Long character_id);

}
