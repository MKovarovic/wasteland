package com.greenfox.tribes.controllers;

import com.greenfox.tribes.services.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shop")
public class Shop {

    @Autowired
    ShopService shopService;
    @GetMapping("/")
    public String browseShop(Model model, Long id){
        model.addAttribute("DTO", shopService.getShoppingList(id));
        return "Shop";
    }

}
