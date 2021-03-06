package gruppe10.flowster.services;

import gruppe10.flowster.models.teams.Team;
import gruppe10.flowster.repositories.FlowsterRepository;
import gruppe10.flowster.repositories.TeamRepository;
import gruppe10.flowster.viewModels.team.TeamViewModel;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;

@Service
public class TeamService
{
    TeamRepository teamRepository = new TeamRepository();
   
    FlowsterRepository flowsterRepository = new FlowsterRepository();
    
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
    
    public ArrayList<Team> retrieveJoinedTeamList()
    {
        // Til fremtidig udvikling: Dette skridt er overflødigt,
        // da vi kan få den med som param gennem pathVariable-orgDbName
        String dbName = convertOrganisationNameToDbName
                                (flowsterRepository.retrieveOrganisationNameFromEmail(UserService.loggedInUser.getEmail()));
        
        return teamRepository.retrieveTeamsListFromUserId(dbName, UserService.loggedInUser.getId());
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
    
    public boolean checkIfTeamIsOnProject(String orgDbName, int teamId, int projectId)
    {
        return teamRepository.checkIfTeamIsOnProject(orgDbName, teamId, projectId);
    }
    
    
}
