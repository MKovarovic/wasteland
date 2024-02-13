package com.greenfox.tribes.controllers;

import com.greenfox.tribes.dtos.ActivityDTO;
import com.greenfox.tribes.dtos.PortraitDTO;
import com.greenfox.tribes.enums.ActivityType;
import com.greenfox.tribes.enums.Faction;
import com.greenfox.tribes.mappers.PortraitMapper;
import com.greenfox.tribes.models.Combatant;
import com.greenfox.tribes.models.Monster;
import com.greenfox.tribes.repositories.PersonaRepository;
import com.greenfox.tribes.services.*;
import com.greenfox.tribes.models.WastelandUser;
import com.greenfox.tribes.repositories.UserRepository;
import com.greenfox.tribes.repositories.MonsterRepository;
import com.greenfox.tribes.models.Persona;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@RequestMapping("/activity")
public class ActivityController {

  private UserRepository userRepository;
  private ActivityService activityService;
  private PersonaRepository personaRepository;
  private MonsterRepository monsterRepository;
  private CombatService combatService;
  private PersonaService characterService;
  private PortraitService portraitService;
  private MonsterService monsterService;

  public Model commonData(Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    Persona userHero = user.getPersona();
    model.addAttribute("hero", characterService.readCharacter(userHero.getId()));
    model.addAttribute("faction", userHero.getFaction().toString());
    model.addAttribute("isBusy", activityService.isFinished(userHero.getId()));
    return model;
  }

  @GetMapping("/notHere")
  public String notHere(Model model) {
    model = commonData(model);
    return "game-sites/not-here";
  }

  @GetMapping("/work")
  public String work(Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    model = commonData(model);
    if (activityService.isFinished(user.getPersona().getId())) {
      activityService.deleteActivity(user.getPersona().getId());
    }

    if (activityService.activityInProgress(user.getPersona().getId())
        && activityService.getActivity(user.getPersona().getId()).getType() != ActivityType.WORK) {
      return "redirect:/activity/notHere";
    }
    model.addAttribute("name", user.getPersona().getCharacterName());
    model.addAttribute("faction", user.getPersona().getFaction().toString());
    model.addAttribute("isBusy", activityService.activityInProgress(user.getPersona().getId()));
    model.addAttribute("id", user.getPersona().getId());
    model.addAttribute("minutes", activityService.timeRemaining(user.getPersona().getId()));

    return "game-sites/work";
  }

  @GetMapping("/work/log")
  public String logWork() {
    activityService.logWorkActivity();
    return "redirect:/activity/work";
  }

  @GetMapping("/pvp")
  public String pvp(Model model) {
    model = commonData(model);
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    Persona userHero = user.getPersona();
    ActivityDTO dto = activityService.getActivity(userHero.getId());
    ActivityType type = null;
    if (dto != null && dto.getType() != null) {
      type = dto.getType();
    }

    if (type == null) {
      return "redirect:/activity/pvp/welcome?id=" + userHero.getId();
    }
    if (type != ActivityType.PVP) {
      return "redirect:/activity/notHere";
    }
    if (activityService.isFinished(userHero.getId())) {
      return "redirect:/activity/pvp/reward?id=" + userHero.getId();
    }
    return "redirect:/activity/pvp/fight?id=" + userHero.getId();
  }

  @GetMapping("/pvp/welcome")
  public String pvpWelcome(Model model, @RequestParam("id") long id) {
    Persona userHero = personaRepository.findById(id).get();
    model = commonData(model);
    model.addAttribute("name", userHero.getCharacterName());

    return "game-sites/pvp-welcome";
  }

  @GetMapping("/pvp/choice")
  public String pvpChoice(Model model) {
    model = commonData(model);
    Persona[] enemies = combatService.randomEnemies(Faction.SETTLER);
    model.addAttribute("enemies", enemies);

    model.addAttribute("enemy1", characterService.readCharacter(enemies[0].getId()));
    model.addAttribute("portraitEnemy1", portraitService.findPortrait(enemies[0].getId()));

    model.addAttribute("enemy2", characterService.readCharacter(enemies[1].getId()));
    model.addAttribute("portraitEnemy2", portraitService.findPortrait(enemies[1].getId()));

    model.addAttribute("enemy3", characterService.readCharacter(enemies[2].getId()));
    model.addAttribute("portraitEnemy3", portraitService.findPortrait(enemies[2].getId()));

    return "game-sites/pvp-selection";
  }

