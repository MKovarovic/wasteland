package com.greenfox.tribes.services;

import com.greenfox.tribes.dtos.ActivityDTO;
import com.greenfox.tribes.enums.ActivityType;
import com.greenfox.tribes.models.ActivityLog;
import com.greenfox.tribes.repositories.ActivityLogRepository;
import com.greenfox.tribes.models.Equipment;
import com.greenfox.tribes.repositories.EquipmentRepository;
import com.greenfox.tribes.models.WastelandUser;
import com.greenfox.tribes.repositories.UserRepository;
import com.greenfox.tribes.models.CharacterEquipment;
import com.greenfox.tribes.models.Monster;
import com.greenfox.tribes.repositories.CharacterEquipmentRepository;
import com.greenfox.tribes.repositories.MonsterRepository;
import com.greenfox.tribes.models.Persona;
import com.greenfox.tribes.repositories.PersonaRepository;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@AllArgsConstructor
public class ActivityService {
  private ActivityLogRepository activityLogRepository;
  @Autowired UserRepository userRepository;
  @Autowired CharacterService characterService;
  @Autowired private PersonaRepository playerCharacters;
  @Autowired private MonsterRepository monsterRepository;
  @Autowired private EquipmentRepository equipmentRepository;
  @Autowired private CharacterEquipmentRepository pairingRepo;

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
    activityLogRepository.save(activity);
  }

  public ActivityDTO getActivity(Long id) {
    Optional<ActivityLog> activity = activityLogRepository.findActivityLogByPersonaId(id);
    // return activity;
    if (activity.isEmpty()) {
      return null;
    }

    return new ActivityDTO(
        activity.get().getType(),
        activity.get().getTimestamp(),
        activity.get().getTime(),
        activity.get().getPullRings(),
        activity.get().getGivesItem(),
        activity.get().getEnemyID(),
        activity.get().getPersona().getId());
  }

  public boolean isFinished(Long id) {
    Optional<ActivityLog> activity = activityLogRepository.findActivityLogByPersonaId(id);
    if (activity.isEmpty()) {
      return false;
    }
    if (System.currentTimeMillis()
        >= activity.get().getTimestamp() + (activity.get().getTime() * 60 * 1000)) {
      getReward(id);
      Persona persona = activity.get().getPersona();
      persona.setIsBusy(false);
      activityLogRepository.delete(activity.get());
      return true;
    }
    return false;
  }

  public void getReward(Long id) {
    Persona persona = playerCharacters.findById(id).get();
    persona.setPullRing(
        persona.getPullRing()
            + activityLogRepository.findActivityLogByPersonaId(id).get().getPullRings());
    if (activityLogRepository.findActivityLogByPersonaId(id).get().getGivesItem()) {
      Random rnd = new Random();
      int item = rnd.nextInt((int) equipmentRepository.count());
      Equipment itemToGive = equipmentRepository.findAll().get(item);
      CharacterEquipment pair = new CharacterEquipment();
      pair.setPair(persona, itemToGive);
      pairingRepo.save(pair);
    }
  }

  // TARGET SELECTION

  public void pvpMatching(Long id) {
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
    activityLogRepository.findActivityLogByPersonaId(attacker.getId()).get().setEnemyID(defender.getId());
    activityLogRepository.save(activityLogRepository.findActivityLogByPersonaId(attacker.getId()).get());
  }

  public void pveMatching(Long id) {
    Persona attacker =
        playerCharacters
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No such persona"));
    Monster defender =
        monsterRepository
            .findById(
                monsterRepository
                    .findRandomMonsterId()
                    .orElseThrow(() -> new IllegalArgumentException("No such Monster")))
            .orElseThrow(() -> new IllegalArgumentException("No such Monster"));
    logActivity(ActivityType.PVE, attacker.getId());
    activityLogRepository.findActivityLogByPersonaId(attacker.getId()).get().setEnemyID(defender.getId());
    activityLogRepository.save(activityLogRepository.findActivityLogByPersonaId(attacker.getId()).get());
  }

  // COMBAT RESOLUTION

  public Persona[] fightOutcome(Long id) {
    Persona attacker =
        playerCharacters
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No such persona"));
    if (characterService.readCharacter(attacker.getId()).getEquipedItems() != null) {

      List<Equipment> equippedItems =
          characterService.readCharacter(attacker.getId()).getEquipedItems();
      for (Equipment e : equippedItems) {
        attacker.setAtk(attacker.getAtk() + e.getAtkBonus());
        attacker.setDef(attacker.getDef() + e.getDefBonus());
        attacker.setHp(attacker.getHp() + e.getHpBonus());
        attacker.setLck(attacker.getLck() + e.getLckBonus());
        attacker.setDmg(attacker.getDmg() + e.getDmgBonus());
      }
    }
    Persona defender =
        playerCharacters
            .findById(
                activityLogRepository.findActivityLogByPersonaId(attacker.getId()).get().getEnemyID())
            .orElseThrow(() -> new IllegalArgumentException("No such persona"));
    if (characterService.readCharacter(defender.getId()).getEquipedItems() != null) {
      List<Equipment> equippedItems2 =
          characterService.readCharacter(defender.getId()).getEquipedItems();
      for (Equipment e : equippedItems2) {
        defender.setAtk(defender.getAtk() + e.getAtkBonus());
        defender.setDef(defender.getDef() + e.getDefBonus());
        defender.setHp(defender.getHp() + e.getHpBonus());
        defender.setLck(defender.getLck() + e.getLckBonus());
        defender.setDmg(defender.getDmg() + e.getDmgBonus());
      }
    }
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

    Persona[] result = new Persona[2];
    result[0] = winner;
    result[1] = loser;
    return result;
  }

  // REWARD - STEAL OR HAVE STOLEN

  public void arenaPrize(Persona[] combatants) {
    Persona winnerPersona =
        playerCharacters
            .findById(combatants[0].getId())
            .orElseThrow(() -> new IllegalArgumentException("No such persona"));
    Persona loserPersona =
        playerCharacters
            .findById(combatants[1].getId())
            .orElseThrow(() -> new IllegalArgumentException("No such persona"));

    getReward(winnerPersona.getId());
    winnerPersona.setPullRing(winnerPersona.getPullRing() + (loserPersona.getPullRing() / 2));
    loserPersona.setPullRing(loserPersona.getPullRing() / 2);
    playerCharacters.save(winnerPersona);
    playerCharacters.save(loserPersona);
  }

  public void huntPrize(Persona[] combatants) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    Persona loggedCharacter = user.getPersona();
    if (combatants[0] == loggedCharacter) {
      loggedCharacter.setPullRing(
          loggedCharacter.getPullRing() + (combatants[1].getPullRing() / 2));
      getReward(loggedCharacter.getId());
    } else {
      loggedCharacter.setPullRing(
          loggedCharacter.getPullRing() - (loggedCharacter.getPullRing() / 2));
    }
    playerCharacters.save(loggedCharacter);
  }
}
