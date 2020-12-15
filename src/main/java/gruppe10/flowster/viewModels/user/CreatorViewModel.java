package gruppe10.flowster.viewModels.user;

public class CreatorViewModel
{
    private int id;
    private String name;
    
    // constructor
    public CreatorViewModel(int id, String name)
    {
        this.id = id;
        this.name = name;
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
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
}
