package com.greenfox.tribes.services;

import com.greenfox.tribes.dtos.ActivityDTO;
import com.greenfox.tribes.dtos.CombatantDTO;
import com.greenfox.tribes.dtos.PersonaDTO;
import com.greenfox.tribes.enums.ActivityType;
import com.greenfox.tribes.enums.Faction;
import com.greenfox.tribes.models.*;
import com.greenfox.tribes.repositories.ActivityLogRepository;
import com.greenfox.tribes.repositories.EquipmentRepository;
import com.greenfox.tribes.repositories.UserRepository;
import com.greenfox.tribes.repositories.CharacterEquipmentRepository;
import com.greenfox.tribes.repositories.MonsterRepository;
import com.greenfox.tribes.repositories.PersonaRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@AllArgsConstructor
public class ActivityService {
  private ActivityLogRepository activityLogRepository;
  private UserRepository userRepository;
  private CustomUserDetailService userService;
  private CharacterService characterService;
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

  public void logActivity(ActivityType type, Integer time, Integer pullRings,
                          Boolean givesItem) {
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
    Persona initiator = userService.getLoggedUser().getPersona();
    Combatant persona = playerCharacters.findById(id).get();

    persona.setPullRing(
        persona.getPullRing()
            + activityLogRepository
            .findActivityLogByPersonaId(initiator.getId())
            .get()
            .getPullRings());
    if (activityLogRepository.findActivityLogByPersonaId(initiator.getId()).get().getGivesItem()) {
      Random rnd = new Random();
      int item = rnd.nextInt((int) equipmentRepository.count());
      Equipment itemToGive = equipmentRepository.findAll().get(item);
      CharacterEquipment pair = new CharacterEquipment();
      pair.setPair(initiator, itemToGive);
      pairingRepo.save(pair);
    }
  }

  // TARGET SELECTION

  public void pvpMatching(Long id) {

    Persona attacker =
        playerCharacters
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No such persona"));
    Faction faction;
    if (attacker.getFaction() == Faction.RAIDER) {
      faction = Faction.SETTLER;
    } else {
      faction = Faction.RAIDER;
    }

    Persona defender = randomEnemy(faction);
    logPVPActivity(defender.getId());
    // -----------

  }

  public Persona randomEnemy(Faction faction) {
    Persona defender =
        playerCharacters
            .findById(
                playerCharacters
                    .findRandomIdByFaction(faction)
                    .orElseThrow(() -> new IllegalArgumentException("Nobody on the other team")))
            .orElseThrow(() -> new IllegalArgumentException("No such persona"));
    return defender;
  }

  public void pveMatching(Long id) {
    Persona attacker =
        playerCharacters
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No such persona"));

    Monster defender = randomMonster();
    logPVEActivity(defender.getId());
  }

  public Monster randomMonster() {
    Monster defender =
        monsterRepository
            .findById(
                monsterRepository
                    .findRandomMonsterId()
                    .orElseThrow(() -> new IllegalArgumentException("No such Monster")))
            .orElseThrow(() -> new IllegalArgumentException("No such Monster"));
    return defender;
  }

  // COMBAT RESOLUTION
  // todo try to make this method shorter if possible
  public Pair<Combatant, Combatant> fightStart(Long id) {
    PersonaDTO attacker = equipGladiator(id);
    CombatantDTO defender = getDefender(id);

    Pair<CombatantDTO, CombatantDTO> combatants = fightOutcome(attacker, defender);

    Combatant attackerCombatant = getCombatant(id, "Persona");
    Combatant defenderCombatant;
    if(activityLogRepository.findActivityLogByPersonaId(id).get().getType() == ActivityType.PVE) {
      defenderCombatant = getCombatant(activityLogRepository.findActivityLogByPersonaId(id).get().getEnemyID(), "Monster");
    } else {
      defenderCombatant = getCombatant(activityLogRepository.findActivityLogByPersonaId(id).get().getEnemyID(), "Persona");
    }
    if (Objects.equals(combatants.getFirst(), id)) {
        return Pair.of(attackerCombatant, defenderCombatant);
    } else {
      return Pair.of(defenderCombatant, attackerCombatant);
    }
  }

