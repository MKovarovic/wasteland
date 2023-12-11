package com.greenfox.tribes.services;

import com.greenfox.tribes.entities.Player;
import com.greenfox.tribes.repositories.UserRepository;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Player player =
        userRepository
            .findByUsername(username)
            .orElseThrow(
                () -> new UsernameNotFoundException("Username " + username + " not found."));

    return new org.springframework.security.core.userdetails.User(
        username, player.getPassword(), Set.of());
  }
}
