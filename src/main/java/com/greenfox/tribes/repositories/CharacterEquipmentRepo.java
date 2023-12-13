package com.greenfox.tribes.repositories;

import com.greenfox.tribes.models.CharacterEquipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterEquipmentRepo extends JpaRepository<CharacterEquipment, Long>{


    Integer countAllByEquipment_IdAndPlayerCharacter_Id(Long equipmentId, Long characterId);
}
