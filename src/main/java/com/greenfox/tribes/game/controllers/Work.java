package com.greenfox.tribes.game.controllers;

import com.greenfox.tribes.game.enums.ActivityType;
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
    /*    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    Persona[] combatants = activityService.arenaFight(user.getPersona().getId());
    //model.addAttribute("combatants", combatants);
    System.out.println(combatants[0].getCharacterName() + " vs. " + combatants[1].getCharacterName());
    if (activityService.isFinished(user.getPersona().getId())) {
      activityService.decideFightResult(combatants);
    }else{

    }*/

    return "game-sites/work";
  }

  @GetMapping("/pvp/done")
  public String pvpDone(
      Model model, @RequestParam("userId") long id, @RequestParam("enemyId") long enemyId) {

    Persona user = userRepository.findById(id).get().getPersona();
    Persona enemy = userRepository.findById(enemyId).get().getPersona();
    Persona[] combatants = {user, enemy};
    // model.addAttribute("combatants", combatants);
    System.out.println(
        combatants[0].getCharacterName() + " vs. " + combatants[1].getCharacterName());
    if (activityService.isFinished(user.getId())) {
      activityService.decideFightResult(combatants);
    }

    return "game-sites/work";
  }

  @GetMapping("/pvp/log")
  public String logPvp(@RequestParam("id") long id) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    Persona[] combatants = activityService.arenaFight(user.getPersona().getId());
    activityService.logActivity(ActivityType.PVP, id);
    String adress =
        "redirect:/activity/pvp/done"
            + "?userId="
            + combatants[0].getPlayer().getId()
            + "&enemyId="
            + combatants[1].getPlayer().getId();
    return adress;
  }
}
