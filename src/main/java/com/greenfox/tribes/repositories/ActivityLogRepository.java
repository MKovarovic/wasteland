package com.greenfox.tribes.repositories;

import com.greenfox.tribes.models.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

  Optional<ActivityLog> findActivityLogByPersonaId(Long personaId);
}
