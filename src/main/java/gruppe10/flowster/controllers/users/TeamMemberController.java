package gruppe10.flowster.controllers.users;

import gruppe10.flowster.models.users.TeamMember;
import gruppe10.flowster.services.TeamMemberService;
import gruppe10.flowster.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TeamMemberController
{
    UserService userService = new UserService();
    TeamMemberService teamMemberService = new TeamMemberService();
    
    
    @GetMapping("/frontPage")
    public String frontPage(Model loggedInTeamMemberModel)
    {
        // TODO måske er castet (TeamMember) overflødig
        loggedInTeamMemberModel.addAttribute("loggedInTeamMember", (TeamMember) userService.loggedInUser);
        
        return "teamMember/frontPage"; // html
    }
    
    
    
    
    
    
}
