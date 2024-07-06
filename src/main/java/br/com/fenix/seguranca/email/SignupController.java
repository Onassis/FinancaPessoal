package br.com.fenix.seguranca.email;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.fenix.seguranca.usuario.UsuarioDto;
import br.com.fenix.seguranca.usuario.UsuarioServicoImp;




@Controller
@RequestMapping("/signup")
public class SignupController {
	
	private static Log log = LogFactory.getLog(SignupController.class);
	
	private UsuarioServicoImp userService;
	
	public SignupController(UsuarioServicoImp userService) {

		this.userService = userService;
	}

	@GetMapping
	public String signup(Model model) {
		
		model.addAttribute("user", new UsuarioDto());
		return "signup";
	}	

	@PostMapping
	public String doSignup(@Validated @ModelAttribute("user") UsuarioDto user,
			BindingResult result,
			RedirectAttributes redirectAttributes) {
		
		if (result.hasErrors())
			return "signup";
		
//		userService.signup(user);
//		MyUtils.flash(redirectAttributes, "success", "signupSuccess");
		return "redirect:/";
	}	
}
