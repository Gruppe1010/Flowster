package gruppe10.flowster.models.project;

import java.util.ArrayList;
import java.util.Date;

public class Subtask
{
    private String title;
    
    // constructors
    public Subtask(){}
    
    public Subtask(String title)
    {
        this.title = title;
    }
    
    // getters + getters
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
}
