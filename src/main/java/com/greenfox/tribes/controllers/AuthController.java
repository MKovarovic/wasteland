package com.greenfox.tribes.controllers;

import com.greenfox.tribes.exceptions.UserAlreadyExistsException;
import com.greenfox.tribes.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@AllArgsConstructor
public class AuthController {

  private AuthService authService;

  @GetMapping("/welcome")
  public String welcome() {
    return "welcome";
  }

  @PostMapping("/register")
  public RedirectView registerPost(
      @RequestParam String username,
      @RequestParam String password,
      RedirectAttributes ra,
      HttpServletRequest request) {
    try {
      authService.registerAndLogIn(username, password, request.getSession(true));
    } catch (UserAlreadyExistsException e) {
      ra.addFlashAttribute("alreadyExists", true);
      return new RedirectView("/welcome");
    }
    return new RedirectView("/character/new");
  }

  @GetMapping("/")
  public RedirectView main() {
    return new RedirectView("/character/me");
  }
}
