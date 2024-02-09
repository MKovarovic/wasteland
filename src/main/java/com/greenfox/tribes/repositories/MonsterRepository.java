package com.greenfox.tribes.repositories;

import com.greenfox.tribes.models.Monster;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MonsterRepository extends JpaRepository<Monster, Long> {
  @Query(nativeQuery = true, value = "SELECT id FROM monster ORDER BY RAND() LIMIT 1")
  Optional<Long> findRandomMonsterId();

  @Query(nativeQuery = true, value = "SELECT id FROM monster ORDER BY RAND() LIMIT 3")
  Optional<Long[]> findRandomMonsters();
}
