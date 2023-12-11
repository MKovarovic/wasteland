package com.greenfox.tribes.repositories;

import com.greenfox.tribes.entities.WastelandUser;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<WastelandUser, Long> {

  Optional<WastelandUser> findByUsername(String username);
}
