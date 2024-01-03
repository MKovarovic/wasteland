package com.greenfox.tribes.gameuser.controllers;

import com.greenfox.tribes.misc.exceptions.UserAlreadyExistsException;
import com.greenfox.tribes.gameuser.services.CustomUserDetailService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@AllArgsConstructor
public class AuthController {

  AuthenticationManager provider;
  CustomUserDetailService userDetailsService;

  @GetMapping("/welcome")
  public String welcome() {
    return "welcome";
  }
  @GetMapping("/login")
  public String login() {
    return "welcome";
  }

  @GetMapping("/register")
  public String register() {
    return "welcome";
  }

  @PostMapping("/register")
  public RedirectView registerPost(
      @RequestParam String username, @RequestParam String password, RedirectAttributes ra) {
    try {
      userDetailsService.createUser(username, password);
    } catch (UserAlreadyExistsException e) {
      ra.addFlashAttribute("alreadyExists", true);
      return new RedirectView("/register");
    }

    // Authenticate user programmatically after registration
    Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
    Authentication authenticated = provider.authenticate(authentication);

    SecurityContextHolder.getContext().setAuthentication(authenticated);

    ra.addAttribute("username", username);
    return new RedirectView("/character/new");

  }

  // todo remove the following, it is only an example
  @GetMapping("/secure")
  public String secure(Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());

    return "user-settings/secure";
  }

  @GetMapping("/")
  public String main() {
    return "main-page";
  }
}
