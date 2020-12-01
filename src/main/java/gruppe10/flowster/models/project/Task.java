package gruppe10.flowster.models.project;

import java.util.ArrayList;
import java.util.Date;

public class Task
{
    private String title;
    private ArrayList<Subtask> subtasksList;
    
    // constructors
    public Task(){}
    
    public Task(String title, ArrayList<Subtask> subtasksList)
    {
        this.title = title;
        this.subtasksList = subtasksList;
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
    public ArrayList<Subtask> getSubtasksList()
    {
        return subtasksList;
    }
    public void setSubtasksList(ArrayList<Subtask> subtasksList)
    {
        this.subtasksList = subtasksList;
    }
}
