package gruppe10.flowster.viewModels.project;

public class CreateProjectViewModel
{
    private String title;
    private String deadline;
    
    // constructors
    public CreateProjectViewModel(String title, String deadline)
    {
        this.title = title;
        this.deadline = deadline;
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
    public String getDeadline()
    {
        return deadline;
    }
    public void setDeadline(String deadline)
    {
        this.deadline = deadline;
    }
    
}
