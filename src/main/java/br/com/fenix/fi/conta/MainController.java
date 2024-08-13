package br.com.fenix.fi.conta;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.fenix.dominio.dto.Option;
import ch.qos.logback.core.model.Model;

@Controller()
@RequestMapping("/select")
public class MainController {

   
    @ModelAttribute("options")
    public List<Option> index(Model model) {
        List<Option> options = Arrays.asList(
            new Option("1", "Option 1"),
            new Option("2", "Option 2"),
            new Option("3", "Option 3")
        );
//        model.
//        .addAttribute("options", options);
        return options;
    }
    
    @GetMapping
    public String testeSelect() { 
    	return "testeSelect"; 
    }
//
//    public static class Option {
//        private String value;
//        private String text;
//
//        public Option(String value, String text) {
//            this.value = value;
//            this.text = text;
//        }
//
//        public String getValue() {
//            return value;
//        }
//
//        public String getText() {
//            return text;
//        }
//    }
}
