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
import org.springframework.web.context.request.WebRequest;

@Controller
public class TeamController
{
    TeamService teamService = new TeamService();
    CreateTeamViewModel createTeamViewModel = new CreateTeamViewModel();
    UserService userService = new UserService();

    String orgDbName;


    String redirect = "redirect:/";
    
    @GetMapping("/{orgDbName}/createTeam")
    public String createTeam(Model loggedInUserModel, Model createTeamViewModel, Model orgDbNameModel)
    {
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        createTeamViewModel.addAttribute("createTeamViewModel", this.createTeamViewModel);

        orgDbName = userService.getOrgDbName();
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        
        return "navbars/create-team"; // html
    }
    
    /*
    @PostMapping("/postCreateTeam")
    public String postCreateTeam(WebRequest dataFromCreateTeamForm)
    {
        // oprette createTeamViewModel(dataFromCreateTeamForm) ud fra webRequest
        createTeamViewModel = teamService.createTeamViewModelFromForm(dataFromCreateTeamForm);
    
        // tjekke om info er valid (teamnavn optaget, eller identisk team findes allerede)
        boolean dataIsValid = teamService.checkDataFromCreateTeamViewModel(createTeamViewModel);
    
        // hvis data er valid
        if(dataIsValid)
        {
            // tilføj nyt team til db
            teamService.insertNewTeamIntoDb(createTeamViewModel);
    
            int teamId = TeamService.currentTeam.getId();
    
            return redirect + "viewTeam?" + teamId;
        }
    
        // hvis data ikke er valid
        return redirect + "createTeam";
    }
    
     */
    
     
    
     @GetMapping("/viewTeam/{teamId}")
    public String viewTeam(@PathVariable int teamId, Model loggedInUserModel, Model teamModel)
    {
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
