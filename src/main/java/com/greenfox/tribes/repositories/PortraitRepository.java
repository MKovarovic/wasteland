package com.greenfox.tribes.repositories;

import com.greenfox.tribes.models.Persona;
import com.greenfox.tribes.models.Portrait;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortraitRepository  extends JpaRepository<Portrait, Long> {}
