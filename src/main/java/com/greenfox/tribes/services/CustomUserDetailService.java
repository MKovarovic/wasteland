package com.greenfox.tribes.services;

import com.greenfox.tribes.entities.WastelandUser;
import com.greenfox.tribes.exceptions.UserAlreadyExistsException;
import com.greenfox.tribes.repositories.UserRepository;
import java.util.Optional;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    WastelandUser wastelandUser =
        userRepository
            .findByUsername(username)
            .orElseThrow(
                () -> new UsernameNotFoundException("Username " + username + " not found."));

    return new org.springframework.security.core.userdetails.User(
        username, wastelandUser.getPassword(), Set.of());
  }

  public void createUser(String username, String password) throws UserAlreadyExistsException {
    Optional<WastelandUser> wastelandUser = userRepository.findByUsername(username);
    if (wastelandUser.isPresent()) {
      throw new UserAlreadyExistsException();
    }
    WastelandUser user = new WastelandUser();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));

    userRepository.save(user);
  }
}
