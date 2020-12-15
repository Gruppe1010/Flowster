package gruppe10.flowster.viewModels.project;

public class CreateTaskViewModel
{
    private int id;
    private String title;


    // constructors
    
    
    public CreateTaskViewModel(String title)
    {
        this.title = title;
    }
    
    public CreateTaskViewModel(int id, String title)
    {
        this.id = id;
        this.title = title;
    }
    
    
    // getters + setters
    
}
