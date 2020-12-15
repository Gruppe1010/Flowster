package gruppe10.flowster.models.project;

import java.util.ArrayList;
import java.util.Date;

public class Subtask
{
    private int id;
    private String title;
    
    // constructors
    public Subtask(){}
    
    public Subtask(int id, String title)
    {
        this.id = id;
        this.title = title;
    }
    
    // getters + getters
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
    
    
    @Override
    public String toString()
    {
        return "Subtask{" +
                       "id=" + id +
                       ", title='" + title + '\'' +
                       '}';
    }
}

