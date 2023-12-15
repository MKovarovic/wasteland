package com.greenfox.tribes.game.controllers;

import com.greenfox.tribes.game.services.ShopService;
import com.greenfox.tribes.gameuser.models.WastelandUser;
import com.greenfox.tribes.gameuser.repositories.UserRepository;
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
@RequestMapping("/shop")
public class Shop {

  @Autowired ShopService shopService;
  @Autowired
  UserRepository userRepository;
  @GetMapping("")
  public String browseShop(Model model, Long id) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
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
