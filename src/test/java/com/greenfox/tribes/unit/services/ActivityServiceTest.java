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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ActivityServiceTest extends BaseTest {

  @Mock
  private PersonaService personaService;
  @Mock private ActivityLogRepository activityRepository;
  @InjectMocks
  private ActivityService activityService;

  @Test
  public void ActivityService_logWork_logNotEmpty() {
    Persona persona = createTestPersona();


    when(personaService.getLoggedInPersona()).thenReturn(persona);
    activityService.logWorkActivity();

    ArgumentCaptor<ActivityLog> argument = ArgumentCaptor.forClass(ActivityLog.class);
;


    verify(activityRepository).save(argument.capture());
    assertEquals(persona, argument.getValue().getPersona());
    assertEquals(ActivityType.WORK, argument.getValue().getType());

  }

  private Persona createTestPersona() {
    Persona persona = new Persona("JoeMama", Faction.RAIDER, 50, 20, 10, 10, 100, 1);
    persona.setId(1L);
    return persona;
  }
}
