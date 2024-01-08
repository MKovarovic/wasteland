package com.greenfox.tribes.game.controllers;

import com.greenfox.tribes.game.services.ActivityService;
import com.greenfox.tribes.gameuser.models.WastelandUser;
import com.greenfox.tribes.gameuser.repositories.UserRepository;
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
    model.addAttribute("name", user.getPersona().getCharacterName());
    model.addAttribute("faction", user.getPersona().getFaction());
    model.addAttribute("isBusy", user.getPersona().getIsBusy());
    model.addAttribute("id", user.getPersona().getId());
    activityService.isFinished(user.getPersona().getId());
    return "game-sites/work";
  }

  @GetMapping("/work/log")
  public String logWork(@RequestParam("id") long id) {
    activityService.logActivity("WORK", id);
    return "redirect:/activity/work";
  }
}
