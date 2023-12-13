package com.greenfox.tribes.persona.repositories;

import com.greenfox.tribes.gameuser.models.WastelandUser;
import com.greenfox.tribes.persona.models.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonaRepo extends JpaRepository<Persona, Long> {

    Optional<Persona> findPersonaByPlayer_Username(String loggedUser);
}
