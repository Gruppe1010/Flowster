package gruppe10.flowster.viewModels.project;

public class CreateSubtaskViewModel
{
    private int id;
    private String title;
    
    // constructors
    public CreateSubtaskViewModel(String title)
    {
        this.title = title;
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
}
