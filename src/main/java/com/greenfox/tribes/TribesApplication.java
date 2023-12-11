package com.greenfox.tribes;

import com.greenfox.tribes.entities.Player;
import com.greenfox.tribes.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TribesApplication implements ApplicationRunner {

  @Autowired UserRepository userRepository;

  public static void main(String[] args) {
    SpringApplication.run(TribesApplication.class, args);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    Player player = new Player();
    player.setUsername("pepa");
    player.setPassword("$2a$10$TjIjpzO23iBpwxqSmBrgJe9vNzkHTDsausOI4U1lwCYECqyasrk8G");
    player.setId(1L);
    userRepository.save(player);
  }
}
