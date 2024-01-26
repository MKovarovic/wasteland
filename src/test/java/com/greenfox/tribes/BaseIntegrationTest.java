package com.greenfox.tribes;

import com.greenfox.tribes.enums.Faction;
import com.greenfox.tribes.services.EquipmentService;
import com.greenfox.tribes.models.WastelandUser;
import com.greenfox.tribes.repositories.UserRepository;
import com.greenfox.tribes.models.CharacterEquipment;
import com.greenfox.tribes.repositories.CharacterEquipmentRepository;

import com.greenfox.tribes.services.PersonaService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseIntegrationTest extends BaseTest {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private EquipmentService equipmentService;
  @Autowired
  private PersonaService characterService;
  @Autowired
  private CharacterEquipmentRepository pairingRepo;
  @Autowired
  private PasswordEncoder passwordEncoder;

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
    characterService.addCharacter("MaidBot3000", 20, 30, 10, 30, 10, Faction.MONSTER, 10);
    CharacterEquipment pair = new CharacterEquipment();
    pair.setPair(characterService.getPersona(1L), equipmentService.getItem(1L));
    pairingRepo.save(pair);
  }
}
