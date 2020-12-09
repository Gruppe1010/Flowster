package gruppe10.flowster.repositories;

import gruppe10.flowster.models.teams.Team;

import java.awt.desktop.ScreenSleepEvent;
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
    
    
    public boolean retrieveTeamFromTeamName(String dbName, String teamName)
    {
        Boolean teamNameIsAvailable = true;
        
        organisationConnection = generalRepository.establishConnection(dbName);
    
        try
        {
            String sqlCommand = "SELECT * FROM teams WHERE team_name = ?";
        
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
        
            preparedStatement.setString(1, teamName);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            // hvis der er noget i resultSet'et, er teamName altså IKKE ledig
            if(resultSet.next())
            {
                teamNameIsAvailable = false;
            }
         
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in TeamRepository retrieveTeamFromTeamName: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in TeamRepository retrieveTeamFromTeamNameFinally: " + e.getMessage());
            }
        }
        
        
        return teamNameIsAvailable;
    }
    
    
    public void insertNewTeamIntoDb(String dbName, String teamName)
    {
        organisationConnection = generalRepository.establishConnection(dbName);
    
        try
        {
            String sqlCommand = "INSERT INTO teams (team_name) value (?)";
        
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
        
            preparedStatement.setString(1, teamName);
        
            preparedStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in insertNewTeamIntoDb: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in insertNewTeamIntoDbFinally: " + e.getMessage());
            }
        }
    }
    
    public int retrieveTeamIdFromTeamName(String dbName, String teamName)
    {
        int teamId = 0;
        
        organisationConnection = generalRepository.establishConnection(dbName);
    
        try
        {
            String sqlCommand = "SELECT id_team FROM teams WHERE team_name = ?";
        
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
        
            preparedStatement.setString(1, teamName);
        
            ResultSet resultSet = preparedStatement.executeQuery();
            
            teamId = createTeamIdFromResultSet(resultSet);
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in retrieveTeamIdFromTeamName: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in retrieveTeamIdFromTeamNameFinally: " + e.getMessage());
            }
        }
        
        
        return teamId;
    }
    
    public int createTeamIdFromResultSet(ResultSet resultSet)
    {
        int teamId = 0;
    
        try
        {
            if(resultSet.next())
            {
                teamId = resultSet.getInt("id_team");
            }
        
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in createTeamIdFromResultSet: " + e.getMessage());
        }
        
        return teamId;
    
    }
    
    public void insertNewRowIntoTeamsUsers(String dbName, int teamId, int userId)
    {
        organisationConnection = generalRepository.establishConnection(dbName);
    
        try
        {
            String sqlCommand = "INSERT INTO teams_users (f_id_team, f_id_user) values (?, ?)";
        
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
        
            preparedStatement.setInt(1, teamId);
            preparedStatement.setInt(2, userId);
            
            preparedStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in insertNewRowIntoTeamsUsers: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in insertNewRowIntoTeamsUsersFinally: " + e.getMessage());
            }
        }
        
        
        
        
        
        
    }
    
    
    
}
