package gruppe10.flowster.controllers;

import gruppe10.flowster.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
/*  TODO: slettet fordi vi har slettet projectManager-mappen i templates
// generalisering af GetMappings for hele klassen:
@RequestMapping("/projectManager")

 */
public class MenubarController
{

    @GetMapping("/{orgDbName}/frontPage")
    public String frontPage(@PathVariable String orgDbName, Model orgDbNameModel, Model loggedInUserModel)
    {
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        
        return "front-page"; // html
    }

    
    @GetMapping("/logOut")
    public String logOut()
    {
        // TODO metode som sætter loggedInUser til null
        
        return "redirect:/";
    }
    

}
