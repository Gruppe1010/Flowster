package gruppe10.flowster.models.project;

import gruppe10.flowster.models.teams.Team;
import gruppe10.flowster.models.users.ProjectManager;

import java.util.ArrayList;
import java.util.Date;

public class Project
{
    private int id;
    private String title;
    private Date deadline;
    private double manhours;
    private ArrayList<Subproject> subprojectList;
    private ArrayList<ProjectManager> projectManagersSet;
    private ArrayList<Team> teamsSet;
    
    // constructors
    public Project(){}
    
    public Project(String title, Date deadline, double manhours, ArrayList<Subproject> subprojectList, ProjectManager
                   projectManagerCreater)
    {
        this.title = title;
        this.deadline = deadline;
        this.manhours = manhours;
        this.subprojectList = subprojectList;
        this.projectManagersSet = new ArrayList<>();
        projectManagersSet.add(projectManagerCreater);
        this.teamsSet = new ArrayList<>();
    }
    public Project(int id, String title, Date deadline, double manhours, ArrayList<Subproject> subprojectList,
                   ProjectManager projectManagerCreater)
    {
        this.id = id;
        this.title = title;
        this.deadline = deadline;
        this.manhours = manhours;
        this.subprojectList = subprojectList;
        this.projectManagersSet = new ArrayList<>();
        projectManagersSet.add(projectManagerCreater);
        this.teamsSet = new ArrayList<>();
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
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    public Date getDeadline()
    {
        return deadline;
    }
    public void setDeadline(Date deadline)
    {
        this.deadline = deadline;
    }
    public double getManhours()
    {
        return manhours;
    }
    public void setManhours(double manhours)
    {
        this.manhours = manhours;
    }
    public ArrayList<Subproject> getSubprojectList()
    {
        return subprojectList;
    }
    public void setSubprojectList(ArrayList<Subproject> subprojectList)
    {
        this.subprojectList = subprojectList;
    }
    public ArrayList<ProjectManager> getProjectManagersSet()
    {
        return projectManagersSet;
    }
    public void setProjectManagersSet(ArrayList<ProjectManager> projectManagersSet)
    {
        this.projectManagersSet = projectManagersSet;
    }
    public ArrayList<Team> getTeamsSet()
    {
        return teamsSet;
    }
    public void setTeamsSet(ArrayList<Team> teamsSet)
    {
        this.teamsSet = teamsSet;
    }
}
