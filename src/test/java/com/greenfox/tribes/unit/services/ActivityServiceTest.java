package com.greenfox.tribes.unit.services;

import com.greenfox.tribes.BaseTest;
import com.greenfox.tribes.dtos.ActivityDTO;
import com.greenfox.tribes.enums.ActivityType;
import com.greenfox.tribes.enums.Faction;
import com.greenfox.tribes.models.ActivityLog;
import com.greenfox.tribes.models.Equipment;
import com.greenfox.tribes.models.Persona;
import com.greenfox.tribes.repositories.ActivityLogRepository;
import com.greenfox.tribes.repositories.CharacterEquipmentRepository;
import com.greenfox.tribes.repositories.EquipmentRepository;
import com.greenfox.tribes.repositories.PersonaRepository;
import com.greenfox.tribes.services.ActivityService;
import com.greenfox.tribes.services.EquipmentService;
import com.greenfox.tribes.services.PersonaService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActivityServiceTest extends BaseTest {

  @Mock private PersonaService personaService;
  @Mock private PersonaRepository personaRepository;
  @Mock private ActivityLogRepository activityRepository;

  @Mock private EquipmentRepository equipmentRepository;
  @Mock private CharacterEquipmentRepository pairingRepo;
  @InjectMocks private ActivityService activityService;
  @InjectMocks private EquipmentService equipmentService;

  @Test
  public void activityService_logWork_logNotEmpty() {
    Persona persona = createTestRaider();

    when(personaService.getLoggedInPersona()).thenReturn(persona);
    activityService.logWorkActivity();

    ArgumentCaptor<ActivityLog> argument = ArgumentCaptor.forClass(ActivityLog.class);
    ;

    verify(activityRepository).save(argument.capture());
    assertEquals(persona, argument.getValue().getPersona());
    assertEquals(ActivityType.WORK, argument.getValue().getType());
  }

  @Test
  public void activityService_logPvP_logNotEmpty() {
    Persona persona = createTestRaider();
    ActivityLog activityLog = getActivity(ActivityType.PVP);

    when(personaService.getLoggedInPersona()).thenReturn(persona);
    when(activityRepository.findActivityLogByPersonaId(1L)).thenReturn(Optional.of(activityLog));
    activityService.logPVPActivity(1L);

    ArgumentCaptor<ActivityLog> argument = ArgumentCaptor.forClass(ActivityLog.class);
    verify(activityRepository, times(2)).save(argument.capture());
    assertEquals(persona, argument.getValue().getPersona());
    assertEquals(ActivityType.PVP, argument.getValue().getType());
  }

  @Test
  public void activityService_logPvE_logNotEmpty() {
    Persona persona = createTestRaider();
    ActivityLog activityLog = getActivity(ActivityType.PVE);

    when(personaService.getLoggedInPersona()).thenReturn(persona);
    when(activityRepository.findActivityLogByPersonaId(1L)).thenReturn(Optional.of(activityLog));
    activityService.logPVEActivity(1L);

    ArgumentCaptor<ActivityLog> argument = ArgumentCaptor.forClass(ActivityLog.class);
    verify(activityRepository, times(2)).save(argument.capture());
    assertEquals(persona, argument.getValue().getPersona());
    assertEquals(ActivityType.PVE, argument.getValue().getType());
  }

  @Test
  public void activityService_makeDTO_logNotEmpty() {
    ActivityLog activityLog = getActivity(ActivityType.PVP);
    when(activityRepository.findById(any())).thenReturn(Optional.of(activityLog));
    ActivityDTO dto = activityService.makeDTO(1L);
    assertEquals(ActivityType.PVP, dto.getType());
  }

  @Test
  public void activityService_timeRemaining_works() {

    when(activityRepository.findActivityLogByPersonaId(any()))
        .thenReturn(Optional.of(getActivity(ActivityType.WORK)));
    assertEquals(4, activityService.timeRemaining(1L));
  }


  @Test
  public void activityService_getReward() {
    Equipment equipment = new Equipment();
    equipment.setId(1L);
    equipment.setName("Rusty Sword");
    equipment.setType("Weapon");
    equipment.setDefBonus(0);
    equipment.setHpBonus(0);
    equipment.setDmgBonus(0);
    equipment.setAtkBonus(5);
    equipment.setLckBonus(0);
    equipment.setPrice(10);
    Persona persona = createTestRaider();
    when(personaRepository.findById(any())).thenReturn(Optional.of(persona));
    when(personaService.getLoggedInPersona()).thenReturn(createTestSettler());
    when(activityRepository.findActivityLogByPersonaId(any()))
        .thenReturn(Optional.of(getActivity(ActivityType.PVP)));
    when(equipmentRepository.count()).thenReturn(19L);
    when(equipmentRepository.findById(any())).thenReturn(Optional.of(equipment));

    activityService.getReward(persona.getId());
    verify(personaRepository, times(1)).save(any());
  }

  private Persona createTestRaider() {
    Persona persona = new Persona("JoeMama", Faction.RAIDER, 50, 20, 10, 10, 100, 1);
    persona.setId(1L);
    return persona;
  }

  private Persona createTestSettler() {
    Persona persona = new Persona("JoeMama", Faction.SETTLER, 50, 20, 10, 10, 100, 1);
    persona.setId(1L);
    return persona;
  }

  private ActivityLog getActivity(ActivityType type) {
    ActivityLog activityLog = new ActivityLog();
    Persona persona = createTestRaider();
    activityLog.setPersona(persona);
    activityLog.setType(type);
    activityLog.setId(1L);
    activityLog.setGivesItem(type == ActivityType.PVE);
    activityLog.setPullRings(100);
    activityLog.setTime(5);
    activityLog.setTimestamp(System.currentTimeMillis());
    return activityLog;
  }
}
