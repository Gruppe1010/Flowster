package gruppe10.flowster.services;

import gruppe10.flowster.models.teams.Team;
import gruppe10.flowster.viewModels.team.CreateTeamViewModel;
import org.springframework.web.context.request.WebRequest;

public class TeamService
{
    public static Team currentTeam = new Team();
    
    public CreateTeamViewModel createTeamViewModelFromForm(WebRequest dataFromCreateTeamFrom)
    {
        
        // automatisk-parameternavn som inkrementeres -
        // while(der er flere id'er)
        // opret bruger ud fra id og læg på createTeamViewModel's liste-attr
       
       return new CreateTeamViewModel();
       // TODO return new CreateTeamViewModel(dataFromCreateTeamFrom.getParameter("team-name"), );
    }
    
    
    
    
}