  private CombatantDTO getDefender(Long id) {
    ActivityLog activityLog = activityLogRepository.findActivityLogByPersonaId(id).get();
    if (activityLog.getType() == ActivityType.PVP) {
      return equipGladiator(activityLog.getEnemyID());
    } else if (activityLog.getType() == ActivityType.PVE) {
      return monsterService.findMonster(activityLog.getEnemyID());
    }
    return null;
  }
  private Combatant getCombatant(Long id, String combatantType) {
    if (combatantType == "Persona") {
      return playerCharacters.findById(id).orElseThrow(() -> new IllegalArgumentException("No such persona"));
    } else if (combatantType == "Monster") {
      return monsterRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No such monster"));
    }
    return null;
  }



  public PersonaDTO equipGladiator(Long id) {
    Persona gladiator =
        playerCharacters
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No such persona"));

    PersonaDTO gladiatorDTO = characterService.readCharacter();
    if (gladiatorDTO.getEquipedItems() != null) {

      List<Equipment> equippedItems =
          characterService.readCharacter(gladiator.getId()).getEquipedItems();
      for (Equipment e : equippedItems) {
        gladiatorDTO.setAtk(gladiator.getAtk() + e.getAtkBonus());
        gladiatorDTO.setDef(gladiator.getDef() + e.getDefBonus());
        gladiatorDTO.setHp(gladiator.getHp() + e.getHpBonus());
        gladiatorDTO.setLck(gladiator.getLck() + e.getLckBonus());
        gladiatorDTO.setDmg(gladiator.getDmg() + e.getDmgBonus());
      }
    }
    return gladiatorDTO;
  }


  public Pair<CombatantDTO, CombatantDTO> fightOutcome(PersonaDTO attacker, CombatantDTO defender) {
    int doom = 0;
    int initialHPAttacker = attacker.getHp();
    int initialHPDefender = defender.getHp();
    Random rnd = new Random();
    while (attacker.getHp() > 0 && defender.getHp() > 0) {
      int attack = rnd.nextInt((int) attacker.getAtk());
      doom++;
      if (attack >= defender.getDef() - doom) {
        if (rnd.nextInt(100) < attacker.getLck()) {
          defender.setHp(defender.getHp() - (attacker.getDmg() * 2));
        }
        defender.setHp(defender.getHp() - attacker.getDmg());
      }
      if (defender.getHp() <= 0 || attacker.getHp() <= 0) {
        break;
      }
      int defense = rnd.nextInt((int) defender.getAtk());
      if (defense >= attacker.getDef() - doom) {
        if (rnd.nextInt(100) < attacker.getLck()) {
          attacker.setHp(attacker.getHp() - (defender.getDmg() * 2));
        }
        attacker.setHp(attacker.getHp() - defender.getDmg());
      }
    }

    CombatantDTO winner;
    CombatantDTO loser;





    if (attacker.getHp() <= 0) {
      return Pair.of(defender, attacker);

    } else {

      return Pair.of(attacker, defender);

    }

  }

  // REWARD - STEAL OR HAVE STOLEN

  public void arenaPrize(Pair<Combatant, Combatant> combatants) {
    Persona winnerPersona =
        playerCharacters
            .findById(combatants.getFirst().getId())
            .orElseThrow(() -> new IllegalArgumentException("No such persona"));
    Persona loserPersona =
        playerCharacters
            .findById(combatants.getSecond().getId())
            .orElseThrow(() -> new IllegalArgumentException("No such persona"));

    getReward(winnerPersona.getId());

    winnerPersona.setPullRing(winnerPersona.getPullRing() + (loserPersona.getPullRing() / 2));
    loserPersona.setPullRing(loserPersona.getPullRing() / 2);
    playerCharacters.save(winnerPersona);
    playerCharacters.save(loserPersona);
  }

  public void huntPrize(Pair<Combatant, Combatant> combatants) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    Persona loggedCharacter = user.getPersona();
    if (combatants.getFirst() == loggedCharacter) {
      loggedCharacter.setPullRing(
          loggedCharacter.getPullRing() + (combatants.getSecond().getPullRing() / 2));
      getReward(loggedCharacter.getId());
    } else {
      loggedCharacter.setPullRing(
          loggedCharacter.getPullRing() - (loggedCharacter.getPullRing() / 2));
    }
    playerCharacters.save(loggedCharacter);
  }
}
