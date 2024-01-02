package com.greenfox.tribes.misc.repositories;

import com.greenfox.tribes.gameitems.models.Equipment;
import com.greenfox.tribes.misc.models.CharacterEquipment;
import com.greenfox.tribes.persona.models.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterEquipmentRepo extends JpaRepository<CharacterEquipment, Long> {


  Integer countAllByEquipmentAndPersona(Equipment e, Persona d);

  CharacterEquipment findFirstByEquipmentAndPersona(Equipment e, Persona d);
}
