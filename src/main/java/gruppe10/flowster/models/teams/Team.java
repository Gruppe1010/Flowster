package gruppe10.flowster.models.teams;

import gruppe10.flowster.models.project.Project;
import gruppe10.flowster.models.users.User;
import gruppe10.flowster.services.TeamService;

import java.util.ArrayList;

public class Team
{
    private int id;
    private String name;
    private ArrayList<User> usersList;
    private ArrayList<Project> projectList;
    
    // constructors
    public Team(int id, String name)
    {
        this.id = id;
        this.name = name;
        this.usersList = null;
        this.projectList = null;
        
    }
    
    // getters + setters
    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public ArrayList<User> getUsersList()
    {
        return usersList;
    }
    public void setUsersList(ArrayList<User> usersList)
    {
        this.usersList = usersList;
    }
    public ArrayList<Project> getProjectList()
    {
        return projectList;
    }
    public void setProjectList(ArrayList<Project> projectList)
    {
        this.projectList = projectList;
    }
    
    // ANDRE metoder
    public boolean isOnProject(String orgDbName, int projectId)
    {
        TeamService teamService = new TeamService();
    
        return teamService.checkIfTeamIsOnProject(orgDbName, id, projectId);
    }

}
