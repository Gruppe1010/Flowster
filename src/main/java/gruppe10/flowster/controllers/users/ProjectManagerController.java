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
        return "projectManager/front-page-test"; // html
    }
    
    @GetMapping("/teams")
    public String teams(Model loggedInUserModel)
    {
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        
        return "projectManager/menubar/teams"; // html
    }
    
  

}
