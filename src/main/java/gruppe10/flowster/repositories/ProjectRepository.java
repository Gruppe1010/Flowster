package gruppe10.flowster.repositories;

import gruppe10.flowster.models.project.Project;
import gruppe10.flowster.models.teams.Team;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProjectRepository
{
    
    GeneralRepository generalRepository = new GeneralRepository();
    Connection organisationConnection;

    // TODO HER HER HER
    public ArrayList<Project> retrieveProjectsArrayListFromUserId(String dbName, int userId)
    {
        ArrayList<Project> joinedProjectsList = null;

        organisationConnection = generalRepository.establishConnection(dbName);

        try
        {
            // TOD
            String sqlCommand = "SELECT id_team, team_name FROM teams_users " +
                    "RIGHT JOIN teams ON f_id_team = id_team WHERE f_id_user = ?;";

            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);

            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            joinedProjectsList = createJoinedProjectsListFromResultSet(resultSet);

        }
        catch(SQLException e)
        {
            System.err.println("ERROR in retrieveProjectsArrayListFromUserId: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in retrieveProjectsArrayListFromUserIdFinally: " + e.getMessage());
            }
        }

        return joinedProjectsList;
    }

    // TODO HER HER HER
    public ArrayList<Project> createJoinedProjectsListFromResultSet(ResultSet resultSet)
    {
        ArrayList<Project> joinedProjectsList = new ArrayList<>();

        try
        {
            while(resultSet.next())
            {
                // TODO: VI SKAL OGSÅ give teamet de to lister!!!!! - men det gad jeg ikke lige
                Team team = new Team(resultSet.getInt("id_team"), resultSet.getString("team_name"));

                joinedProjectsList.add(team);
            }
            if(joinedProjectsList.size() == 0)
            {
                joinedProjectsList = null;
            }

        }
        catch(SQLException e)
        {
            System.err.println("ERROR in createJoinedTeamsListFromResultSet: " + e.getMessage());
        }

        return joinedProjectsList;
    }



}
