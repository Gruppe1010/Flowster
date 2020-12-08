package gruppe10.flowster.controllers.users;

import gruppe10.flowster.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
/*  TODO: slettet fordi vi har slettet projectManager-mappen i templates
// generalisering af GetMappings for hele klassen:
@RequestMapping("/projectManager")

 */
public class ProjectManagerController
{
    
    UserService userService = new UserService();
    
    @GetMapping("/frontPage")
    public String frontPage(Model loggedInUserModel)
    {
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);

        //System.out.println(UserService.loggedInUser.getJoinedTeamsList().size());
        
        return "front-page"; // html
    }
    
    @GetMapping("/teams")
    public String teams(Model loggedInUserModel)
    {
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        
        return "navbars/teams"; // html
    }
    
    @GetMapping("/projects")
    public String projects(Model loggedInUserModel)
    {
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        
        return "navbars/projects"; // html
    }
    
    
    

}
