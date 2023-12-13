package com.greenfox.tribes.game.controllers;

import com.greenfox.tribes.persona.dtos.PersonaDTO;
import com.greenfox.tribes.persona.services.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserInterface {

  @Autowired CharacterService characterService;

  @GetMapping("/character/new")
  public String newCharacter() {
    return "CharacterCreation";
  }

  @GetMapping("/character/me")
  public String myCharacter(Model model, @RequestParam("id") Long id) {
    PersonaDTO dto = characterService.readCharacter(id);
    model.addAttribute("DTO", dto);
    return "MainPage";
  }
}