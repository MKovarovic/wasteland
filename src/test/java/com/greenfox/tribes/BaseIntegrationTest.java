package com.greenfox.tribes;

import com.greenfox.tribes.gameitems.services.EquipmentService;
import com.greenfox.tribes.gameuser.models.WastelandUser;
import com.greenfox.tribes.gameuser.repositories.UserRepository;
import com.greenfox.tribes.misc.models.CharacterEquipment;
import com.greenfox.tribes.misc.repositories.CharacterEquipmentRepo;
import com.greenfox.tribes.persona.services.CharacterService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseIntegrationTest extends BaseTest {

  @Autowired private UserRepository userRepository;
  @Autowired private EquipmentService equipmentService;
  @Autowired private CharacterService characterService;
  @Autowired private CharacterEquipmentRepo pairingRepo;
  @Autowired private PasswordEncoder passwordEncoder;

  @BeforeAll
  void setupDatabase() {
    WastelandUser wastelandUser = new WastelandUser();
    wastelandUser.setUsername("test");
    wastelandUser.setPassword(passwordEncoder.encode("test"));
    wastelandUser.setId(1L);
    userRepository.save(wastelandUser);

    equipmentService.newItem("Rusty Sword", "Weapon", 10, 5, 0, 0, 0, 1);
    equipmentService.newItem("Wooden Shield", "Shield", 0, 0, 15, 0, 0, 5);
    equipmentService.newItem("Hard hat", "Helmet", 0, 0, 0, 0, 5, 0);
    characterService.addCharacter("MaidBot3000", 20, 30, 5, 80, 1, "Bots", 10);
    CharacterEquipment pair = new CharacterEquipment();
    pair.setPair(characterService.returnCharacter(1L), equipmentService.returnItem(1L));
    pairingRepo.save(pair);
  }
}
