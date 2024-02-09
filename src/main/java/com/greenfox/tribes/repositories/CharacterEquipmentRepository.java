package com.greenfox.tribes.repositories;

import com.greenfox.tribes.models.Equipment;
import com.greenfox.tribes.models.CharacterEquipment;
import com.greenfox.tribes.models.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterEquipmentRepository extends JpaRepository<CharacterEquipment, Long> {


  Integer countAllByEquipmentAndPersona(Equipment e, Persona d);

  Integer countAllByPersona_IdAndEquipment_IdAndIsEquipped(Long personaID, Long equipmentId, boolean equipped);

  CharacterEquipment findFirstByEquipmentAndPersona(Equipment e, Persona d);
}
