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
    
    // global variabel fordi hvis navnet er optaget, skal vi bruge variablen i GetMapping'en så det kan blive
    // ved med at stå der i html'en
    String teamName = null;
    String error = null;

    @GetMapping("/teams")
    public String teams(@PathVariable String orgDbName, Model orgDbNameModel, Model loggedInUserModel)
    {
        orgDbName = userService.findOrgDbName();
        orgDbNameModel.addAttribute("orgDbName", orgDbName);

        teamService.updateJoinedTeamList();

        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);

        return "team/teams"; // html
    }


    @GetMapping("/createTeam")
    public String createTeam(@PathVariable String orgDbName, Model loggedInUserModel, Model teamNameModel,
                             Model orgDbNameModel, Model errorModel)
    {
    
        // så sidebar hele tiden er opdateret med de teams, som man er en del af
        teamService.updateJoinedTeamList();
        
        // model til error-popup
        errorModel.addAttribute("error", error);
    
        // modeller til menu-, sidebar og footer
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        teamNameModel.addAttribute("createTeamViewModel", this.teamName);
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        
        return "team/create-team"; // html
    }
    
    @PostMapping("/createTeam")
    public String postCreateTeam(@PathVariable String orgDbName, WebRequest dataFromCreateTeamForm)
    {
        // nulstil error
        error = null;
        
        // oprette createTeamViewModel(dataFromCreateTeamForm) ud fra webRequest
        teamName = teamService.createTeamNameFromForm(dataFromCreateTeamForm);
    
        // tjek om teamnavn optaget
        boolean teamNameIsAvailable = teamService.isTeamNameAvailable(orgDbName, teamName);
    
        // hvis teamName ikke findes allerede
        if(teamNameIsAvailable)
        {
            // tilføj nyt team til db
            teamService.insertNewTeamIntoDb(orgDbName, teamName);
    
            // vi henter id på nyoprettet team
            int teamId = teamService.retrieveTeamIdFromTeamName(orgDbName, teamName);
    
            return String.format("redirect:/%s/editTeam/%d", orgDbName, teamId);
            // return "redirect:editTeam/" + teamId;
        }
    
    
        error =
                String.format("Der findes allerede et team ved navn \"%s\" i din virksomhed", teamName);
        
        return String.format("redirect:/%s/createTeam/#error-popup", orgDbName);
    }
    
    @GetMapping("/editTeam/{teamId}")
    public String editTeam(@PathVariable String orgDbName, @PathVariable int teamId,
                           Model loggedInUserModel, Model teamModel, Model orgDbNameModel, Model errorModel)
    {
        // model til error-popup
        errorModel.addAttribute("error", error);
    
        // så sidebar hele tiden er opdateret med de teams, som man er en del af
        teamService.updateJoinedTeamList();
    
        TeamViewModel teamViewModel = teamService.retrieveAndCreateEditTeamViewModelFromId(orgDbName, teamId);
    
        // modeller til menu-, sidebar og footer
        teamModel.addAttribute("team", teamViewModel);
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
      
        // teamModel.addAttribute(har teamID);
        // lav en model med url - "/postEditTeam/" + teamId + "/addUser/" - og i html: + ${user.getId()}
     
        return "team/edit-team";
    }
    
    @PostMapping("/editTeam/{teamId}/editTeamName")
    public String editTeamName(@PathVariable String orgDbName, @PathVariable int teamId,
                               WebRequest dataFromEditTeamNameForm)
    {
        error = null;
        
        // Opret og gem newTeamName i String ud fra webRequest
        String newTeamName = teamService.createTeamNameFromForm(dataFromEditTeamNameForm);
        
        // tjek om teamnavn allerede eksisterer i db == er optaget
        boolean teamNameIsAvailable = teamService.isTeamNameAvailable(orgDbName, newTeamName);
        
        // hvis teamName er ledigt
        if(teamNameIsAvailable)
        {
            // opdater teamName på teamet der har teamId'et vi er inde på
            teamService.updateTeamName(orgDbName, teamId, newTeamName);
            
            // gå tilbage til at se teamet
            return String.format("redirect:/%s/viewTeam/%d", orgDbName, teamId);
        }
        
        error =
                String.format("Der findes allerede et team ved navn \"%s\" i din virksomhed", newTeamName);
        
        return String.format("redirect:/%s/editTeam/%d/#error-popup", orgDbName, teamId);
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
    public String postRemoveUserFromTeam(@PathVariable String orgDbName, @PathVariable int teamId,
                                         @PathVariable int userId)
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
        teamService.updateJoinedTeamList();
        
        TeamViewModel teamViewModel = teamService.retrieveAndCreateViewTeamViewModelFromId(orgDbName, teamId);
    
    
        // modeller til menu-, sidebar og footer
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        teamModel.addAttribute("team", teamViewModel);
        
        
        
    
        return "team/view-team";
    }
    
  
  
    
    
    
    
    
    
}
