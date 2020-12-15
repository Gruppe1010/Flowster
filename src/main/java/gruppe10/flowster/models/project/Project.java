package gruppe10.flowster.models.project;

import gruppe10.flowster.models.teams.Team;
import gruppe10.flowster.models.users.ProjectManager;
import gruppe10.flowster.models.users.User;

import java.util.ArrayList;
import java.util.Date;

public class Project
{
    private int id;
    private String title;
    private String description;
    private Date deadline;
    private double manhours;
    private User creator;
    private ArrayList<Subproject> subprojectList;
    private ArrayList<Team> teamList;
    
    // constructors
    public Project(){}
    
    
    
    public Project(int id, String title, String description, Date deadline, double manhours, User creator,
                   ArrayList<Subproject> subprojectList, ArrayList<Team> teamList)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.manhours = manhours;
        this.creator = creator;
        this.subprojectList = subprojectList;
        this.teamList = teamList;
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
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
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
    public User getCreator()
    {
        return creator;
    }
    public void setCreator(User creator)
    {
        this.creator = creator;
    }
    public ArrayList<Subproject> getSubprojectList()
    {
        return subprojectList;
    }
    public void setSubprojectList(ArrayList<Subproject> subprojectList)
    {
        this.subprojectList = subprojectList;
    }
    public ArrayList<Team> getTeamList()
    {
        return teamList;
    }
    public void setTeamList(ArrayList<Team> teamList)
    {
        this.teamList = teamList;
    }
}
