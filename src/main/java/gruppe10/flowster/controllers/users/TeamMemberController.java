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
    TeamMemberService teamMemberService = new TeamMemberService();
    
    
    
    @GetMapping("/frontPage")
    public String frontPage(Model loggedInUserModel)
    {
        System.out.println(UserService.loggedInUser.getFirstname());
        
        
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        
        // TODO: RET TIL: return "projectManager/front-page"; // html
        return "teamMember/front-page-test"; // html
    }
    
    
    /* GAMLE
    @GetMapping("/frontPage")
    public String frontPage(Model loggedInTeamMemberModel)
    {
        // TODO måske er castet (TeamMember) overflødig
        loggedInTeamMemberModel.addAttribute("loggedInTeamMember", (TeamMember) userService.loggedInUser);
        
        return "teamMember/frontPage"; // html
    }
    
     */
    
    
    
    
    @GetMapping("/contact")
    public String contact(Model loggedInUserModel)
    {
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        
        return "general/contact"; // html
    }
    
    
}
