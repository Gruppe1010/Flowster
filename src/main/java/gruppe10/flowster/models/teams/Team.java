package gruppe10.flowster.models.teams;

import gruppe10.flowster.models.project.Project;
import gruppe10.flowster.models.users.User;

import java.util.ArrayList;

public class Team
{
    private int id;
    private String name;
    private ArrayList<User> employeesList;
    private ArrayList<Project> projectsList;
    
    
    // constructors
    public Team(){}
    
    // TODO: slet muligvis denne senere hvis den ikke bliver brugt
    public Team(int id, String name)
    {
        this.id = id;
        this.name = name;
        this.employeesList = null;
        this.projectsList = null;
        
    }
    
    public Team(String name, ArrayList<User> employeesList, ArrayList<Project> projectsList)
    {
        this.name = name;
        this.employeesList = employeesList;
        this.projectsList = projectsList;
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
    public ArrayList<User> getEmployeesList()
    {
        return employeesList;
    }
    public void setEmployeesList(ArrayList<User> employeesList)
    {
        this.employeesList = employeesList;
    }
    public ArrayList<Project> getProjectsList()
    {
        return projectsList;
    }
    public void setProjectsList(ArrayList<Project> projectsList)
    {
        this.projectsList = projectsList;
    }
}
