package gruppe10.flowster.viewModels;

public class EditProfileModel
{
    private String name;
    private double manhours;
    private String email;
    private byte[] profilePictureBytes;
    private String profilePictureInitials;
    private String password;
    private String confirmPassword;
    
    // constructors
    
    public EditProfileModel(){}
    
    public EditProfileModel(String name, double manhours, String email, byte[] profilePictureBytes, String profilePictureInitials, String password, String confirmPassword)
    {
        this.name = name;
        this.manhours = manhours;
        this.email = email;
        this.profilePictureBytes = profilePictureBytes;
        this.profilePictureInitials = profilePictureInitials;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
    
    // getters + setters
    
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public double getManhours()
    {
        return manhours;
    }
    public void setManhours(double manhours)
    {
        this.manhours = manhours;
    }
    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public byte[] getProfilePictureBytes()
    {
        return profilePictureBytes;
    }
    public void setProfilePictureBytes(byte[] profilePictureBytes)
    {
        this.profilePictureBytes = profilePictureBytes;
    }
    public String getProfilePictureInitials()
    {
        return profilePictureInitials;
    }
    public void setProfilePictureInitials(String profilePictureInitials)
    {
        this.profilePictureInitials = profilePictureInitials;
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
