package com.greenfox.tribes.controllers;

import com.greenfox.tribes.services.ShopService;
import com.greenfox.tribes.models.WastelandUser;
import com.greenfox.tribes.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@RequestMapping("/shop")
public class ShopController {

   ShopService shopService;
   UserRepository userRepository;

  @GetMapping("")
  public String browseShop(Model model, Long id) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    model.addAttribute("faction", user.getPersona().getFaction());
    model.addAttribute("DTO", shopService.getShoppingList());
    model.addAttribute("rings", user.getPersona().getPullRing());
    return "game-sites/shop";
  }

  @PostMapping("/buy")
  public String buy(@RequestParam("id") Long id) {
    shopService.buyStuff(id);
    return "redirect:/shop";
  }

  @PostMapping("/sell")
  public String sell(@RequestParam("id") Long id) {
    shopService.sellStuff(id);
    return "redirect:/shop";
  }
}
