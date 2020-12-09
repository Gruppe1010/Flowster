package gruppe10.flowster.controllers;

import gruppe10.flowster.models.teams.Team;
import gruppe10.flowster.services.TeamService;
import gruppe10.flowster.services.UserService;
import gruppe10.flowster.viewModels.team.CreateTeamViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("/{orgDbName}")
public class TeamController
{
    UserService userService = new UserService();
    
    TeamService teamService = new TeamService();
    String teamNameModel = null;
    String orgDbName;
    
    
    @GetMapping("/createTeam")
    public String createTeam(@PathVariable String orgDbName, Model loggedInUserModel, Model teamNameModel,
                             Model orgDbNameModel)
    {
        userService.updateJoinedTeamsList();
        
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        teamNameModel.addAttribute("createTeamViewModel", this.teamNameModel);

        
        return "navbars/create-team"; // html
    }
    
    
    @PostMapping("/createTeam")
    public String postCreateTeam(@PathVariable String orgDbName, WebRequest dataFromCreateTeamForm)
    {
        // oprette createTeamViewModel(dataFromCreateTeamForm) ud fra webRequest
        teamNameModel = teamService.createTeamNameModelFromForm(dataFromCreateTeamForm);
    
        // tjek om teamnavn optaget
        boolean teamNameIsAvailable = teamService.isTeamNameAvailable(orgDbName, teamNameModel);
    
        // hvis teamName ikke findes allerede
        if(teamNameIsAvailable)
        {
            // tilføj nyt team til db
            teamService.insertNewTeamIntoDb(orgDbName, teamNameModel);
    
            // vi henter id på nyoprettet team
            int teamId = teamService.retrieveTeamIdFromTeamName(orgDbName, teamNameModel);
    
            return "redirect:editTeam/" + teamId;
        }
        
        return "redirect:createTeam";
    }
    
     
    
     @GetMapping("/viewTeam/{teamId}")
    public String viewTeam(@PathVariable int teamId, Model loggedInUserModel, Model teamModel)
    {
        userService.updateJoinedTeamsList();
        
        // Team team = teamService.retrieveAndCreateTeamFromDb(teamId);
    
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        // teamModel.addAttribute("team", team);
        
        // teamModel.addAttribute(har teamID);
        // lav en model med url - "/postEditTeam/" + teamId + "/addUser/" - og i html: + ${user.getId()}
        System.out.println(teamId);
        return "Hej";
    }
    
    
    // editTeam/9
    @GetMapping("/editTeam/{teamId}")
    public String editTeam(@PathVariable int teamId, Model loggedInUser, Model teamModel)
    {
        userService.updateJoinedTeamsList();
        
        // teamModel.addAttribute(har teamID);
        // lav en model med url - "/postEditTeam/" + teamId + "/addUser/" - og i html: + ${user.getId()}
        System.out.println(teamId);
        return "Hej";
    }
    
    // th:each="user : userlist"
    // Lav tom form med submitknap med action="/postEditTeam/${teamModel.getId()}/addUser/${user.getId()}"
    @PostMapping("/postEditTeam/{teamId}/addUser/{userId}")
    public String editTeam(@PathVariable int teamId, @PathVariable int userId, Model loggedInUser)
    {
        System.out.println(teamId);
        return "Hej";
    }
    
    
    
    
    
    
    
}
