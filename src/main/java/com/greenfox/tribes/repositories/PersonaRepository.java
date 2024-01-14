package com.greenfox.tribes.repositories;

import com.greenfox.tribes.models.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

  Optional<Persona> findPersonaByPlayer_Username(String loggedUser);

  @Query("SELECT id FROM Persona  WHERE faction = :faction ORDER BY RAND() LIMIT 1")
  Optional<Long> findRandomIdByFaction(@Param("faction")String faction);
}
