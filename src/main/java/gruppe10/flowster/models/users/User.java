package gruppe10.flowster.models.users;

import gruppe10.flowster.UserData;

public class User implements UserData
{
    // 00000 = første 3 nuller = organisationskode, 2 nuller = stillingskode
    // 01 == projectManager
    // 02 == employee
    private int organisationAndJobType;
    private String firstname;
    private String surname;
    private String email;
    private String password;
    private double manhours;
    private byte[] profilePictureBytes;
    
    // constructors
    
    public User() {}
    
    public User(int organisationAndJobType, String firstname, String surname, String email, String password,
                double manhours)
    {
        this.organisationAndJobType = organisationAndJobType;
        this.firstname = firstname;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.manhours = manhours;
        this.profilePictureBytes = null;
    }
    public User(int organisationAndJobType, String firstname, String surname, String email, String password,
                double manhours, byte[] profilePictureBytes)
    {
        this.organisationAndJobType = organisationAndJobType;
        this.firstname = firstname;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.manhours = manhours;
        this.profilePictureBytes = profilePictureBytes;
    }
    
    // getters + setters
    public int getOrganisationAndJobType()
    {
        return organisationAndJobType;
    }
    public void setOrganisationAndJobType(int organisationAndJobType)
    {
        this.organisationAndJobType = organisationAndJobType;
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
    
    // METODER FRA UserData-interface
    /**
     * Finder organisationsId ved at adskille de første 3 cifre fra organisationAndJobType-attributten
     *
     *
     * @return int fundne organisationId
     * */
    public int findOrganisationIdFromOrganisationAndJopType()
    {
        String organisationAndJobTypeString = Integer.toString(organisationAndJobType);
        
        return Integer.parseInt(organisationAndJobTypeString.substring(0,3));
    }
    
    /**
     * Finder jobTypeId ved at adskille de sidste 2 cifre fra organisationAndJobType-attributten
     *
     *
     * @return int fundne jobTypeId
     * */
    public int findJobTypeIdFromOrganisationAndJopType()
    {
        String organisationAndJobTypeString = Integer.toString(organisationAndJobType);
    
        return Integer.parseInt(organisationAndJobTypeString.substring(3,5));
    }
    
    // ANDRE METODER
    
}
