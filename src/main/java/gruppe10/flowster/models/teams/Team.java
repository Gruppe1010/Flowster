package gruppe10.flowster.models.teams;

import gruppe10.flowster.models.project.Project;
import gruppe10.flowster.models.users.TeamMember;

import java.util.ArrayList;
import java.util.HashSet;

public class Team
{
    private String name;
    private ArrayList<TeamMember> employeesList;
    private ArrayList<Project> projectsList;
    
    
    // constructors
    public Team(){}
    
    // TODO: slet muligvis denne senere hvis den ikke bliver brugt
    public Team(String name)
    {
        this.name = name;
        this.employeesList = null;
        this.projectsList = null;
        
    }
    
    public Team(String name, ArrayList<TeamMember> employeesList, ArrayList<Project> projectsList)
    {
        this.name = name;
        this.employeesList = employeesList;
        this.projectsList = projectsList;
    }
    
    
    // getters + setters
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public ArrayList<TeamMember> getEmployeesList()
    {
        return employeesList;
    }
    public void setEmployeesList(ArrayList<TeamMember> employeesList)
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
