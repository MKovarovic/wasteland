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

  public Persona[] arenaFight(Long id) {
    String faction;
    Persona attacker =
        playerCharacters
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No such persona"));
    if (attacker.getFaction().equals("Raider")) {
      faction = "Settler";
    } else {
      faction = "Raider";
    }
    Persona defender =
        playerCharacters
            .findById(
                playerCharacters
                    .findRandomIdByFaction(faction)
                    .orElseThrow(() -> new IllegalArgumentException("Nobody on the other team")))
            .orElseThrow(() -> new IllegalArgumentException("No such persona"));

    logActivity(ActivityType.PVE, attacker.getId());
    Persona[] result = new Persona[2];
    result[0] = attacker;
    result[1] = defender;
    return result;
  }

  public void decideFightResult(Persona[] combatants) {
    Persona attacker = combatants[0];
    Persona defender = combatants[1];
    Random rnd = new Random();
    while (attacker.getHp() > 0 && defender.getHp() > 0) {
      int attack = rnd.nextInt((int) attacker.getAtk());

      if (attack >= defender.getDef()) {
        if (rnd.nextInt(100) < attacker.getLck()) {
          defender.setHp(defender.getHp() - (attacker.getDmg() * 2));
        }
        defender.setHp(defender.getHp() - attacker.getDmg());
      }
      if (defender.getHp() <= 0 || attacker.getHp() <= 0) {
        break;
      }
      int defense = rnd.nextInt((int) defender.getAtk());
      if (defense >= attacker.getDef()) {
        if (rnd.nextInt(100) < attacker.getLck()) {
          attacker.setHp(attacker.getHp() - (defender.getDmg() * 2));
        }
        attacker.setHp(attacker.getHp() - defender.getDmg());
      }
    }


    Persona winner;
    Persona loser;

    if (attacker.getHp() <= 0) {
      winner = defender;
      loser = attacker;
    } else {
      winner = attacker;
      loser = defender;
    }

    Persona winnerPersona =
        playerCharacters
            .findById(winner.getId())
            .orElseThrow(() -> new IllegalArgumentException("No such persona"));
    Persona loserPersona =
        playerCharacters
            .findById(loser.getId())
            .orElseThrow(() -> new IllegalArgumentException("No such persona"));

    getReward(winnerPersona.getId());
    winnerPersona.setPullRing(winnerPersona.getPullRing() + (loserPersona.getPullRing() / 2));
    loserPersona.setPullRing(loserPersona.getPullRing() / 2);
    playerCharacters.save(winnerPersona);
    playerCharacters.save(loserPersona);
  }
}
