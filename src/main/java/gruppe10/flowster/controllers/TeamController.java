package gruppe10.flowster.controllers;

import gruppe10.flowster.services.TeamService;
import gruppe10.flowster.services.UserService;
import gruppe10.flowster.viewModels.team.TeamViewModel;
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

    @GetMapping("/teams")
    public String teams(@PathVariable String orgDbName, Model orgDbNameModel, Model loggedInUserModel)
    {
        orgDbName = userService.findOrgDbName();
        orgDbNameModel.addAttribute("orgDbName", orgDbName);

        teamService.updateJoinedTeamsList();

        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);

        return "team/teams"; // html
    }


    @GetMapping("/createTeam")
    public String createTeam(@PathVariable String orgDbName, Model loggedInUserModel, Model teamNameModel,
                             Model orgDbNameModel)
    {
        // så sidebar hele tiden er opdateret med de teams, som man er en del af
        teamService.updateJoinedTeamsList();
        
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
    
            return String.format("redirect:/%s/editTeam/%d", orgDbName, teamId);
            // return "redirect:editTeam/" + teamId;
        }
        
        //hvis teamName er optaget: TODO tilføj error-message
        return "redirect:createTeam";
    }
    
    @GetMapping("/editTeam/{teamId}")
    public String editTeam(@PathVariable String orgDbName, @PathVariable int teamId, Model loggedInUserModel,
                           Model teamModel, Model orgDbNameModel)
    {
        // så sidebar hele tiden er opdateret med de teams, som man er en del af
        teamService.updateJoinedTeamsList();
    
        TeamViewModel teamViewModel = teamService.retrieveAndCreateEditTeamViewModelFromId(orgDbName, teamId);
        
        teamModel.addAttribute("team", teamViewModel);
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
        teamService.insertRowIntoTeamsUsers(orgDbName, teamId, userId);
        
        return String.format("redirect:/%s/editTeam/%d", orgDbName, teamId); // kører hurtigere
        // return "redirect:/" + orgDbName + "/editTeam/" + teamId;
    }
    
    @PostMapping("/editTeam/{teamId}/removeUser/{userId}")
    public String postRemoveUserFromTeam(@PathVariable String orgDbName, @PathVariable int teamId, @PathVariable int userId)
    {

        teamService.deleteRowFromTeamsUser(orgDbName, teamId, userId);
    
        return String.format("redirect:/%s/editTeam/%d", orgDbName, teamId);
        // return "redirect:/" + orgDbName + "/editTeam/" + teamId;
    }
    
     @GetMapping("/viewTeam/{teamId}")
    public String viewTeam(@PathVariable String orgDbName, @PathVariable int teamId,
                           Model loggedInUserModel, Model orgDbNameModel, Model teamModel)
    {
        // så sidebar hele tiden er opdateret med de teams, som man er en del af
        teamService.updateJoinedTeamsList();
    
        TeamViewModel teamViewModel = teamService.retrieveAndCreateViewTeamViewModelFromId(orgDbName, teamId);
        
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        teamModel.addAttribute("team", teamViewModel);
        
    
        return "team/view-team";
    }
    
  
    
    
    
    
    
    
    
}
