package gruppe10.flowster.viewModels.user;

import org.apache.tomcat.util.codec.binary.Base64;

public class PreviewUserViewModel
{
    private int id;
    private byte[] profilePictureBytes;
    private String base64;
    private String name;
    private String jobType;
    private boolean isOnTeam;
    
    
    // constructors
    public PreviewUserViewModel(int id, byte[] profilePictureBytes, String name, String jobType)
    {
        this.id = id;
        this.profilePictureBytes = profilePictureBytes;
        this.base64 = byteArrayAs64String();
        this.name = name;
        this.jobType = jobType;
        this.isOnTeam = false;
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
    public byte[] getProfilePictureBytes()
    {
        return profilePictureBytes;
    }
    public void setProfilePictureBytes(byte[] profilePictureBytes)
    {
        this.profilePictureBytes = profilePictureBytes;
    }
    public String getBase64()
    {
        return base64;
    }
    public void setBase64(String base64)
    {
        this.base64 = base64;
    }
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
    public boolean isOnTeam()
    {
        return isOnTeam;
    }
    public void setOnTeam(boolean onTeam)
    {
        isOnTeam = onTeam;
    }
    
    // andre metoder
    private String byteArrayAs64String()
    {
        return Base64.encodeBase64String(this.profilePictureBytes);
    }
}
