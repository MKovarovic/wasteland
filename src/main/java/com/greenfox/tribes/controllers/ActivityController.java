package com.greenfox.tribes.controllers;

import com.greenfox.tribes.dtos.ActivityDTO;
import com.greenfox.tribes.dtos.PortraitDTO;
import com.greenfox.tribes.enums.ActivityType;
import com.greenfox.tribes.models.Combatant;
import com.greenfox.tribes.services.ActivityService;
import com.greenfox.tribes.models.WastelandUser;
import com.greenfox.tribes.repositories.UserRepository;
import com.greenfox.tribes.repositories.MonsterRepository;
import com.greenfox.tribes.models.Persona;
import com.greenfox.tribes.services.CharacterService;
import com.greenfox.tribes.services.MonsterService;
import com.greenfox.tribes.services.PortraitService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
  // todo: autowired
  private UserRepository userRepository;
  private ActivityService activityService;
  private MonsterRepository monsterRepository;
  private CharacterService characterService;
  private PortraitService portraitService;
  private MonsterService monsterService;

  @GetMapping("/work")
  public String work(Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    if (activityService.isFinished(user.getPersona().getId())) {
      activityService.deleteActivity(user.getPersona().getId());
    }

    model.addAttribute("name", user.getPersona().getCharacterName());
    model.addAttribute("faction", user.getPersona().getFaction());
    model.addAttribute("isBusy", !activityService.isFinished(user.getPersona().getId()));
    model.addAttribute("id", user.getPersona().getId());
    return "game-sites/work";
  }

  @GetMapping("/work/log")
  public String logWork(@RequestParam("id") long id) {
    activityService.logActivity(ActivityType.WORK, id);
    return "redirect:/activity/work";
  }

  @GetMapping("/pvp")
  public String pvp(Model model) {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    Persona userHero = userRepository.findById(user.getPersona().getId()).get().getPersona();

    ActivityDTO dto = activityService.getActivity(userHero.getId());
    ActivityType type = null;
    if (dto != null && dto.getType() != null) {
      type = dto.getType();
    }

    if (type == null) {
      return "redirect:/activity/pvp/welcome?id=" + userHero.getId();
    } else if (type == ActivityType.PVP) {

      if (activityService.isFinished(userHero.getId())) {
        return "redirect:/activity/pvp/reward?id=" + userHero.getId();
      }
      return "redirect:/activity/pvp/fight?id=" + userHero.getId();
    }
    return "redirect:/activity/notHere";
  }

  @GetMapping("/pvp/welcome")
  public String pvpWelcome(Model model, @RequestParam("id") long id) {
    Persona userHero = userRepository.findById(id).get().getPersona();
    model.addAttribute("hero", characterService.readCharacter(userHero.getId()));
    model.addAttribute("faction", userHero.getFaction());
    model.addAttribute("isBusy", activityService.isFinished(userHero.getId()));

    return "game-sites/pvp-welcome";
  }

  @GetMapping("/pvp/reward")
  public String pvpReward(Model model, @RequestParam("id") long id) {
    Persona userHero = userRepository.findById(id).get().getPersona();
    model.addAttribute("hero", characterService.readCharacter(userHero.getId()));
    model.addAttribute("faction", userHero.getFaction());
    model.addAttribute("isBusy", !activityService.isFinished(userHero.getId()));

    int pullrings = userHero.getPullRing();
    Combatant[] combatants = activityService.fightStart(userHero.getId());
    activityService.arenaPrize(combatants);
    int reward = userHero.getPullRing() - pullrings;
    model.addAttribute("reward", reward);
    activityService.deleteActivity(userHero.getId());

    return "game-sites/pvp-reward";
  }

  @GetMapping("/pvp/fight")
  public String pvpFight(Model model, @RequestParam("id") long id) {
    Persona userHero = userRepository.findById(id).get().getPersona();
    model.addAttribute("hero", characterService.readCharacter(userHero.getId()));
    model.addAttribute("faction", userHero.getFaction());
    model.addAttribute("isBusy", activityService.isFinished(userHero.getId()));

    ActivityDTO dto = activityService.getActivity(id);

    model.addAttribute("enemy", characterService.readCharacter(dto.getEnemyID()));
    model.addAttribute("portraitEnemy", portraitService.findPortrait(dto.getEnemyID()));

    PortraitDTO portraitHero = portraitService.findPortrait(userHero.getId());
    model.addAttribute("portraitHero", portraitHero);
    if (activityService.timeRemaining(userHero.getId()) < 1) {
      return "redirect:/activity/pvp/reward?id=" + userHero.getId();
    }
    model.addAttribute("minutes", activityService.timeRemaining(userHero.getId()));
    return "game-sites/pvp";
  }

  @GetMapping("/pvp/log")
  public String logPvp() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    activityService.pvpMatching(user.getPersona().getId());
    return "redirect:/activity/pvp";
  }

  @GetMapping("/pve")
  public String pve(Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    Persona userHero = userRepository.findById(user.getPersona().getId()).get().getPersona();

    ActivityDTO dto = activityService.getActivity(userHero.getId());
    ActivityType type = null;
    if (dto != null && dto.getType() != null) {
      type = dto.getType();
    }

    if (type == null) {
      return "redirect:/activity/pve/welcome?id=" + userHero.getId();
    } else if (type == ActivityType.PVE) {

      if (activityService.isFinished(userHero.getId())) {
        return "redirect:/activity/pve/reward?id=" + userHero.getId();
      }
      return "redirect:/activity/pve/fight?id=" + userHero.getId();
    } else {
      return "redirect:/activity/notHere";
    }
  }

  @GetMapping("/pve/welcome")
  public String pveWelcome(Model model, @RequestParam("id") long id) {
    Persona userHero = userRepository.findById(id).get().getPersona();
    model.addAttribute("hero", characterService.readCharacter(userHero.getId()));
    model.addAttribute("isBusy", activityService.isFinished(userHero.getId()));

    return "game-sites/pve-welcome";
  }

  @GetMapping("/pve/reward")
  public String pveReward(Model model, @RequestParam("id") long id) {
    Persona userHero = userRepository.findById(id).get().getPersona();
    model.addAttribute("hero", characterService.readCharacter(userHero.getId()));
    model.addAttribute("faction", userHero.getFaction());
    model.addAttribute("isBusy", !activityService.isFinished(userHero.getId()));

    int pullrings = userHero.getPullRing();
    Combatant[] combatants = activityService.fightStart(userHero.getId());
    activityService.huntPrize(combatants);
    int reward = userHero.getPullRing() - pullrings;
    model.addAttribute("reward", reward);
    activityService.deleteActivity(userHero.getId());

    return "game-sites/pve-reward";
  }

  @GetMapping("/pve/fight")
  public String pveFight(Model model, @RequestParam("id") long id) {
    Persona userHero = userRepository.findById(id).get().getPersona();
    model.addAttribute("hero", characterService.readCharacter(userHero.getId()));
    model.addAttribute("isBusy", activityService.isFinished(userHero.getId()));

    ActivityDTO dto = activityService.getActivity(id);

    model.addAttribute("enemy", monsterService.findMonster(dto.getEnemyID()));

    PortraitDTO portraitHero = portraitService.findPortrait(userHero.getId());
    model.addAttribute("portraitHero", portraitHero);
    if (activityService.timeRemaining(userHero.getId()) < 1) {
      return "redirect:/activity/pve/reward?id=" + userHero.getId();
    }
    model.addAttribute("minutes", activityService.timeRemaining(userHero.getId()));
    return "game-sites/pve";
  }

  @GetMapping("/pve/log")
  public String logPve() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    activityService.pveMatching(user.getPersona().getId());
    return "redirect:/activity/pve";
  }
}
