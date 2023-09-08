package sg.edu.nus.iss.spring_security_demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/{[path:[^\\.]*}")
public class HomeController {
    
    public String redirect() {
        return "forward:/";
    }
}  
