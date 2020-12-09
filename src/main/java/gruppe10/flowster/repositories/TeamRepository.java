package gruppe10.flowster.repositories;

import gruppe10.flowster.models.teams.Team;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TeamRepository
{
    
    GeneralRepository generalRepository = new GeneralRepository();
    
    Connection organisationConnection;
    
    public ArrayList<Team> retrieveTeamsArrayListFromUserId(String dbName, int userId)
    {
        ArrayList<Team> joinedTeamsList = null;
        
        organisationConnection = generalRepository.establishConnection(dbName);
        
        try
        {
            String sqlCommand = "SELECT id_team, team_name FROM teams_users " +
                                        "RIGHT JOIN teams ON f_id_team = id_team WHERE f_id_user = ?;";
            
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setInt(1, userId);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            joinedTeamsList = createJoinedTeamsListFromResultSet(resultSet);
            
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in retrieveTeamsArrayListFromUserId: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in retrieveTeamsArrayListFromUserIdFinally: " + e.getMessage());
            }
        }
        
        return joinedTeamsList;
    }
    
    public ArrayList<Team> createJoinedTeamsListFromResultSet(ResultSet resultSet)
    {
        ArrayList<Team> joinedTeamsList = new ArrayList<>();
        
        try
        {
            while(resultSet.next())
            {
                // TODO: VI SKAL OGSÅ give teamet de to lister!!!!! - men det gad jeg ikke lige
                Team team = new Team(resultSet.getInt("id_team"), resultSet.getString("team_name"));
                
                joinedTeamsList.add(team);
            }
            if(joinedTeamsList.size() == 0)
            {
                joinedTeamsList = null;
            }
            
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in createJoinedTeamsListFromResultSet: " + e.getMessage());
        }
        
        return joinedTeamsList;
    }
    
    // TODO HER MANGLER DER at vi henter de to lister som er attributter på Team-klasse
    
    /* TODO her er vi i gang lige nu
    public ResultSet retrieveTeamResultSetFromId(int teamId)
    {
        
        ResultSet resultSet = null;
    
        organisationConnection = generalRepository.establishConnection(dbName);
    
        try
        {
        
            int emailId = flowsterRepository.retrieveEmailIdFromEmail(logInViewModel.getEmail());
        
            String sqlCommand = "SELECT * FROM users WHERE f_id_email = ? and password = ?";
        
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
        
            preparedStatement.setInt(1, emailId);
            preparedStatement.setString(2, logInViewModel.getPassword());
        
        
            ResultSet resultSet = preparedStatement.executeQuery();
        
            user = createUserFromResultSet(resultSet);
        
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in retrieveUserFromDb: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in retrieveUserFromDbFinally: " + e.getMessage());
            }
        }
    
    
    
    
    
    
        return resultSet;
    }
    
     */
    
    
    
}
