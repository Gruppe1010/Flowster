package gruppe10.flowster.repositories;

import gruppe10.flowster.models.project.Project;
import gruppe10.flowster.models.project.Subproject;
import gruppe10.flowster.models.project.Subtask;
import gruppe10.flowster.models.project.Task;
import gruppe10.flowster.models.teams.Team;
import gruppe10.flowster.models.users.User;
import gruppe10.flowster.viewModels.project.CreateProjectViewModel;
import gruppe10.flowster.viewModels.project.CreateSubViewModel;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;

@Repository
public class ProjectRepository
{
    
    GeneralRepository generalRepository = new GeneralRepository();
    // TODO ryk evt. metoden vi kalder via dette obj til generalRep
    OrganisationRepository organisationRepository = new OrganisationRepository();
    TeamRepository teamRepository = new TeamRepository();
    
    Connection organisationConnection;
    
    
    /**
     * Metoder der henter alle de projekter op, som en bruger er tilknyttet via teams hun/han er i
     * Finder først teams som bruger er i --> dernæst findes de projekter teams'ene er tilknyttet
     * teams_users --> f_id_team --> projects_teams --> f_id_project --> projects (INFO HERFRA)
     *
     * @param dbName databasens navn, som projekter skal hentes fra
     * @param userId brugerens id, som tilknyttede teams findes ud fra
     * */
    public ArrayList<Project> retrieveCreatedProjectListFromUserId(String dbName, int userId)
    {
        ArrayList<Project> createdProjectList = null;
        
        organisationConnection = generalRepository.establishConnection(dbName);
        
        try
        {

            String sqlCommand =
                    "SELECT * FROM projects_users " +
                        "RIGHT JOIN projects ON f_id_project = id_project " +
                        "WHERE f_id_user = ?";
            
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setInt(1, userId);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            createdProjectList = createJoinedProjectListFromResultSet(resultSet);
            
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in retrieveProjectArrayListFromUserId: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in retrieveProjectArrayListFromUserIdFinally: " + e.getMessage());
            }
        }
        
