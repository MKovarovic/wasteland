package com.greenfox.tribes.repositories;

import com.greenfox.tribes.models.WastelandUser;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<WastelandUser, Long> {

  Optional<WastelandUser> findByUsername(String username);
}
