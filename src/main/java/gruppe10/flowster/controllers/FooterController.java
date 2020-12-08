package gruppe10.flowster.controllers;

import gruppe10.flowster.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FooterController
{
    
    @GetMapping("/contact")
    public String contact(Model loggedInUserModel)
    {
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        
        return "footer/contact"; // html
    }
    
    @GetMapping("/about")
    public String about(Model loggedInUserModel)
    {
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        
        return "footer/about"; // html
    }
    
    
    
    
}
