package gruppe10.flowster.viewModels;



/**
 *
 * Denne klasse holder på data som skal vises i createUserForm som vises på index-html
 *
 * */

public class CreateUserModel
{
    private String organisationAndJobType;
    private double manhours;
    private String firstname;
    private String surname;
    private String email;
    private String password;
    private String confirmPassword;
    
    
    // constructors
    public CreateUserModel(){}
    
    public CreateUserModel(String organisationAndJobType, double manhours, String firstname, String surname,
                           String email,
                           String password, String confirmPassword)
    {
        this.organisationAndJobType = organisationAndJobType;
        this.manhours = manhours;
        this.firstname = firstname;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
    
    
    // getters + setters
    public String getOrganisationAndJobType()
    {
        return organisationAndJobType;
    }
    public void setOrganisationAndJobType(String organisationAndJobType)
    {
        this.organisationAndJobType = organisationAndJobType;
    }
    public double getManhours()
    {
        return manhours;
    }
    public void setManhours(double manhours)
    {
        this.manhours = manhours;
    }
    public String getFirstname()
    {
        return firstname;
    }
    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }
    public String getSurname()
    {
        return surname;
    }
    public void setSurname(String surname)
    {
        this.surname = surname;
    }
    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public String getPassword()
    {
        return password;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }
    public String getConfirmPassword()
    {
        return confirmPassword;
    }
    public void setConfirmPassword(String confirmPassword)
    {
        this.confirmPassword = confirmPassword;
    }
}
