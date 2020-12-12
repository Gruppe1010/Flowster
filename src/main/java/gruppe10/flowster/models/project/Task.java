package gruppe10.flowster.models.project;

import java.util.ArrayList;

public class Task
{
    private int id;
    private String title;
    private ArrayList<Subtask> subtaskList;
    
    // constructors
    public Task(){}
    
    public Task(int id, String title)
    {
        this.id = id;
        this.title = title;
        this.subtaskList = null;
    }
 
    public Task(int id, String title, ArrayList<Subtask> subtaskList)
    {
        this.id = id;
        this.title = title;
        this.subtaskList = subtaskList;
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
    public ArrayList<Subtask> getSubtaskList()
    {
        return subtaskList;
    }
    public void setSubtaskList(ArrayList<Subtask> subtaskList)
    {
        this.subtaskList = subtaskList;
    }
}