        return createdProjectList;
    }
    
    
    public ArrayList<Project> retrieveProjectListFromUserId(String dbName, int userId)
    {
        ArrayList<Project> joinedProjectList = null;

        organisationConnection = generalRepository.establishConnection(dbName);

        try
        { // hej igen
            String sqlCommand =
                    "SELECT DISTINCT " +
                        "id_project, project_title, project_description, project_deadline, project_manhours " +
                        "FROM teams_users " +
                        "RIGHT JOIN teams_projects ON teams_users.f_id_team = teams_projects.f_id_team " +
                        "RIGHT JOIN projects ON teams_projects.f_id_project = projects.id_project " +
                        "WHERE f_id_user = ?";

            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);

            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            
            joinedProjectList = createJoinedProjectListFromResultSet(resultSet);

        }
        catch(SQLException e)
        {
            System.err.println("ERROR in retrieveProjectArrayListFromUserId: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in retrieveProjectArrayListFromUserIdFinally: " + e.getMessage());
            }
        }
        return joinedProjectList;
    }
    
    
    public ArrayList<Project> createJoinedProjectListFromResultSet(ResultSet resultSet)
    {
        ArrayList<Project> joinedProjectList = new ArrayList<>();

        try
        {
            // så længe der er flere rækker i resultSet'et
            while(resultSet.next())
            {
                int projectId = resultSet.getInt("id_project");
                
                // opret nyt projekt
                Project project = new Project
                      (projectId,
                        resultSet.getString("project_title"),
                        resultSet.getString("project_description"),
                        resultSet.getDate("project_deadline"),
                        resultSet.getDouble("project_manhours"),
                        retrieveCreator(projectId),
                        retrieveSubprojectList(projectId),
                        retrieveTeamList(projectId));

                // tilføj nyoprettet projekt til liste
                joinedProjectList.add(project);
            }
            if(joinedProjectList.size() == 0)
            {
                joinedProjectList = null;
            }
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in createJoinedProjectListFromResultSet: " + e.getMessage());
        }

        return joinedProjectList;
    }
    
    public User retrieveCreator(int projectId)
    {
        User creator = null;
        
        try
        {
  
            String sqlCommand = "SELECT * FROM projects_users " +
                                "RIGHT JOIN users ON f_id_user = id_user " +
                                "WHERE f_id_project = ?";
            
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setInt(1, projectId);
    
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if(resultSet.next())
            {
                creator = organisationRepository.createUserFromResultSet(resultSet);
            }
            
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in retrieveCreator: " + e.getMessage());
        }
        
        return creator;
    }
    
    public ArrayList<Subproject> retrieveSubprojectList(int projectId)
    {
        ArrayList<Subproject> subprojectList = null;
    
        try
        {
            String sqlCommand =
                    "SELECT * FROM projects_subprojects " +
                            "RIGHT JOIN subprojects ON f_id_subproject = id_subproject " +
                            "WHERE f_id_project = ?";
    
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
    
            preparedStatement.setInt(1, projectId);
    
            ResultSet resultSet = preparedStatement.executeQuery();
       
            subprojectList = createSubprojectListFromResultSet(resultSet);
            
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in retrieveSubprojectList: " + e.getMessage());
        }
    
        return subprojectList;
    }
    
    public ArrayList<Subproject> createSubprojectListFromResultSet(ResultSet resultSet)
    {
        ArrayList<Subproject> subprojectList = new ArrayList<>();
    
       try
       {
           // så længe der er en række mere i resultSet'et
           while(resultSet.next())
           {
               int subprojectId = resultSet.getInt("id_subproject");
               
               // opret nyt subprojekt
               Subproject subproject = new Subproject
                   (subprojectId,
                   resultSet.getString("subproject_title"),
                   resultSet.getString("subproject_description"),
                   resultSet.getDate("subproject_deadline"),
                   resultSet.getDouble("subproject_manhours"),
                   retrieveTaskList(subprojectId));
        
               // tilføj nyoprettet projekt til liste
               subprojectList.add(subproject);
           }
           if(subprojectList.size() == 0)
           {
               subprojectList = null;
           }
       }
       catch(SQLException e)
       {
           System.err.println("ERROR in createSubprojectListFromResultSet: " + e.getMessage());
       }
       
       return subprojectList;
    }
    
    public ArrayList<Task> retrieveTaskList(int subprojectId)
    {
        ArrayList<Task> taskList = null;
    
        try
        {
            String sqlCommand =
                    "SELECT * FROM subprojects_tasks " +
                            "RIGHT JOIN tasks ON f_id_task = id_task " +
                            "WHERE f_id_subproject = ?";
        
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
        
            preparedStatement.setInt(1, subprojectId);
        
            ResultSet resultSet = preparedStatement.executeQuery();
        
           
            taskList = createTaskListFromResultSet(resultSet);
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in retrieveTaskList: " + e.getMessage());
        }
    
        return taskList;
        
    }
    
    public ArrayList<Task> createTaskListFromResultSet(ResultSet resultSet)
    {
        ArrayList<Task> taskList = new ArrayList<>();
        
        try
        {
            while(resultSet.next())
            {
                int taskId = resultSet.getInt("id_task");
                
                // opretter nyt subprojekt
                Task task = new Task
                       (taskId, resultSet.getString("task_title"),
                       resultSet.getDouble("task_manhours"),
                       retrieveSubtaskList(taskId));
                
                // tilføjer nyoprettet projekt til liste
                taskList.add(task);
            }
            if(taskList.size() == 0)
            {
                taskList = null;
            }
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in createTaskListFromResultSet: " + e.getMessage());
        }
        
        return taskList;
    }
    
    public ArrayList<Subtask> retrieveSubtaskList(int taskId)
    {
        ArrayList<Subtask> subtaskList = null;
        
        try
        {
            String sqlCommand =
                    "SELECT * FROM tasks_subtasks " +
                            "RIGHT JOIN subtasks ON f_id_subtask = id_subtask " +
                            "WHERE f_id_task = ?";
            
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setInt(1, taskId);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            subtaskList = createSubtaskListFromResultSet(resultSet);
            
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in retrieveSubtaskList: " + e.getMessage());
        }
        
        return subtaskList;
    }
    
    public ArrayList<Subtask> createSubtaskListFromResultSet(ResultSet resultSet)
    {
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        
        try
        {
            while(resultSet.next())
            {
                
                // opretter nyt subprojekt
                Subtask subtask = new Subtask
                            (resultSet.getInt("id_subtask"),
                            resultSet.getString("subtask_title"),
                            resultSet.getDouble("subtask_manhours"));
                
                // tilføjer nyoprettet projekt til liste
                subtaskList.add(subtask);
            }
            if(subtaskList.size() == 0)
            {
                subtaskList = null;
            }
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in createSubtaskListFromResultSet: " + e.getMessage());
        }
        
        return subtaskList;
    }
    
    public ArrayList<Team> retrieveTeamList(int projectId)
    {
        ArrayList<Team> teamList = null;
    
        try
        {
            // henter alle teams
            String sqlCommand = "SELECT * FROM teams_projects " +
                                "RIGHT JOIN teams ON f_id_team = id_team " +
                                "WHERE f_id_project = ?";
    
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
    
            preparedStatement.setInt(1, projectId);
    
            ResultSet resultSet = preparedStatement.executeQuery();
    
            
            // denne metoder gemmer reelt kun teamId og teamName - vi mangler altså at gemme userList og projectList
            teamList = teamRepository.createTeamListFromResultSet(resultSet);
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in retrieveTeamList: " + e.getMessage());
        }
    
        return teamList;
    }
    
    // GEM PROJEKT i db
    
    public boolean checkIfProjectTitleIsAvailable(String dbName, String title)
    {
        boolean projectTitleIsAvailable = true;
    
        organisationConnection = generalRepository.establishConnection(dbName);
    
        try
        {
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
            if(createProjectViewModel.getDeadline().equals(""))
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
    
    public Project retrieveProject(String orgDbName, int projectId)
    {
        Project project = null;
    
        organisationConnection = generalRepository.establishConnection(orgDbName);
    
        try
        {
            String sqlCommand = "SELECT * FROM projects WHERE id_project = ?";
        
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
        
            preparedStatement.setInt(1, projectId);
        
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if(resultSet.next())
            {
                project = createProjectFromResultSet(resultSet);
            }
        
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in projectRepository retrieveProject: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in projectRepository retrieveProjectFinally: " + e.getMessage());
            }
        }
        
        return project;
    }
    
    public Project createProjectFromResultSet(ResultSet resultSet)
    {
        Project project = null;
        
        try
        {
            int projectId = resultSet.getInt("id_project");
            
            // opretter nyt projekt
            project = new Project
                          (projectId,
                          resultSet.getString("project_title"),
                          resultSet.getString("project_description"),
                          resultSet.getDate("project_deadline"),
                          resultSet.getDouble("project_manhours"),
                          retrieveCreator(projectId),
                          retrieveSubprojectList(projectId),
                          retrieveTeamList(projectId));
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in createProjectFromResultSet: " + e.getMessage());
        }
        
        return project;
    }
    
    
    // GEM SUBPROJEKT i db
    // TODO - se om de 3 følgende metoder måske kan gøres dynamiske? De bruge både til subproject, task og subtask
    
    public boolean checkIfSubprojectTitleIsAvailable(String dbName, int projectId, String subprojectTitle)
    {
        boolean subprojectTitleIsAvailable = true;
    
        organisationConnection = generalRepository.establishConnection(dbName);
    
        try
        {
            // find alt hvor projectId'et og subprojectTitel mathcer vores input
            String sqlCommand =
                    "SELECT * FROM projects_subprojects " +
                        "RIGHT JOIN subprojects ON f_id_subproject = id_subproject " +
                        "WHERE f_id_project = ? AND subproject_title = ?";
             
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
    
            preparedStatement.setInt(1, projectId);
            preparedStatement.setString(2, subprojectTitle);
           
            ResultSet resultSet = preparedStatement.executeQuery();
        
            // hvis der der findes et subprojekt med titlen subprojectTitle under projektet der har id'et projectId
            if(resultSet.next())
            {
                // så er titlen IKKE available
                subprojectTitleIsAvailable = false;
            }
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in ProjectRepository checkIfSubprojectTitleIsAvailable: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in ProjectRepository checkIfSubprojectTitleIsAvailable: " + e.getMessage());
            }
        }
        
        return subprojectTitleIsAvailable;
    }
    
    public void insertNewSubprojectIntoDb(String dbName, CreateSubViewModel createSubViewModel)
    {
        String title = createSubViewModel.getTitle();
        double mahours = createSubViewModel.getManhours();
                
                organisationConnection = generalRepository.establishConnection(dbName);
    
        try
        {
            String sqlCommand = "INSERT INTO subprojects (subproject_title, subproject_manhours) values (?, ?)";
        
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
        
            preparedStatement.setString(1, title);
            preparedStatement.setDouble(2, mahours);
        
            preparedStatement.executeUpdate();
        
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in projectRepository insertNewSubprojectIntoDb: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in projectRepository insertNewSubprojectIntoDb: " + e.getMessage());
            }
        }
    }
    
    public void insertRowIntoProjectsSubprojects(String dbName, int projectId, int subprojectId)
    {
        organisationConnection = generalRepository.establishConnection(dbName);
        
        try
        {
            String sqlCommand = "INSERT INTO projects_subprojects (f_id_project, f_id_subproject) values (?, ?)";
            
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setInt(1, projectId);
            preparedStatement.setInt(2, subprojectId);
            
            preparedStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in insertRowIntoProjectsSubprojects: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in insertRowIntoProjectsSubprojects: " + e.getMessage());
            }
        }
    }
    
    // GEM TASK i db
    
    public boolean checkIfTaskTitleIsAvailable(String dbName, int subprojectId, String taskTitle)
    {
        // returVærdi sættes til at være true - vi ændrer den KUN til false, hvis vi ud af at det givne subproject
        // HAR en task med taskTitle-værdien
        boolean taskTitleIsAvailable = true;
    
        organisationConnection = generalRepository.establishConnection(dbName);
    
        try
        {
            // find alt hvor subprojedtId'et og taskTitel mathcer vores input
            String sqlCommand =
                    "SELECT * FROM subprojects_tasks " +
                            "RIGHT JOIN tasks ON f_id_task = id_task " +
                            "WHERE f_id_subproject = ? AND task_title = ?";
        
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
        
            preparedStatement.setInt(1, subprojectId);
            preparedStatement.setString(2, taskTitle);
        
            ResultSet resultSet = preparedStatement.executeQuery();
        
            // hvis der der findes et task med titlen taskTitle under subproject'et der har id'et subprojectId
            if(resultSet.next())
            {
                // så er titlen IKKE available
                taskTitleIsAvailable = false;
            }
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in ProjectRepository checkIfTaskTitleIsAvailable: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in ProjectRepository checkIfTaskTitleIsAvailable: " + e.getMessage());
            }
        }
    
        return taskTitleIsAvailable;
        
    }
    
    /**
     * returnerer positiv værdi hvis subprojektet har FLERE timer end task'ene
     *
     *
     *
     * */
    public double calcHoursDiffBetweenSubprojectAndTasks(String dbName, int subproject, double manhours)
    {
        // hent subprojekts arbejdstimer ud fra id
        double subprojectManhours = retrieveManhoursFromSubprojectId(dbName, subproject);
    
        // hent samlede task-hours under subprojektet + indtastede manhours
        double tasksManhours = retrieveTasksManhours(dbName, subproject) + manhours;
        
        return subprojectManhours - tasksManhours;
    }
    
    
    public double retrieveManhoursFromSubprojectId(String dbName, int subprojectId)
    {
        double subprojectManhours = 0;
    
        organisationConnection = generalRepository.establishConnection(dbName);
    
        try
        {
            String sqlCommand = "SELECT subproject_manhours FROM subprojects WHERE id_subproject = ?";
        
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
        
            preparedStatement.setInt(1, subprojectId);
        
            ResultSet resultSet = preparedStatement.executeQuery();
        
            if(resultSet.next())
            {
                subprojectManhours = resultSet.getDouble("subproject_manhours");
            }
        
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in projectRepository retrieveManhoursFromSubprojectId: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in projectRepository retrieveManhoursFromSubprojectIdFinally: " + e.getMessage());
            }
        }
        
        return subprojectManhours;
    }
    
    public double retrieveTasksManhours(String dbName, int subprojectId)
    {
        double tasksManhours = 0;
        
        organisationConnection = generalRepository.establishConnection(dbName);
        
        try
        {
            //
            String sqlCommand = "SELECT task_manhours FROM subprojects_tasks " +
                                "RIGHT JOIN tasks ON f_id_task = id_task " +
                                "WHERE f_id_subproject = ?";
            
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setInt(1, subprojectId);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next())
            {
                tasksManhours += resultSet.getDouble("task_manhours");
            }
            
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in projectRepository retrieveTasksManhours: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in projectRepository retrieveTasksManhoursFinally: " + e.getMessage());
            }
        }
        
        return tasksManhours;
    }
    
    
    public String retrieveSubprojectTitleFromId(String dbName, int subprojectId)
    {
        String subprojectTitle = null;
    
        organisationConnection = generalRepository.establishConnection(dbName);
    
        try
        {
            String sqlCommand = "SELECT subproject_title FROM subprojects WHERE id_subproject = ?";
        
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
        
            preparedStatement.setInt(1, subprojectId);
        
            ResultSet resultSet = preparedStatement.executeQuery();
        
            if(resultSet.next())
            {
                subprojectTitle = resultSet.getString("subproject_title");
            }
        
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in projectRepository retrieveSubprojectTitleFromId: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in projectRepository retrieveSubprojectTitleFromIdFinally: " + e.getMessage());
            }
        }
        return subprojectTitle;
    }
    
    public void insertNewTaskIntoDb(String dbName, String title, double manhours)
    {
        organisationConnection = generalRepository.establishConnection(dbName);
        
        try
        {
            String sqlCommand = "INSERT INTO tasks (task_title, task_manhours) value (?, ?)";
            
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setString(1, title);
            preparedStatement.setDouble(2, manhours);
            
            preparedStatement.executeUpdate();
            
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in projectRepository insertNewTaskIntoDb: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in projectRepository insertNewTaskIntoDb: " + e.getMessage());
            }
        }
    }
    
    public void insertRowIntoSubprojectsTasks(String dbName, int subprojectId, int taskId)
    {
        organisationConnection = generalRepository.establishConnection(dbName);
        
        try
        {
            String sqlCommand = "INSERT INTO subprojects_tasks (f_id_subproject, f_id_task) values (?, ?)";
            
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setInt(1, subprojectId);
            preparedStatement.setInt(2, taskId);
            
            preparedStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in insertRowIntoSubprojectsTasks: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in insertRowIntoSubprojectsTasks: " + e.getMessage());
            }
        }
    }
    
    // GEMT SUBTASK i db
    
    public boolean checkIfSubtaskTitleIsAvailable(String dbName, int taskId, String subtaskTitle)
    {
        // returVærdi sættes til at være true
        // vi ændrer den KUN til false, hvis vi ud af at det givne subproject HAR en task med taskTitle-værdien
        boolean subtaskTitleIsAvailable = true;
        
        organisationConnection = generalRepository.establishConnection(dbName);
        
        try
        {
            // find alt hvor taskId'et og subtaskTitel mathcer vores input
            String sqlCommand =
                    "SELECT * FROM tasks_subtasks " +
                            "RIGHT JOIN subtasks ON f_id_subtask = id_subtask " +
                            "WHERE f_id_task = ? AND subtask_title = ?";
            
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setInt(1, taskId);
            preparedStatement.setString(2, subtaskTitle);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            // hvis der der findes et task med titlen taskTitle under subproject'et der har id'et subprojectId
            if(resultSet.next())
            {
                // så er titlen IKKE available
                subtaskTitleIsAvailable = false;
            }
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in ProjectRepository checkIfSubtaskTitleIsAvailable: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in ProjectRepository checkIfSubtaskTitleIsAvailable: " + e.getMessage());
            }
        }
        
        return subtaskTitleIsAvailable;
        
    }
    
    public void insertNewSubtaskIntoDb(String dbName, String title, double manhours)
    {
        organisationConnection = generalRepository.establishConnection(dbName);
        
        try
        {
            String sqlCommand = "INSERT INTO subtasks (subtask_title, subtask_manhours) value (?,?)";
            
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setString(1, title);
            preparedStatement.setDouble(2, manhours);
            
            preparedStatement.executeUpdate();
            
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in projectRepository insertNewSubtaskIntoDb: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in projectRepository insertNewSubtaskIntoDb: " + e.getMessage());
            }
        }
    }
    
    public void insertRowIntoTasksSubtasks(String dbName, int taskId, int subtaskId)
    {
        organisationConnection = generalRepository.establishConnection(dbName);
        
        try
        {
            String sqlCommand = "INSERT INTO tasks_subtasks (f_id_task, f_id_subtask) values (?, ?)";
            
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setInt(1, taskId);
            preparedStatement.setInt(2, subtaskId);
            
            preparedStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in insertRowIntoTasksSubtasks: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in insertRowIntoTasksSubtasks: " + e.getMessage());
            }
        }
    }
    
    // GEM/SLET TEAM i db
    
    public void insertRowIntoTeamsProjects(String dbName, int teamId, int projectId)
    {
        organisationConnection = generalRepository.establishConnection(dbName);
        
        try
        {
            String sqlCommand = "INSERT INTO teams_projects (f_id_team, f_id_project) values (?, ?)";
            
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setInt(1, teamId);
            preparedStatement.setInt(2, projectId);
            
            preparedStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in ProjectRepository insertRowIntoTeamsProjects: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in ProjectRepository insertRowIntoTeamsProjectsFinally: " + e.getMessage());
            }
        }
        
    }
    
    public void deleteRowFromTeamsProjects(String dbName, int teamId, int projectId)
    {
        organisationConnection = generalRepository.establishConnection(dbName);
        
        try
        {
            String sqlCommand = "DELETE FROM teams_projects WHERE f_id_team = ? AND f_id_project = ?";
            
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setInt(1, teamId);
            preparedStatement.setInt(2, projectId);
            
            preparedStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in ProjectRepository deleteRowFromTeamsProjects: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in ProjectRepository deleteRowFromTeamsProjects:: " + e.getMessage());
            }
        }
        
        
    }
    
}
