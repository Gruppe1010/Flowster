package gruppe10.flowster.models.teams;

import gruppe10.flowster.models.project.Project;
import gruppe10.flowster.models.users.TeamMember;

import java.util.ArrayList;
import java.util.HashSet;

public class Team
{
    private String name;
    private HashSet<TeamMember> employeesSet;
    private ArrayList<Project> projectsList;
    
    
    // constructors
    public Team(){}
    
    public Team(String name, HashSet<TeamMember> employeesSet, ArrayList<Project> projectsList)
    {
        this.name = name;
        this.employeesSet = employeesSet;
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
    public HashSet<TeamMember> getEmployeesSet()
    {
        return employeesSet;
    }
    public void setEmployeesSet(HashSet<TeamMember> employeesSet)
    {
        this.employeesSet = employeesSet;
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
