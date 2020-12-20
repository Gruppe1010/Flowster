package gruppe10.flowster.models.project;

import java.util.ArrayList;
import java.util.Date;

public class Subtask
{
    private int id;
    private String title;
    private double manhours;
    
    // constructors
    public Subtask(int id, String title, double manhours)
    {
        this.id = id;
        this.title = title;
        this.manhours = manhours;
    }
    
    // getters + getters
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
    
    
    @Override
    public String toString()
    {
        return "Subtask{" +
                       "id=" + id +
                       ", title='" + title + '\'' +
                       '}';
    }
}

