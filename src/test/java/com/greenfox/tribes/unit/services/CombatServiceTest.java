package com.greenfox.tribes.unit.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.greenfox.tribes.BaseTest;
import com.greenfox.tribes.dtos.CombatantDTO;
import com.greenfox.tribes.dtos.EquipmentDTO;
import com.greenfox.tribes.dtos.PersonaDTO;
import com.greenfox.tribes.enums.Faction;
import com.greenfox.tribes.mappers.EquipmentMapping;
import com.greenfox.tribes.mappers.PersonaMapping;
import com.greenfox.tribes.models.Combatant;
import com.greenfox.tribes.models.Equipment;
import com.greenfox.tribes.models.Persona;
import com.greenfox.tribes.repositories.PersonaRepository;
import com.greenfox.tribes.services.ActivityService;
import com.greenfox.tribes.services.CombatService;
import com.greenfox.tribes.services.PersonaService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Pair;

class CombatServiceTest extends BaseTest {

  @Autowired private CombatService gladiatorService;

  @MockBean private PersonaRepository personaRepository;
  @Mock ActivityService activityService;

  @Mock private PersonaRepository personaRepo;
  @MockBean private PersonaService characterService;
  @InjectMocks private CombatService combatService;
  private Persona testGladiator;
  private PersonaDTO testGladiatorDTO;
  private EquipmentDTO testEquipment1;
  private EquipmentDTO testEquipment2;
  private EquipmentDTO testEquipment3;

  private Persona getPersona() {
    Persona persona = new Persona("Gladiator", Faction.RAIDER, 50, 20, 10, 10, 100, 1);
    persona.setInventory(new ArrayList<>());
    return persona;
  }

  private PersonaDTO getPersonaDTO() {
    return PersonaMapping.remap(getPersona());
  }

  private Equipment getEquipment(int price, String type) {
    Equipment res = new Equipment();
    res.setName("Sword");
    res.setType(type);
    res.setPrice(price);
    res.setAtkBonus(3);
    res.setDefBonus(2);
    res.setDmgBonus(4);
    res.setHpBonus(10);
    res.setLckBonus(1);
    return res;
  }

  @BeforeEach
  public void setUp() {
    // Initialize test data
    testGladiator = getPersona();
    testGladiatorDTO = getPersonaDTO();
    testEquipment1 = EquipmentMapping.remap(getEquipment(30, "Weapon"));
    testEquipment2 = EquipmentMapping.remap(getEquipment(50, "Tool"));
    testEquipment3 = EquipmentMapping.remap(getEquipment(70, "Amulet"));
    testGladiatorDTO.setInventoryEquipment(List.of(testEquipment1, testEquipment2, testEquipment3));
    testGladiatorDTO.setEquippedItemsEquipment(List.of(testEquipment1, testEquipment2));

    when(characterService.readCharacter(any())).thenReturn(testGladiatorDTO);
    when(characterService.readCharacter()).thenReturn(testGladiatorDTO);
    when(personaRepository.findById(anyLong())).thenReturn(Optional.of(testGladiator));
  }

  @Test
  public void equipGladiator_PersonaExists_EquipmentApplied() {
    // Setup
    Long gladiatorId = 1L;

    // Act
    PersonaDTO result = gladiatorService.equipGladiator(gladiatorId);

    // Assert
    assertNotNull(result);
    assertEquals(
        testGladiator.getAtk() + testEquipment1.getAtkBonus() + testEquipment2.getAtkBonus(),
        result.getAtk());
    assertEquals(
        testGladiator.getDef() + testEquipment1.getDefBonus() + testEquipment2.getDefBonus(),
        result.getDef());
    assertEquals(
        testGladiator.getHp() + testEquipment1.getHpBonus() + testEquipment2.getHpBonus(),
        result.getHp());
    assertEquals(
        testGladiator.getLck() + testEquipment1.getLckBonus() + testEquipment2.getLckBonus(),
        result.getLck());
    assertEquals(
        testGladiator.getDmg() + testEquipment1.getDmgBonus() + testEquipment2.getDmgBonus(),
        result.getDmg());
  }

  @Test
  public void equipGladiator_PersonaDoesNotExist_ExceptionThrown() {
    // Setup
    Long invalidId = -1L;
    when(personaRepository.findById(invalidId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          gladiatorService.equipGladiator(invalidId);
        });
  }

  @Test
  public void combatServiceTest_ArenaPrize() {
    Persona winner = createTestRaider();
    winner.setId(2L);
    winner.setPullRing(100);
    Persona loser = createTestRaider();
    loser.setPullRing(50);
    Pair<Combatant, Combatant> combatants = Pair.of(winner, loser);

    // when(activityService.getReward(any())).thenReturn(java.util.Optional.of(winner));
    when(personaRepo.findById(2L)).thenReturn(java.util.Optional.of(winner));
    when(personaRepo.findById(1L)).thenReturn(java.util.Optional.of(loser));

    combatService.arenaPrize(combatants);

    assertEquals(125, winner.getPullRing());
    assertEquals(25, loser.getPullRing());

    verify(personaRepo, times(1)).save(winner);
    verify(personaRepo, times(1)).save(loser);
  }

  private Persona createTestRaider() {
    Persona persona = new Persona("JoeMama", Faction.RAIDER, 50, 20, 10, 10, 100, 10);
    persona.setId(1L);
    return persona;
  }

  @Test
  public void combatServiceTest_fightOutcome_isFair() {
    PersonaDTO attacker = new PersonaDTO();
    CombatantDTO defender = new CombatantDTO();
    attacker.setHp(100);
    attacker.setAtk(50);
    attacker.setDef(30);
    attacker.setDmg(20);
    attacker.setLck(60);
    defender.setHp(100);
    defender.setAtk(50);
    defender.setDef(30);
    defender.setDmg(20);
    defender.setLck(60);

    int totalRounds = 1000;
    int attackerWins = 0;

    for (int i = 0; i < totalRounds; i++) {
      attacker.setHp(50);
      defender.setHp(50);

      Pair<CombatantDTO, CombatantDTO> outcome = combatService.fightOutcome(attacker, defender);
      if (outcome.getFirst() == attacker) {

        attackerWins++;
      }
    }
    double percentWon = ((double) attackerWins / totalRounds) * 10;
    assertEquals(5, (int) percentWon);
  }
}
