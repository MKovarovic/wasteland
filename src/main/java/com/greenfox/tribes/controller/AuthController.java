package com.greenfox.tribes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

  @GetMapping("/")
  public String main() {
    return "index";
  }

  @GetMapping("/secure")
  public String secure() {
    return "secure";
  }

  @GetMapping("/login")
  public String login() {
    return "login";
  }
}
