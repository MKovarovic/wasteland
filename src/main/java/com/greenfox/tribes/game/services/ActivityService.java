/*
package com.greenfox.tribes.game.services;

import com.greenfox.tribes.game.models.Activity;
import com.greenfox.tribes.game.models.ActivityLog;
import com.greenfox.tribes.game.repositories.ActivityLogRepo;
import com.greenfox.tribes.game.repositories.ActivityRepo;
import com.greenfox.tribes.misc.models.CharacterEquipment;
import com.greenfox.tribes.persona.models.Persona;
import com.greenfox.tribes.persona.repositories.PersonaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {
  @Autowired private ActivityLogRepo activityLogRepo;
  @Autowired private ActivityRepo activityRepo;
  @Autowired private PersonaRepo playerCharacters;

  public void setActivity(String username, String activity) {
    ActivityLog activityLog = new ActivityLog();
    Persona character = playerCharacters.findPersonaByPlayer_Username(username).get();
    character.setIsBusy(true);
    activityLog.setCharacterId(character.getId());
    activityLog.setActivity(activityRepo.findActivityByName(activity).get());
    activityLog.setStartTime(System.currentTimeMillis());
    activityLogRepo.save(activityLog);
  }

  public void endActivity(String username) {
    Persona character = playerCharacters.findPersonaByPlayer_Username(username).get();
    ActivityLog activityLog = activityLogRepo.findActivityLogByCharacterId(character.getId()).get();
    Activity currActivity = activityLog.getActivity();
    if (System.currentTimeMillis()
        >= activityLog.getStartTime() + currActivity.getTime() * 60000L) {
      character.setIsBusy(false);
      character.setPullRing(character.getPullRing() + currActivity.getPullRings());
      character.getInventory().add((CharacterEquipment) currActivity.getRewardItem());
      playerCharacters.save(character);
    }
  }
}
*/
