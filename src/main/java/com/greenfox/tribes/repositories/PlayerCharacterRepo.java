package com.greenfox.tribes.repositories;


import com.greenfox.tribes.models.PlayerCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerCharacterRepo extends JpaRepository<PlayerCharacter, Long> {
}
