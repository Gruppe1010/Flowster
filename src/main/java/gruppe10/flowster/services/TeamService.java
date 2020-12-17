package gruppe10.flowster.services;

import gruppe10.flowster.repositories.FlowsterRepository;
import gruppe10.flowster.repositories.TeamRepository;
import gruppe10.flowster.viewModels.team.TeamViewModel;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

@Service
public class TeamService
{
    TeamRepository teamRepository = new TeamRepository();
   
    FlowsterRepository flowsterRepository = new FlowsterRepository();




    public TeamViewModel createTeamViewModelFromForm(WebRequest dataFromCreateTeamFrom)
    {
        
        // automatisk-parameternavn som inkrementeres -
        // while(der er flere id'er)
        // opret bruger ud fra id og læg på createTeamViewModel's liste-attr
       
       return new TeamViewModel();
       // TODO return new CreateTeamViewModel(dataFromCreateTeamFrom.getParameter("team-name"), );
    }
    
    /* TODO
    public Team retrieveAndCreateTeamFromDb(int teamId)
    {
        
        ResultSet resultSet = teamRepository.retrieveTeamResultSetFromId(teamId);
        
        
        
        
        
        return new Team();
    }
    
     */
    
    public String createTeamNameFromForm(WebRequest dataFromCreateTeamForm)
    {
        String teamName;
 
        teamName = dataFromCreateTeamForm.getParameter("team-name");
        
        return teamName;
    }
    
    public boolean isTeamNameAvailable(String orgDbName, String teamName)
    {
        return teamRepository.retrieveTeamFromTeamName(orgDbName, teamName);
    }
    
    public void insertNewTeamIntoDb(String orgDbName, String teamName)
    {
        // først tilføjes nyt team til teams-tabel
        teamRepository.insertNewTeamIntoDb(orgDbName, teamName);
        // derefter findes teamId på nyoprettet team
        int teamId = teamRepository.retrieveTeamIdFromTeamName(orgDbName, teamName);
        // ny række i teams_users-tabel med nyt teamId og loggedInUser's id
        // == teamet og brugeren der har oprettet det bliver altså knyttet til hinanden
        teamRepository.insertRowIntoTeamsUsers(orgDbName, teamId, UserService.loggedInUser.getId());
    }
    
    
    
    public int retrieveTeamIdFromTeamName(String orgDbName, String teamName)
    {
        return teamRepository.retrieveTeamIdFromTeamName(orgDbName, teamName);
   
    }
    
    public TeamViewModel retrieveAndCreateEditTeamViewModelFromId(String orgDbName, int teamId)
    {
       return teamRepository.retrieveAndCreateEditTeamViewModelFromId(orgDbName, teamId);
    
    }

    public void insertRowIntoTeamsUsers(String orgDbName, int teamId, int userId)
    {
        teamRepository.insertRowIntoTeamsUsers(orgDbName, teamId, userId);
    }

    public void deleteRowFromTeamsUser(String orgDbName, int teamId, int userId)
    {
        teamRepository.deleteRowFromTeamsUsers(orgDbName, teamId, userId);
    }
    
    public TeamViewModel retrieveAndCreateViewTeamViewModelFromId(String orgDbName, int teamId)
    {
        return teamRepository.retrieveAndCreateViewTeamViewModelFromId(orgDbName, teamId);
    }
    
    public void updateTeamName(String orgDbName, int teamId, String newTeamName)
    {
        teamRepository.updateTeamName(orgDbName, teamId, newTeamName);
        
        
    }


    public void updateJoinedTeamList()
    {
        // TODO dette skridt er overflødigt - vi kan få den med som param gennem pathVariable-orgDbName
        String dbName = convertOrganisationNameToDbName
                (flowsterRepository.retrieveOrganisationNameFromEmail(UserService.loggedInUser.getEmail()));

        UserService.loggedInUser.setJoinedTeamList(teamRepository.retrieveTeamsListFromUserId(dbName,
                UserService.loggedInUser.getId()));
    }

    /**
     * Konverterer organisationName til db-name
     *
     * @param organisationName organisationName som skal konverteres til db-name
     * @return String konverterede db-name
     * */
    public String convertOrganisationNameToDbName(String organisationName)
    {
        // laver underscore hvor der er mellemrum i orgName
        String convertedOrganisationName = organisationName.replaceAll(" ", "_");

        // laver til små bogstaver
        convertedOrganisationName = convertedOrganisationName.toLowerCase();

        return "flowster_" + convertedOrganisationName;
    }
    
    
    
}
