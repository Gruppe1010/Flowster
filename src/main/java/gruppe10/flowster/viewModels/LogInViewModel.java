package gruppe10.flowster.viewModels;

public class LogInViewModel
{
    private String username;
    private String password;
    
    
    // constructors
    public LogInViewModel(){}
    
    public LogInViewModel(String username, String password)
    {
        this.username = username;
        this.password = password;
    }
    
    
    // getters + setters
    public String getUsername()
    {
        return username;
    }
    public void setUsername(String username)
    {
        this.username = username;
    }
    public String getPassword()
    {
        return password;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }
}
