package com.greenfox.tribes.unit.services;

import com.greenfox.tribes.BaseTest;
import com.greenfox.tribes.dtos.ActivityDTO;
import com.greenfox.tribes.enums.ActivityType;
import com.greenfox.tribes.enums.Faction;
import com.greenfox.tribes.models.ActivityLog;
import com.greenfox.tribes.models.Persona;
import com.greenfox.tribes.repositories.ActivityLogRepository;
import com.greenfox.tribes.services.ActivityService;
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
  @Mock private ActivityLogRepository activityRepository;
  @InjectMocks private ActivityService activityService;

  @Test
  public void ActivityService_logWork_logNotEmpty() {
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
  public void ActivityService_logPvP_logNotEmpty() {
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
  public void ActivityService_logPvE_logNotEmpty() {
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
  public void ActivityService_makeDTO_logNotEmpty() {
    ActivityLog activityLog = getActivity(ActivityType.PVP);
    when(activityRepository.findById(any())).thenReturn(Optional.of(activityLog));
    ActivityDTO dto = activityService.makeDTO(1L);
    assertEquals(ActivityType.PVP, dto.getType());
  }

  @Test
  public void ActivityService_timeRemaining_works(){

    when(activityRepository.findActivityLogByPersonaId(any())).thenReturn(Optional.of(getActivity(ActivityType.WORK)));
    assertEquals(4, activityService.timeRemaining(1L));

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
    activityLog.setGivesItem(false);
    activityLog.setPullRings(100);
    activityLog.setTime(5);
    activityLog.setTimestamp(System.currentTimeMillis());
    return activityLog;
  }
}
