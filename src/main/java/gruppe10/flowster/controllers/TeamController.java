package gruppe10.flowster.controllers;

import gruppe10.flowster.models.teams.Team;
import gruppe10.flowster.services.TeamService;
import gruppe10.flowster.services.UserService;
import gruppe10.flowster.viewModels.team.EditTeamViewModel;
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
    EditTeamViewModel editTeamViewModel = null;
  
    @GetMapping("/createTeam")
    public String createTeam(@PathVariable String orgDbName, Model loggedInUserModel, Model teamNameModel,
                             Model orgDbNameModel)
    {
        userService.updateJoinedTeamsList();
        
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        teamNameModel.addAttribute("createTeamViewModel", this.teamNameModel);
        orgDbNameModel.addAttribute("orgDbName", orgDbName);

        
        return "team/create-team"; // html
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
    
    // editTeam/9
    @GetMapping("/editTeam/{teamId}")
    public String editTeam(@PathVariable String orgDbName, @PathVariable int teamId, Model loggedInUserModel,
                           Model teamModel, Model orgDbNameModel)
    {
        // TODO: opdater modellen - hent ny ArrayList til teamModel - KUN her måske, og ikke i post
        
        userService.updateJoinedTeamsList();
    
        editTeamViewModel = teamService.retrieveAndCreateTeamViewModelFromId(orgDbName, teamId);
        
        teamModel.addAttribute("team", editTeamViewModel);
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
      
        // teamModel.addAttribute(har teamID);
        // lav en model med url - "/postEditTeam/" + teamId + "/addUser/" - og i html: + ${user.getId()}
     
        return "team/edit-team";
    }
    
    // th:each="user : userlist"
    // Lav tom form med submitknap med action="/postEditTeam/${teamModel.getId()}/addUser/${user.getId()}"
    //
    @PostMapping("/editTeam/{teamId}/addUser/{userId}")
    public String postAddUserToTeam(@PathVariable String orgDbName, @PathVariable int teamId, @PathVariable int userId)
    {
        // TODO: opdater modellen - hent ny ArrayList til teamModel
        teamService.insertRowIntoTeamsUsers(orgDbName, teamId, userId);

        // String.format("Jeg hedder %s", "Vibe");
        // TODO return String.format("redirect:/%d/editTeam/%d", orgDbName, teamId);// kører hurtigere
        return "redirect:/" + orgDbName + "/editTeam/" + teamId;
    }
    //
    @PostMapping("/editTeam/{teamId}/removeUser/{userId}")
    public String postRemoveUserFromTeam(@PathVariable String orgDbName, @PathVariable int teamId, @PathVariable int userId)
    {
        // TODO: opdater modellen - hent ny ArrayList til teamModel
        
        System.out.println("remove: " + teamId);
        return "redirect:/" + orgDbName + "/editTeam/" + teamId;
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
    
  
    
    
    
    
    
    
    
}
