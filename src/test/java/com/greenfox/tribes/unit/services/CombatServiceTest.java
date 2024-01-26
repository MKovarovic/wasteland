package com.greenfox.tribes.unit.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.greenfox.tribes.BaseTest;
import com.greenfox.tribes.dtos.PersonaDTO;
import com.greenfox.tribes.enums.Faction;
import com.greenfox.tribes.mappers.PersonaMapping;
import com.greenfox.tribes.models.Equipment;
import com.greenfox.tribes.models.Persona;
import com.greenfox.tribes.repositories.PersonaRepository;
import com.greenfox.tribes.services.CombatService;
import com.greenfox.tribes.services.PersonaService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class CombatServiceTest extends BaseTest {


  @Autowired
  private CombatService gladiatorService;

  @MockBean
  private PersonaRepository personaRepository;

  @MockBean
  private PersonaService characterService;

  private Persona testGladiator;
  private PersonaDTO testGladiatorDTO;
  private Equipment testEquipment1;
  private Equipment testEquipment2;
  private Equipment testEquipment3;

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
    testEquipment1 = getEquipment(30, "Weapon");
    testEquipment2 = getEquipment(50, "Tool");
    testEquipment3 = getEquipment(70, "Amulet");
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
    assertEquals(testGladiator.getHp() + testEquipment1.getHpBonus() + testEquipment2.getHpBonus(),
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
    assertThrows(IllegalArgumentException.class, () -> {
      gladiatorService.equipGladiator(invalidId);
    });
  }
}
