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
public class WorkController {
  @Autowired UserRepository userRepository;
  @Autowired ActivityService activityService;
  @Autowired
  MonsterRepository monsterRepository;
  @Autowired
  CharacterService characterService;
  @Autowired
  PortraitService  portraitService;

  @GetMapping("/work")
  public String work(Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    activityService.isFinished(user.getPersona().getId());
    model.addAttribute("name", user.getPersona().getCharacterName());
    model.addAttribute("faction", user.getPersona().getFaction());
    model.addAttribute("isBusy", user.getPersona().getIsBusy());
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
    if (activityService.isFinished(userHero.getId())) {
      Combatant[] combatants = activityService.fightStart(userHero.getId());
      activityService.arenaPrize(combatants);
    }

    ActivityDTO dto = activityService.getActivity(userHero.getId());
    int noEnemy = 1;
    if (dto != null) {
      noEnemy = 0;
      model.addAttribute("enemy", characterService.readCharacter(userRepository.findById(dto.getEnemyID()).get().getPersona().getId()));
      /*model.addAttribute(
          "enemyName",
          userRepository.findById(dto.getEnemyID()).get().getPersona().getCharacterName());
      model.addAttribute(
          "enemyATK", userRepository.findById(dto.getEnemyID()).get().getPersona().getAtk());
      model.addAttribute(
          "enemyHP", userRepository.findById(dto.getEnemyID()).get().getPersona().getHp());
      model.addAttribute(
          "enemyDMG", userRepository.findById(dto.getEnemyID()).get().getPersona().getDmg());
      model.addAttribute(
          "enemyDEF", userRepository.findById(dto.getEnemyID()).get().getPersona().getDef());
      model.addAttribute(
          "enemyLCK", userRepository.findById(dto.getEnemyID()).get().getPersona().getLck());*/

    } /*else {
      model.addAttribute("enemyName", "????");
      model.addAttribute("enemyATK", "????");
      model.addAttribute("enemyHP", "????");
      model.addAttribute("enemyDMG", "????");
      model.addAttribute("enemyDEF", "????");
      model.addAttribute("enemyLCK", "????");
    }*/
    model.addAttribute("noEnemy", noEnemy);
    model.addAttribute("hero", characterService.readCharacter(userHero.getId()));
    PortraitDTO portraitHero = portraitService.findPortrait(userHero.getId());
    model.addAttribute("portraitHero", portraitHero);
    /*model.addAttribute("Name", userHero.getCharacterName());
    model.addAttribute("ATK", userHero.getAtk());
    model.addAttribute("HP", userHero.getHp());
    model.addAttribute("DMG", userHero.getDmg());
    model.addAttribute("DEF", userHero.getDef());
    model.addAttribute("LCK", userHero.getLck());*/

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
    if (activityService.isFinished(userHero.getId())) {
      Combatant[] combatants = activityService.fightStart(userHero.getId());
      activityService.huntPrize(combatants);
    }

    ActivityDTO dto = activityService.getActivity(userHero.getId());
    if (dto != null) {
      model.addAttribute("enemyName", monsterRepository.findById(dto.getEnemyID()).get().getName());
      model.addAttribute("enemyATK", monsterRepository.findById(dto.getEnemyID()).get().getAtk());
      model.addAttribute("enemyHP", monsterRepository.findById(dto.getEnemyID()).get().getHp());
      model.addAttribute("enemyDMG", monsterRepository.findById(dto.getEnemyID()).get().getDmg());
      model.addAttribute("enemyDEF", monsterRepository.findById(dto.getEnemyID()).get().getDef());
      model.addAttribute("enemyLCK", monsterRepository.findById(dto.getEnemyID()).get().getLck());

    } else {
      model.addAttribute("enemyName", "????");
      model.addAttribute("enemyATK", "????");
      model.addAttribute("enemyHP", "????");
      model.addAttribute("enemyDMG", "????");
      model.addAttribute("enemyDEF", "????");
      model.addAttribute("enemyLCK", "????");
    }

    model.addAttribute("Name", userHero.getCharacterName());
    model.addAttribute("ATK", userHero.getAtk());
    model.addAttribute("HP", userHero.getHp());
    model.addAttribute("DMG", userHero.getDmg());
    model.addAttribute("DEF", userHero.getDef());
    model.addAttribute("LCK", userHero.getLck());

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
