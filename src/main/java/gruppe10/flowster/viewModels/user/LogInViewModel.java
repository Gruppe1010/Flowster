package gruppe10.flowster.viewModels.user;

public class LogInViewModel
{
    private String email;
    private String password;
    
    
    // constructors
    public LogInViewModel(String email, String password)
    {
        this.email = email;
        this.password = password;
    }
    
    
    // getters + setters
    public String getEmail()
    {
        return email;
    }
    public void setEmail(String username)
    {
        this.email = username;
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
