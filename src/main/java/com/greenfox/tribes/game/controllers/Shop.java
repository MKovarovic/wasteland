package com.greenfox.tribes.game.controllers;

import com.greenfox.tribes.game.services.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/shop")
public class Shop {

  @Autowired ShopService shopService;

  @GetMapping("")
  public String browseShop(Model model, Long id) {
    model.addAttribute("DTO", shopService.getShoppingList());
    return "game-sites/shop";
  }

  @GetMapping("/buy")
  public String buy(@RequestParam("id") Long id) {
    shopService.buyStuff(id);
    return "game-sites/shop";
  }

  @GetMapping("/sell")
  public String sell(@RequestParam("id") Long id) {
    shopService.sellStuff(id);
    return "game-sites/shop";
  }


}
