package gruppe10.flowster.controllers.users;

import gruppe10.flowster.models.users.TeamMember;
import gruppe10.flowster.services.TeamMemberService;
import gruppe10.flowster.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
// generalisering af GetMappings for hele klassen:
@RequestMapping("/teamMember")
public class TeamMemberController
{
    UserService userService = new UserService();
    
    @GetMapping("/frontPage")
    public String frontPage(Model loggedInUserModel)
    {
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        
        //System.out.println(UserService.loggedInUser.getJoinedTeamsList().size());
        
        return "teamMember/front-page"; // html
    }
    
    @GetMapping("/teams")
    public String teams(Model loggedInUserModel)
    {
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        
        // TODO ret
        return "projectManager/menubar/teams"; // html
    }
    
    @GetMapping("/projects")
    public String projects(Model loggedInUserModel)
    {
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        // TODO ret
        return "projectManager/menubar/projects"; // html
    }
    
    
    
    
}
