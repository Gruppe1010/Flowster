package gruppe10.flowster.viewModels;

public class ViewProfileViewModel
{
    private String name;
    private String jobType;
    private double manhours;
    private String email;
    private byte[] profilePictureBytes;
    private String profilePictureInitials;
    
    
    // constructors
    
    public ViewProfileViewModel(){}
    
    public ViewProfileViewModel(String name, String jobType, double manhours, byte[] profilePictureBytes, String profilePictureInitials)
    {
        this.name = name;
        this.jobType = jobType;
        this.manhours = manhours;
        this.profilePictureBytes = profilePictureBytes;
        this.profilePictureInitials = profilePictureInitials;
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
    public String getJobType()
    {
        return jobType;
    }
    public void setJobType(String jobType)
    {
        this.jobType = jobType;
    }
    public double getManhours()
    {
        return manhours;
    }
    public void setManhours(double manhours)
    {
        this.manhours = manhours;
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
}

