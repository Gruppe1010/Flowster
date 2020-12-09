package gruppe10.flowster.controllers;

import gruppe10.flowster.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
/*  TODO: slettet fordi vi har slettet projectManager-mappen i templates
// generalisering af GetMappings for hele klassen:
@RequestMapping("/projectManager")

 */
public class MenubarController
{
    UserService userService = new UserService();
    String orgDbName;
    
    
    @GetMapping("/{orgDbName}/frontPage")
    public String frontPage(@PathVariable String orgDbName, Model orgDbNameModel, Model loggedInUserModel)
    {
        orgDbName = userService.getOrgDbName();
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        
        return "front-page"; // html
    }
    
    @GetMapping("/{orgDbName}/teams")
    public String teams(@PathVariable String orgDbName, Model orgDbNameModel, Model loggedInUserModel)
    {
        orgDbName = userService.getOrgDbName();
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        
        userService.updateJoinedTeamsList();
        
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        
        return "navbars/teams"; // html
    }
    
    @GetMapping("/{orgDbName}/projects")
    public String projects(@PathVariable String orgDbName, Model orgDbNameModel, Model loggedInUserModel)
    {
        orgDbName = userService.getOrgDbName();
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        
        return "navbars/projects"; // html
    }
    
    @GetMapping("/logOut")
    public String logOut()
    {
        // TODO metode som sætter loggedInUser til null
        
        return "redirect:/";
    }
    

}
