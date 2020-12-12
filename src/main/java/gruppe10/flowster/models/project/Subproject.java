package gruppe10.flowster.models.project;

import java.util.ArrayList;
import java.util.Date;

public class Subproject
{
    private int id;
    private String title;
    private Date deadline;
    private double manhours;
    private ArrayList<Task> taskList;
    
    // constructors
    public Subproject(){}
    
    public Subproject(int id, String title,  ArrayList<Task> taskList)
    {
        this.id = id;
        this.title = title;
        this.taskList = taskList;
    }
    
    public Subproject(int id, String title, Date deadline, double manhours, ArrayList<Task> taskList)
    {
        this.id = id;
        this.title = title;
        this.deadline = deadline;
        this.manhours = manhours;
        this.taskList = taskList;
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
    public ArrayList<Task> getTaskList()
    {
        return taskList;
    }
    public void setTaskList(ArrayList<Task> taskList)
    {
        this.taskList = taskList;
    }
}
