package gruppe10.flowster.models.project;

import java.util.ArrayList;
import java.util.Date;

public class Subproject
{
    private String title;
    private Date deadline;
    private double manhours;
    private ArrayList<Task> tasksList;
    
    // constructors
    public Subproject(){}
    
    public Subproject(String title, Date deadline, double manhours, ArrayList<Task> tasksList)
    {
        this.title = title;
        this.deadline = deadline;
        this.manhours = manhours;
        this.tasksList = tasksList;
    }
    
    // getters + setters
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
    public ArrayList<Task> getTasksList()
    {
        return tasksList;
    }
    public void setTasksList(ArrayList<Task> tasksList)
    {
        this.tasksList = tasksList;
    }
}
