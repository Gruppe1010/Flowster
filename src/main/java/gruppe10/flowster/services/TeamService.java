package gruppe10.flowster.services;

import gruppe10.flowster.models.teams.Team;
import gruppe10.flowster.repositories.GeneralRepository;
import gruppe10.flowster.repositories.TeamRepository;
import gruppe10.flowster.viewModels.team.CreateTeamViewModel;
import org.springframework.web.context.request.WebRequest;

import java.sql.ResultSet;

public class TeamService
{
    TeamRepository teamRepository = new TeamRepository();
   
    
    public CreateTeamViewModel createTeamViewModelFromForm(WebRequest dataFromCreateTeamFrom)
    {
        
        // automatisk-parameternavn som inkrementeres -
        // while(der er flere id'er)
        // opret bruger ud fra id og læg på createTeamViewModel's liste-attr
       
       return new CreateTeamViewModel();
       // TODO return new CreateTeamViewModel(dataFromCreateTeamFrom.getParameter("team-name"), );
    }
    
    /* TODO
    public Team retrieveAndCreateTeamFromDb(int teamId)
    {
        
        ResultSet resultSet = teamRepository.retrieveTeamResultSetFromId(teamId);
        
        
        
        
        
        return new Team();
    }
    
     */
    
    
    
    
}
