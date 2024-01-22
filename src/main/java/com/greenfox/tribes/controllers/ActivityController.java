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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/activity")
public class ActivityController {
  // todo: autowired
  @Autowired UserRepository userRepository;
  @Autowired ActivityService activityService;
  @Autowired MonsterRepository monsterRepository;
  @Autowired CharacterService characterService;
  @Autowired PortraitService portraitService;
  @Autowired private MonsterService monsterService;

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

  // todo: way too long for a single controller method, try to make shorter
  @GetMapping("/pvp")
  public String pvp(Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    Persona userHero = userRepository.findById(user.getPersona().getId()).get().getPersona();

    ActivityDTO dto = activityService.getActivity(userHero.getId());
    ActivityType type;
    if (dto != null && dto.getType() != null) {
      type = dto.getType();
    }
    model.addAttribute("hero", characterService.readCharacter(userHero.getId()));
    model.addAttribute("faction", user.getPersona().getFaction());
    model.addAttribute("isBusy", !activityService.isFinished(user.getPersona().getId()));
    int noEnemy = 1;
    if (dto != null) {
      if (dto.getType() == ActivityType.PVP) {
        if (activityService.isFinished(userHero.getId())) {
          int pullrings = userHero.getPullRing();
          Combatant[] combatants = activityService.fightStart(userHero.getId());
          activityService.arenaPrize(combatants);
          int reward = userHero.getPullRing() - pullrings;
          model.addAttribute("reward", reward);
          activityService.deleteActivity(userHero.getId());

          return "game-sites/pvp-reward";
        }
        noEnemy = 0;
        model.addAttribute(
            "enemy",
            characterService.readCharacter(
                userRepository.findById(dto.getEnemyID()).get().getPersona().getId()));
        model.addAttribute("portraitEnemy", portraitService.findPortrait(dto.getEnemyID()));

        model.addAttribute("noEnemy", noEnemy);

        PortraitDTO portraitHero = portraitService.findPortrait(userHero.getId());
        model.addAttribute("portraitHero", portraitHero);
        model.addAttribute("minutes", activityService.timeRemaining(userHero.getId()));
        // todo: instead of showing different pages from the same endpoint, redirect to a new endpoint
        return "game-sites/pvp";
      }
      return "redirect:/character/me";
    }

    return "game-sites/pvp-welcome";
  }

  @GetMapping("/pvp/log")
  public String logPvp() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    activityService.pvpMatching(user.getPersona().getId());
    return "redirect:/activity/pvp";
  }

  // todo: try to make it shorter
  @GetMapping("/pve")
  public String pve(Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    Persona userHero = userRepository.findById(user.getPersona().getId()).get().getPersona();

    ActivityDTO dto = activityService.getActivity(userHero.getId());
    ActivityType type;
    if (dto != null && dto.getType() != null) {
      type = dto.getType();
    }
    model.addAttribute("hero", characterService.readCharacter(userHero.getId()));
    model.addAttribute("isBusy", !activityService.isFinished(user.getPersona().getId()));
    int noEnemy = 1;
    if (dto != null) {
      if (dto.getType() == ActivityType.PVE) {
        if (activityService.isFinished(userHero.getId())) {
          int pullrings = userHero.getPullRing();
          Combatant[] combatants = activityService.fightStart(userHero.getId());
          activityService.huntPrize(combatants);
          int reward = userHero.getPullRing() - pullrings;
          model.addAttribute("reward", reward);
          activityService.deleteActivity(userHero.getId());

          return "game-sites/pve-reward";
        }
        noEnemy = 0;
        model.addAttribute("enemy", monsterService.findMonster(dto.getEnemyID()));
        // model.addAttribute("portraitEnemy", portraitService.findPortrait(dto.getEnemyID()));

        model.addAttribute("noEnemy", noEnemy);

        PortraitDTO portraitHero = portraitService.findPortrait(userHero.getId());
        model.addAttribute("portraitHero", portraitHero);
        model.addAttribute("minutes", activityService.timeRemaining(userHero.getId()));

        return "game-sites/pve";
      }
      return "redirect:/character/me";
    }

    return "game-sites/pve-welcome";
    /*if (activityService.isFinished(userHero.getId())) {
      Combatant[] combatants = activityService.fightStart(userHero.getId());
      activityService.huntPrize(combatants);
    }
    int noEnemy = 1;
    ActivityDTO dto = activityService.getActivity(userHero.getId());
    if (dto != null) {
      noEnemy = 0;
      model.addAttribute("enemy", monsterRepository.findById(dto.getEnemyID()).get());
    }

    model.addAttribute("faction", user.getPersona().getFaction());
    model.addAttribute("noEnemy", noEnemy);
    model.addAttribute("hero", characterService.readCharacter(userHero.getId()));
    PortraitDTO portraitHero = portraitService.findPortrait(userHero.getId());
    model.addAttribute("portraitHero", portraitHero);
    model.addAttribute("minutes", activityService.timeRemaining(userHero.getId()));*/

  }

  @GetMapping("/pve/log")
  public String logPve() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    activityService.pveMatching(user.getPersona().getId());
    return "redirect:/activity/pve";
  }
}
