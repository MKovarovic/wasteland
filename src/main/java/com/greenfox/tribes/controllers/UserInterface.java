package com.greenfox.tribes.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserInterface {

    @GetMapping("/character/new")
    public static String newCharacter(){
        return "characterCreation";
    }
}
