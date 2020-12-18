package gruppe10.flowster.models.teams;

import gruppe10.flowster.models.project.Project;
import gruppe10.flowster.models.users.User;

import java.util.ArrayList;

public class Team
{
    private int id;
    private String name;
    private ArrayList<User> usersList;
    private ArrayList<Project> projectList;
    
    
    // constructors
    public Team(){}
    
    // TODO: slet muligvis denne senere hvis den ikke bliver brugt
    public Team(int id, String name)
    {
        this.id = id;
        this.name = name;
        this.usersList = null;
        this.projectList = null;
        
    }
    
    public Team(String name, ArrayList<User> usersList, ArrayList<Project> projectList)
    {
        this.name = name;
        this.usersList = usersList;
        this.projectList = projectList;
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
    
    
    public boolean isOnProject()
    {
        return true;
    }
 
    // TODO
    public boolean isOnProject(int projectId)
    {
        
        // int projectIdInt = Integer.parseInt(projectId);
        
        boolean teamIsOnProject = false;
        
        for(Project project : projectList)
        {
            if(projectId == project.getId())
            {
                teamIsOnProject = true;
                
                break;
            }
        }
        return teamIsOnProject;
    }
    
    
    
    
}
