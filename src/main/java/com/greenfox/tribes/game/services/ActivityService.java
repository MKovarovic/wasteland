package com.greenfox.tribes.game.services;

import com.greenfox.tribes.game.enums.ActivityType;
import com.greenfox.tribes.game.models.ActivityLog;
import com.greenfox.tribes.game.repositories.ActivityLogRepo;
import com.greenfox.tribes.gameitems.models.Equipment;
import com.greenfox.tribes.gameitems.repositories.EquipmentRepo;
import com.greenfox.tribes.misc.models.CharacterEquipment;
import com.greenfox.tribes.misc.repositories.CharacterEquipmentRepo;
import com.greenfox.tribes.persona.models.Persona;
import com.greenfox.tribes.persona.repositories.PersonaRepo;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@AllArgsConstructor
public class ActivityService {
  private ActivityLogRepo activityLogRepo;
  private PersonaRepo playerCharacters;
  private EquipmentRepo equipmentRepo;
  @Autowired private CharacterEquipmentRepo pairingRepo;

  public void logActivity(ActivityType type, Long personaId) {
    ActivityLog activity = new ActivityLog();
    activity.setType(type);
    activity.setTimestamp(System.currentTimeMillis());
    switch (type) {
      case WORK:
        activity.setTime(20);
        activity.setPullRings(10);
        activity.setGivesItem(false);
        break;
      case PVP:
        activity.setTime(5);
        activity.setPullRings(100);
        activity.setGivesItem(false);
        break;
      case PVE:
        activity.setTime(10);
        activity.setPullRings(200);
        activity.setGivesItem(true);
        break;
      default:
        break;
    }
    Persona persona =
        playerCharacters
            .findById(personaId)
            .orElseThrow(() -> new IllegalArgumentException("No such persona"));
    persona.setIsBusy(true);
    activity.setPersona(persona);
    activityLogRepo.save(activity);
  }

  public boolean isFinished(Long id) {
    Optional<ActivityLog> activity = activityLogRepo.findActivityLogByPersonaId(id);
    if (activity.isEmpty()) {
      return false;
    }
    if (System.currentTimeMillis()
        >= activity.get().getTimestamp() + (activity.get().getTime() * 60 * 1000)) {
      getReward(id);
      Persona persona = activity.get().getPersona();
      persona.setIsBusy(false);
      activityLogRepo.delete(activity.get());
      return true;
    }
    return false;
  }

  public void getReward(Long id) {
    Persona persona = playerCharacters.findById(id).get();
    persona.setPullRing(
        persona.getPullRing()
            + activityLogRepo.findActivityLogByPersonaId(id).get().getPullRings());
    if (activityLogRepo.findActivityLogByPersonaId(id).get().getGivesItem()) {
      Random rnd = new Random();
      int item = rnd.nextInt((int) equipmentRepo.count());
      Equipment itemToGive = equipmentRepo.findAll().get(item);
      CharacterEquipment pair = new CharacterEquipment();
      pair.setPair(persona, itemToGive);
      pairingRepo.save(pair);
    }
  }
}
