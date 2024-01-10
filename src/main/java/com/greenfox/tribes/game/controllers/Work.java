package com.greenfox.tribes.game.controllers;

import com.greenfox.tribes.game.dtos.ActivityDTO;
import com.greenfox.tribes.game.enums.ActivityType;
import com.greenfox.tribes.game.models.ActivityLog;
import com.greenfox.tribes.game.services.ActivityService;
import com.greenfox.tribes.gameuser.models.WastelandUser;
import com.greenfox.tribes.gameuser.repositories.UserRepository;
import com.greenfox.tribes.persona.models.Persona;
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
public class Work {
  @Autowired UserRepository userRepository;
  @Autowired ActivityService activityService;

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
    if(activityService.isFinished(userHero.getId())){
      Persona[] combatants = activityService.fightOutcome(userHero.getId());
      activityService.arenaPrize(combatants);
    }

ActivityLog dto = activityService.getActivity(userHero.getId()).get();
    if(dto.getEnemyID() != 0){
      model.addAttribute("enemyName", userRepository.findById(dto.getEnemyID()).get().getPersona().getCharacterName());
      model.addAttribute("enemyATK", userRepository.findById(dto.getEnemyID()).get().getPersona().getAtk());
      model.addAttribute("enemyHP", userRepository.findById(dto.getEnemyID()).get().getPersona().getHp());
      model.addAttribute("enemyDMG", userRepository.findById(dto.getEnemyID()).get().getPersona().getDmg());
      model.addAttribute("enemyDEF", userRepository.findById(dto.getEnemyID()).get().getPersona().getDef());
      model.addAttribute("enemyLCK", userRepository.findById(dto.getEnemyID()).get().getPersona().getLck());



    }else{
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



    return "game-sites/pvp";
    }




  @GetMapping("/pvp/log")
  public String logPvp() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    activityService.pvpMatching(user.getPersona().getId());
    //activityService.logActivity(ActivityType.PVP, user.getPersona().getId());
    return "redirect:/activity/pvp";
}}
