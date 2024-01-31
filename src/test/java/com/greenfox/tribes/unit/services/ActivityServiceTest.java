package com.greenfox.tribes.unit.services;

import com.greenfox.tribes.BaseTest;
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
    ActivityLog activityLog = new ActivityLog();
    activityLog.setPersona(persona);
    activityLog.setType(ActivityType.PVP);
    activityLog.setId(1L);
    activityLog.setGivesItem(false);
    activityLog.setPullRings(100);
    activityLog.setTime(5);
    activityLog.setTimestamp(System.currentTimeMillis());

    when(personaService.getLoggedInPersona()).thenReturn(persona);
    when(activityRepository.findActivityLogByPersonaId(1L)).thenReturn(Optional.of(activityLog));
    activityService.logPVPActivity(1L);

    ArgumentCaptor<ActivityLog> argument = ArgumentCaptor.forClass(ActivityLog.class);
    verify(activityRepository, times(2)).save(argument.capture());
    assertEquals(persona, argument.getValue().getPersona());
    assertEquals(ActivityType.PVP, argument.getValue().getType());
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
}
