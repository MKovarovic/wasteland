package com.greenfox.tribes.persona.repositories;

import com.greenfox.tribes.persona.models.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepo extends JpaRepository<Persona, Long> {}
