package com.greenfox.tribes.controllers;

import com.greenfox.tribes.exceptions.UserAlreadyExistsException;
import com.greenfox.tribes.services.CustomUserDetailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@AllArgsConstructor
public class AuthController {

  private AuthenticationManager provider;
  private CustomUserDetailService userDetailsService;

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
      userDetailsService.createUser(username, password);
    } catch (UserAlreadyExistsException e) {
      ra.addFlashAttribute("alreadyExists", true);
      return new RedirectView("/welcome");
    }

    // Authenticate user programmatically after registration
    Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
    Authentication authenticated = provider.authenticate(authentication);

    SecurityContextHolder.getContext().setAuthentication(authenticated);

    HttpSession session = request.getSession(true);
    session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

    ra.addAttribute("username", username);
    return new RedirectView("/character/new");
  }

  @GetMapping("/")
  public RedirectView main() {
    return new RedirectView("/character/me");
  }
}
