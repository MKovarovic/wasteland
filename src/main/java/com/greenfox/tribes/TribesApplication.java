package com.greenfox.tribes;

import com.greenfox.tribes.models.WastelandUser;
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
    WastelandUser wastelandUser = new WastelandUser();
    wastelandUser.setUsername("pepa");
    wastelandUser.setPassword("$2a$10$TjIjpzO23iBpwxqSmBrgJe9vNzkHTDsausOI4U1lwCYECqyasrk8G");
    wastelandUser.setId(1L);
    userRepository.save(wastelandUser);
  }
}
