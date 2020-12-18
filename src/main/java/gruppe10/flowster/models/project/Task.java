package gruppe10.flowster.models.project;

import java.util.ArrayList;

public class Task
{
    private int id;
    private String title;
    private double manhours;
    private ArrayList<Subtask> subtaskList;
    
    // constructors
    public Task(){}
    
    public Task(int id, String title)
    {
        this.id = id;
        this.title = title;
        this.subtaskList = null;
    }
 
    public Task(int id, String title, double manhours, ArrayList<Subtask> subtaskList)
    {
        this.id = id;
        this.title = title;
        this.manhours = manhours;
        this.subtaskList = subtaskList;
    }
    
    // getters + setters
    public double getManhours()
    {
        return manhours;
    }
    public void setManhours(double manhours)
    {
        this.manhours = manhours;
    }
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
    public ArrayList<Subtask> getSubtaskList()
    {
        return subtaskList;
    }
    public void setSubtaskList(ArrayList<Subtask> subtaskList)
    {
        this.subtaskList = subtaskList;
    }
}
