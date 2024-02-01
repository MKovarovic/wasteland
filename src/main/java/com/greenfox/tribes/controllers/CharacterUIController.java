package com.greenfox.tribes.controllers;

import com.greenfox.tribes.dtos.PortraitDTO;
import com.greenfox.tribes.enums.Faction;
import com.greenfox.tribes.mappers.PortraitMapper;
import com.greenfox.tribes.repositories.PersonaRepository;
import com.greenfox.tribes.repositories.PortraitRepository;
import com.greenfox.tribes.services.EquipmentService;
import com.greenfox.tribes.models.WastelandUser;
import com.greenfox.tribes.repositories.UserRepository;
import com.greenfox.tribes.services.CustomUserDetailService;
import com.greenfox.tribes.repositories.CharacterEquipmentRepository;
import com.greenfox.tribes.dtos.PersonaDTO;
import com.greenfox.tribes.models.Persona;
import com.greenfox.tribes.services.PersonaService;
import com.greenfox.tribes.services.PortraitService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/character")
public class CharacterUIController {

  private PersonaService characterService;

  private PersonaRepository personaRepository;
  private CustomUserDetailService userService;
  private UserRepository userRepository;
  private PortraitService portraitService;
  private PortraitRepository portraitRepository;
  private EquipmentService equipmentService;
  private CharacterEquipmentRepository pairingRepo;

  @GetMapping("/new")
  public String newCharacter() {
    return "persona-sites/character-creation";
  }

  @GetMapping("/new/create")
  public String finishCreation(
      @RequestParam("characterName") String characterName,
      @RequestParam("faction") String faction,
      @RequestParam("atk") int atk,
      @RequestParam("dmg") int dmg,
      @RequestParam("def") int def,
      @RequestParam("hp") int hp,
      @RequestParam("lck") int lck,
      @RequestParam("faceImg") String faceImg,
      @RequestParam("eyeImg") String eyeImg,
      @RequestParam("eyebrowsImg") String eyebrowsImg,
      @RequestParam("noseImg") String noseImg,
      @RequestParam("mouthImg") String mouthImg,
      @RequestParam("hairImg") String hairImg) {

    Persona persona =
        characterService.addCharacter(characterName, hp, atk, dmg, def, lck, Faction.valueOf(faction.toUpperCase()), 100);
    System.out.println(persona);
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    Long idPortrait =
        portraitService.createPortrait(
            faceImg, hairImg, eyeImg, noseImg, mouthImg, eyebrowsImg, persona.getId());

    persona.setPortrait(portraitRepository.findById(idPortrait).get());
    personaRepository.save(persona);
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    user.setPersona(persona);
    userRepository.save(user);

    return "redirect:/character/me";
  }

  @GetMapping("/me")
  public String myCharacter(Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();

    if (user.getPersona() == null) {
      return "persona-sites/character-creation";
    }

    PersonaDTO dto = characterService.readCharacter();
    model.addAttribute("DTO", dto);

    int[] bonuses = characterService.getBonuses(dto);
    model.addAttribute("atkBonus", bonuses[0]);
    model.addAttribute("defBonus", bonuses[1]);
    model.addAttribute("hpBonus", bonuses[2]);
    model.addAttribute("lckBonus", bonuses[3]);
    model.addAttribute("dmgBonus", bonuses[4]);
    model.addAttribute("inventory", dto.getInventory());

    PortraitDTO portraitDTO = PortraitMapper.remap(user.getPersona().getPortrait());
    model.addAttribute("portraitDTO", portraitDTO);
    model.addAttribute("faction", user.getPersona().getFaction().toString());
    return "persona-sites/main-page";
  }

  @RequestMapping("/me/equip")
  public String toggleEquip(@RequestParam("id") long id) {
    characterService.toggleEquip((long) id);
    return "redirect:/character/me";
  }
}
