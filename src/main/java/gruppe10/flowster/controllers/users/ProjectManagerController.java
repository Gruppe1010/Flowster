package gruppe10.flowster.controllers.users;

import gruppe10.flowster.models.users.User;
import gruppe10.flowster.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
// generalisering af GetMappings for hele klassen:
@RequestMapping("/projectManager")
public class ProjectManagerController
{
    
    UserService userService = new UserService();
    
    @GetMapping("/frontPage")
    public String frontPage(Model loggedInUserModel)
    {
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        
        // TODO: RET TIL: return "projectManager/front-page"; // html
        
        System.out.println(UserService.loggedInUser.getJoinedTeamsList().size());
        
        return "projectManager/front-page"; // html
    }
    
    @GetMapping("/teams")
    public String teams(Model loggedInUserModel)
    {
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        
        return "projectManager/menubar/teams"; // html
    }
    
    @GetMapping("/projects")
    public String projects(Model loggedInUserModel)
    {
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        
        return "projectManager/menubar/projects"; // html
    }
    
    
    
    
    @GetMapping("/contact")
    public String contact(Model loggedInUserModel)
    {
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
    
        return "general/contact"; // html
    }

}
