package com.greenfox.tribes.misc.repositories;

import com.greenfox.tribes.misc.models.Monster;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Random;

@Repository
public interface MonsterRepo extends JpaRepository<Monster, Long> {
    @Query(nativeQuery=true, value="SELECT id FROM monster ORDER BY RAND() LIMIT 1")
    Optional<Long> findRandomMonsterId();
}
