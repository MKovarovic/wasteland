package com.greenfox.tribes.services;

import com.greenfox.tribes.dtos.CombatantDTO;
import com.greenfox.tribes.dtos.PersonaDTO;
import com.greenfox.tribes.enums.ActivityType;
import com.greenfox.tribes.enums.Faction;
import com.greenfox.tribes.models.*;
import com.greenfox.tribes.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@AllArgsConstructor
public class CombatService {

  private ActivityLogRepository activityLogRepository;
  private UserRepository userRepository;
  private CustomUserDetailService userService;
  private CharacterService characterService;
  private PersonaRepository playerCharacters;
  private MonsterRepository monsterRepository;
  private EquipmentRepository equipmentRepository;
  private CharacterEquipmentRepository pairingRepo;
  private MonsterService monsterService;
  private ActivityService activityService;

  // COMBAT RESOLUTION

  public Pair<Combatant, Combatant> fightStart(Long id) {
    PersonaDTO attacker = equipGladiator(id);
    CombatantDTO defender = getDefender(id);

    Pair<CombatantDTO, CombatantDTO> combatants = fightOutcome(attacker, defender);

    Combatant attackerCombatant = getCombatant(id, "Persona");
    Combatant defenderCombatant;
    if (activityLogRepository.findActivityLogByPersonaId(id).get().getType() == ActivityType.PVE) {
      defenderCombatant =
          getCombatant(
              activityLogRepository.findActivityLogByPersonaId(id).get().getEnemyID(), "Monster");
    } else {
      defenderCombatant =
          getCombatant(
              activityLogRepository.findActivityLogByPersonaId(id).get().getEnemyID(), "Persona");
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
      return playerCharacters
          .findById(id)
          .orElseThrow(() -> new IllegalArgumentException("No such persona"));
    } else if (combatantType == "Monster") {
      return monsterRepository
          .findById(id)
          .orElseThrow(() -> new IllegalArgumentException("No such monster"));
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

    int maxRounds = 100; // Maximum number of rounds to prevent stalemates
    Random rnd = new Random();

    for (int round = 0; round < maxRounds; round++) {
      // Attacker's turn
      attack(attacker, defender, rnd);
      if (defender.getHp() <= 0) {
        return Pair.of(attacker, defender);
      }

      // Defender's turn
      attack(defender, attacker, rnd);
      if (attacker.getHp() <= 0) {
        return Pair.of(defender, attacker);
      }
    }

    // If the maximum rounds are reached, attacker wins
    return Pair.of(attacker, defender);
  }

  private void attack(CombatantDTO attacker, CombatantDTO defender, Random rnd) {
    int attack = rnd.nextInt((int) attacker.getAtk());
    if (attack >= defender.getDef()) {
      int damage = attacker.getDmg();
      if (rnd.nextInt(100) < attacker.getLck()) {
        damage *= 2; // Double damage if luck is in play
      }
      defender.setHp(defender.getHp() - damage);
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

    activityService.getReward(winnerPersona.getId());

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
      activityService.getReward(loggedCharacter.getId());
    } else {
      loggedCharacter.setPullRing(
          loggedCharacter.getPullRing() - (loggedCharacter.getPullRing() / 2));
    }
    playerCharacters.save(loggedCharacter);
  }

  public void pvpMatching(Long id) {

    Persona attacker =
        playerCharacters
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No such persona"));

    Faction faction = attacker.getFaction() == Faction.RAIDER ? Faction.SETTLER : Faction.RAIDER;

    Persona defender = randomEnemy(faction);
    activityService.logPVPActivity(defender.getId());
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
    activityService.logPVEActivity(defender.getId());
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
}
