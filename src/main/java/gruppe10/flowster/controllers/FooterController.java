package gruppe10.flowster.controllers;

import gruppe10.flowster.models.users.User;
import gruppe10.flowster.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FooterController
{
    
    UserService userService = new UserService();
    
    @GetMapping("/contact")
    public String contact1(Model loggedInUserModel, Model orgDbNameModel)
    {
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        orgDbNameModel.addAttribute("orgDbName", userService.findOrgDbName());
        
        return "footer/contact"; // html
    }
    
    @GetMapping("/about")
    public String about1(Model loggedInUserModel, Model orgDbNameModel)
    {
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        orgDbNameModel.addAttribute("orgDbName", userService.findOrgDbName());
        
        return "footer/about"; // html
    }
    
    @GetMapping("/{orgDbName}/contact")
    public String contact(Model loggedInUserModel, Model orgDbNameModel, @PathVariable String orgDbName)
    {
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        
        return "footer/contact"; // html
    }
    
    @GetMapping("/{orgDbName}/about")
    public String about(Model loggedInUserModel, Model orgDbNameModel, @PathVariable String orgDbName)
    {
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        
        return "footer/about"; // html
    }
    
 
    
    
}
