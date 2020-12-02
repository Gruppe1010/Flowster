package gruppe10.flowster.controllers.users;

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
        loggedInTeamMemberModel.addAttribute("loggedInTeamMember", userService.loggedInTeamMember);
        
        return "TeamMember/frontPage"; // html
    }
    
    
    
    
}