  @GetMapping("/pvp/reward")
  public String pvpReward(Model model, @RequestParam("id") long id) {
    Persona userHero = personaRepository.findById(id).get();
    model = commonData(model);

    int initialPullRings = userHero.getPullRing();
    Pair<Combatant, Combatant> combatants = combatService.fightStart(userHero.getId());
    combatService.arenaPrize(combatants);
    int reward = userHero.getPullRing() - initialPullRings;
    model.addAttribute("reward", reward);
    activityService.deleteActivity(userHero.getId());

    return "game-sites/pvp-reward";
  }

  @GetMapping("/pvp/fight")
  public String pvpFight(Model model, @RequestParam("id") long id) {
    Persona userHero = personaRepository.findById(id).get();
    model = commonData(model);

    ActivityDTO dto = activityService.getActivity(id);

    model.addAttribute("enemy", characterService.readCharacter(dto.getEnemyID()));
    model.addAttribute("portraitEnemy", portraitService.findPortrait(dto.getEnemyID()));

    PortraitDTO portraitHero = PortraitMapper.remap(userHero.getPortrait());
    model.addAttribute("portraitHero", portraitHero);
    if (activityService.timeRemaining(userHero.getId()) < 1) {
      return "redirect:/activity/pvp/reward?id=" + userHero.getId();
    }
    model.addAttribute("minutes", activityService.timeRemaining(userHero.getId()));
    return "game-sites/pvp";
  }

  @GetMapping("/pvp/log")
  public String logPvp(@RequestParam("id") long id) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    combatService.logPVP(id);
    return "redirect:/activity/pvp";
  }

  @GetMapping("/pve")
  public String pve(Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    Persona userHero = user.getPersona();
    ActivityDTO dto = activityService.getActivity(userHero.getId());
    ActivityType type = null;
    if (dto != null && dto.getType() != null) {
      type = dto.getType();
    }

    if (type == null) {
      return "redirect:/activity/pve/welcome?id=" + userHero.getId();
    }

    if (type != ActivityType.PVE) {
      return "redirect:/activity/notHere";
    }

    if (activityService.isFinished(userHero.getId())) {
      return "redirect:/activity/pve/reward?id=" + userHero.getId();
    } else {
      return "redirect:/activity/pve/fight?id=" + userHero.getId();
    }
  }

  @GetMapping("/pve/welcome")
  public String pveWelcome(Model model, @RequestParam("id") long id) {
    Persona userHero = personaRepository.findById(id).get();
    model = commonData(model);

    return "game-sites/pve-welcome";
  }

  @GetMapping("/pve/reward")
  public String pveReward(Model model, @RequestParam("id") long id) {

    Persona userHero = personaRepository.findById(id).get();
    model = commonData(model);

    int initialPullRings = userHero.getPullRing();
    Pair<Combatant, Combatant> combatants = combatService.fightStart(userHero.getId());
    combatService.huntPrize(combatants);
    int reward = userHero.getPullRing() - initialPullRings;
    model.addAttribute("reward", reward);
    activityService.deleteActivity(userHero.getId());

    return "game-sites/pve-reward";
  }

  @GetMapping("/pve/choice")
  public String pveChoice(Model model) {
    model = commonData(model);
    Monster[] enemies = combatService.randomMonsters();
    model.addAttribute("enemies", enemies);

    model.addAttribute("enemy1", monsterService.findMonster(enemies[0].getId()));

    model.addAttribute("enemy2", monsterService.findMonster(enemies[1].getId()));

    model.addAttribute("enemy3", monsterService.findMonster(enemies[2].getId()));

    return "game-sites/pve-selection";
  }

  @GetMapping("/pve/fight")
  public String pveFight(Model model, @RequestParam("id") long id) {

    Persona userHero = personaRepository.findById(id).get();
    model = commonData(model);

    ActivityDTO dto = activityService.getActivity(id);

    model.addAttribute("enemy", monsterService.findMonster(dto.getEnemyID()));

    PortraitDTO portraitHero = PortraitMapper.remap(userHero.getPortrait());
    model.addAttribute("portraitHero", portraitHero);
    if (activityService.timeRemaining(userHero.getId()) < 1) {
      return "redirect:/activity/pve/reward?id=" + userHero.getId();
    }
    model.addAttribute("minutes", activityService.timeRemaining(userHero.getId()));
    return "game-sites/pve";
  }

  @GetMapping("/pve/log")
  public String logPve(@RequestParam("id") long id) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    combatService.logPVE(id);
    return "redirect:/activity/pve";
  }
}
