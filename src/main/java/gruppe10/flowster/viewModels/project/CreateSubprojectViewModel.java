package gruppe10.flowster.viewModels.project;

public class CreateSubprojectViewModel
{
    private int id;
    private String title;
    private double manhours;
    
    // constructors
    public CreateSubprojectViewModel(){}
    public CreateSubprojectViewModel(String title, double manhours)
    {
        this.title = title;
        this.manhours = manhours;
    }
    public CreateSubprojectViewModel(int id, String title, double manhours)
    {
        this.id = id;
        this.title = title;
        this.manhours = manhours;
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
    public double getManhours()
    {
        return manhours;
    }
    public void setManhours(double manhours)
    {
        this.manhours = manhours;
    }
}
