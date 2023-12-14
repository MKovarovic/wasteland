package com.greenfox.tribes.game.controllers;

import com.greenfox.tribes.persona.dtos.PersonaDTO;
import com.greenfox.tribes.persona.services.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/character")
public class CharacterUI {

  @Autowired CharacterService characterService;


  @GetMapping("/new")
  public String newCharacter() {
    return "persona-sites/character-creation";
  }

  @GetMapping("/me")
  public String myCharacter(Model model) {
    PersonaDTO dto = characterService.readCharacter();
    model.addAttribute("DTO", dto);
    return "persona-sites/main-page";
  }
}
