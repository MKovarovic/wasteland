package com.greenfox.tribes.misc.repositories;

import com.greenfox.tribes.misc.models.CharacterEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterEquipmentRepo extends JpaRepository<CharacterEquipment, Long> {

  Integer countAllByEquipment_IdAndPersona_Id(Long equipmentId, Long characterId);
}
