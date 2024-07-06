package br.com.fenix.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	@GetMapping("/login")
	public String login(Model model) {
		System.out.println("LoginController -> login ");
    	   model.addAttribute("loginError", false);
		return "login";
	}

	
	  // Login form with error

// Login form with error
  @RequestMapping("/login-error.html")
  public String loginError(Model model) {
	System.out.println("LoginController -> login error ");
    model.addAttribute("loginError", true);
    return "/login";
  }
}

