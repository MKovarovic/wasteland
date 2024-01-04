package com.greenfox.tribes.game.repositories;

import com.greenfox.tribes.game.models.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivityRepo extends JpaRepository<Activity, Long> {
    Optional<Activity> findActivityByName(String activityName);

}
