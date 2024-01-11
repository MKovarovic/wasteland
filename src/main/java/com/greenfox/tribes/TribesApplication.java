package com.greenfox.tribes;

import com.greenfox.tribes.gameuser.repositories.UserRepository;
import com.greenfox.tribes.misc.repositories.CharacterEquipmentRepo;

import com.greenfox.tribes.persona.services.CharacterService;
import com.greenfox.tribes.gameitems.services.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TribesApplication implements ApplicationRunner {

  @Autowired UserRepository userRepository;
  @Autowired EquipmentService equipmentService;
  @Autowired CharacterService characterService;

  @Autowired CharacterEquipmentRepo pairingRepo;

  public static void main(String[] args) {
    SpringApplication.run(TribesApplication.class, args);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    /*          WastelandUser wastelandUser = new WastelandUser();
        wastelandUser.setUsername("pepa");
        wastelandUser.setPassword("$2a$10$TjIjpzO23iBpwxqSmBrgJe9vNzkHTDsausOI4U1lwCYECqyasrk8G");
        wastelandUser.setId(1L);
        userRepository.save(wastelandUser);
    characterService.addCharacter("MaidBot3000", 20, 30, 5, 80, 1, "Settler", 10);
              Persona persona = characterService.returnCharacter(1L);


            equipmentService.newItem( "Rusty Sword", "Weapon", 10, 5, 0, 0, 0, 1);
            equipmentService.newItem( "Wooden Shield", "Shield", 5, 0, 15, 0, 0, 5);
            equipmentService.newItem( "Hard hat", "Helmet", 12, 0, 0, 0, 5, 0);
     */

  }
}
