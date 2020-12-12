package gruppe10.flowster.repositories;

import gruppe10.flowster.models.project.Project;
import gruppe10.flowster.models.teams.Team;
import gruppe10.flowster.viewModels.project.CreateProjectViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;

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

                // joinedProjectsList.add(team);
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
    
    
    public boolean checkIfProjectTitleIsAvailable(String dbName, String title)
    {
        boolean projectTitleIsAvailable = true;
    
        organisationConnection = generalRepository.establishConnection(dbName);
    
        try
        {
            // TOD
            String sqlCommand = "SELECT id_project FROM projects WHERE project_title = ?";
        
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
        
            preparedStatement.setString(1, title);
        
            ResultSet resultSet = preparedStatement.executeQuery();
        
            // hvis der er et projekt som allerede har titlen
            if(resultSet.next())
            {
                // så er titlen IKKE available
                projectTitleIsAvailable = false;
            }
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in checkIfProjectTitleIsAvailable: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in checkIfProjectTitleIsAvailableFinally: " + e.getMessage());
            }
        }
        
        
        return projectTitleIsAvailable;
    }
    
    public void insertNewProjectIntoDb(String dbName, CreateProjectViewModel createProjectViewModel)
    {
        organisationConnection = generalRepository.establishConnection(dbName);
    
        try
        {
            String title = createProjectViewModel.getTitle();
            Date deadline = null;
            // hvis der ER noget gemt som deadline
            if(createProjectViewModel.getDeadline() != "")
            {
                // konverter String til Date
                deadline = Date.valueOf(createProjectViewModel.getDeadline());
            }
        
            String sqlCommand = "INSERT INTO projects (project_title, project_deadline) values (?, ?)";
        
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
        
            preparedStatement.setString(1, title);
            preparedStatement.setDate(2, deadline);
        
            preparedStatement.executeUpdate();
        
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in projectRepository insertNewProjectIntoDb: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in projectRepository insertNewProjectIntoDbFinally: " + e.getMessage());
            }
        }
    
    }
    
    
    public int retrieveProjectIdFromProjectTitle(String dbName, String projectTitle)
    {
        int projectId = 0;
    
        organisationConnection = generalRepository.establishConnection(dbName);
    
        try
        {
            // TOD
            String sqlCommand = "SELECT id_project FROM projects WHERE project_title = ?";
        
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
        
            preparedStatement.setString(1, projectTitle);
        
            ResultSet resultSet = preparedStatement.executeQuery();
        
            if(resultSet.next())
            {
                projectId = resultSet.getInt("id_project");
            }
        
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in projectRepository retrieveProjectIdFromProjectTitle: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in projectRepository retrieveProjectIdFromProjectTitleFinally: " + e.getMessage());
            }
        }
        
        
        return projectId;
    }
    
    public void insertRowIntoProjectsUsers(String dbName, int projectId, int userId)
    {
        organisationConnection = generalRepository.establishConnection(dbName);
    
        try
        {
            String sqlCommand = "INSERT INTO projects_users (f_id_project, f_id_user) values (?, ?)";
        
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
        
            preparedStatement.setInt(1, projectId);
            preparedStatement.setInt(2, userId);
        
            preparedStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in insertRowIntoProjectsUsers: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in insertRowIntoProjectsUsersFinally: " + e.getMessage());
            }
        }
    }
    
    public int findMaxIdFromTable(String dbName, String tableName)
    {
        int maxId = 0;
    
        organisationConnection = generalRepository.establishConnection(dbName);
    
        try
        {
            String idColumnName = "id_" + tableName;
            // fjerner det sidste tegn fra db-namet (altså 's' fordi vores tabelnavne altid slutter på s)
            idColumnName = idColumnName.substring(0,idColumnName.length()-1);
            
            String sqlCommand = String.format("SELECT MAX(%s) FROM %s", idColumnName, tableName);
    
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
            
            // preparedStatement.setString(1, tableName);
        
            ResultSet resultSet = preparedStatement.executeQuery();
        
            if(resultSet.next())
            {
                maxId = resultSet.getInt(String.format("MAX(%s)", idColumnName));
            }
        
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in findMaxIdFromTable: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in findMaxIdFromTableFinally: " + e.getMessage());
            }
        }
    
    
        return maxId;
    }
    



}
