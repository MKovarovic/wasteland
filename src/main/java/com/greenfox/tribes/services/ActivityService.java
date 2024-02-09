package com.greenfox.tribes.services;

import com.greenfox.tribes.dtos.ActivityDTO;
import com.greenfox.tribes.enums.ActivityType;
import com.greenfox.tribes.models.*;
import com.greenfox.tribes.repositories.ActivityLogRepository;
import com.greenfox.tribes.repositories.EquipmentRepository;
import com.greenfox.tribes.repositories.UserRepository;
import com.greenfox.tribes.repositories.CharacterEquipmentRepository;
import com.greenfox.tribes.repositories.MonsterRepository;
import com.greenfox.tribes.repositories.PersonaRepository;

import java.util.Optional;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@AllArgsConstructor
public class ActivityService {
  private ActivityLogRepository activityLogRepository;
  private UserRepository userRepository;
  private CustomUserDetailService userService;
  private PersonaService characterService;
  private PersonaRepository playerCharacters;
  private MonsterRepository monsterRepository;
  private EquipmentRepository equipmentRepository;
  private CharacterEquipmentRepository pairingRepo;
  private MonsterService monsterService;

  public void logWorkActivity() {
    logActivity(ActivityType.WORK, 20, 10, false);
  }

  public void logPVPActivity(Long defenderId) {
    logActivity(ActivityType.PVP, 5, 100, false);
    Long attackerId = characterService.getLoggedInPersona().getId();
    activityLogRepository.findActivityLogByPersonaId(attackerId).get().setEnemyID(defenderId);
    activityLogRepository.save(activityLogRepository.findActivityLogByPersonaId(attackerId).get());
  }

  public void logPVEActivity(Long defenderId) {
    logActivity(ActivityType.PVE, 10, 100, false);
    Long attackerId = characterService.getLoggedInPersona().getId();
    ActivityLog activityLog = activityLogRepository.findActivityLogByPersonaId(attackerId).get();
    activityLog.setEnemyID(defenderId);
    activityLogRepository.save(activityLog);
  }

  public void logActivity(ActivityType type, Integer time, Integer pullRings, Boolean givesItem) {
    ActivityLog activity = new ActivityLog();
    activity.setType(type);
    activity.setTimestamp(System.currentTimeMillis());
    activity.setTime(time);
    activity.setPullRings(pullRings);
    activity.setGivesItem(givesItem);
    Persona persona = characterService.getLoggedInPersona();
    activity.setPersona(persona);
    activityLogRepository.save(activity);
  }

  public void deleteActivity(Long id) {
    activityLogRepository.delete(activityLogRepository.findActivityLogByPersonaId(id).get());
  }

  public ActivityDTO getActivity(Long id) {
    Optional<ActivityLog> activity = activityLogRepository.findActivityLogByPersonaId(id);
    // return activity;
    if (activity.isEmpty()) {
      return null;
    }
    return makeDTO(activity.get().getId());
  }

  public ActivityDTO makeDTO(Long id) {
    Optional<ActivityLog> activity = activityLogRepository.findById(id);
    return new ActivityDTO(
        activity.get().getType(),
        activity.get().getTimestamp(),
        activity.get().getTime(),
        activity.get().getPullRings(),
        activity.get().getGivesItem(),
        activity.get().getEnemyID(),
        activity.get().getPersona().getId());
  }

  public Long timeRemaining(Long id) {
    Optional<ActivityLog> activity = activityLogRepository.findActivityLogByPersonaId(id);
    if (activity.isEmpty()) {
      return null;
    }
    return (activity.get().getTimestamp()
            + (activity.get().getTime() * 60 * 1000)
            - System.currentTimeMillis())
        / 60000;
  }

  public boolean activityInProgress(Long id) {
    Optional<ActivityLog> activity = activityLogRepository.findActivityLogByPersonaId(id);
    if (activity.isEmpty()) {
      return false;
    }
    return !isExpired(activity.get());
  }

  public boolean isFinished(Long id) {
    Optional<ActivityLog> activity = activityLogRepository.findActivityLogByPersonaId(id);
    if (activity.isEmpty()) {
      return false;
    }
    return isExpired(activity.get());
  }

  private boolean isExpired(ActivityLog activityLog) {
    return System.currentTimeMillis()
        >= activityLog.getTimestamp() + ((long) activityLog.getTime() * 60 * 1000);
  }

  public void getReward(Long id) {

    Persona persona = playerCharacters.findById(id).get();

    persona.setPullRing(
        persona.getPullRing()
            + activityLogRepository
                .findActivityLogByPersonaId(persona.getId())
                .get()
                .getPullRings());
    playerCharacters.save(persona);
    if (activityLogRepository.findActivityLogByPersonaId(persona.getId()).get().getGivesItem()) {
      Random rnd = new Random();
      int item = rnd.nextInt((int) equipmentRepository.count());
      Equipment itemToGive = equipmentRepository.findAll().get(item);
      CharacterEquipment pair = new CharacterEquipment();
      pair.setPair(persona, itemToGive);
      pairingRepo.save(pair);
    }
  }
}
