package gruppe10.flowster.models.users;

import gruppe10.flowster.UserData;
import gruppe10.flowster.models.project.Project;
import gruppe10.flowster.models.teams.Team;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class User implements UserData, Comparable<User>
{
    // 00000 = første 3 nuller = organisationskode, 2 nuller = stillingskode
    // 01 == projectManager
    // 02 == employee
    private int id;
    private int organisationAndJobType;
    private String firstname;
    private String surname;
    private String email;
    private String password;
    private double manhours;
    private byte[] profilePictureBytes;
    private String base64;
    private ArrayList<Team> joinedTeamList;
    private ArrayList<Project> joinedProjectList;

    
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
        this.profilePictureBytes = createGenericProfilePictureBytes();
        this.base64 = byteArrayAs64String();
        this.joinedTeamList = null;
    }
    public User(int id, int organisationAndJobType, String firstname, String surname, String email, String password,
                double manhours, byte[] profilePictureBytes, ArrayList<Team> joinedTeamList)
    {
        this.id = id;
        this.organisationAndJobType = organisationAndJobType;
        this.firstname = firstname;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.manhours = manhours;
        this.profilePictureBytes = profilePictureBytes;
        this.base64 = byteArrayAs64String();
        this.joinedTeamList = joinedTeamList;
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
    public ArrayList<Team> getJoinedTeamList()
    {
        return this.joinedTeamList;
    }
    public ArrayList<Project> getJoinedProjectList()
    {
        return joinedProjectList;
    }
    public void setJoinedProjectList(ArrayList<Project> joinedProjectList)
    {
        this.joinedProjectList = joinedProjectList;
    }

    public void setJoinedTeamList(ArrayList<Team> joinedTeamList)

    {
        this.joinedTeamList = joinedTeamList;
    }
    // UserData-interface
    
    @Override
    /**
     * Finder organisationsId ved at adskille de første 3 cifre fra organisationAndJobType-attributten
     *
     * @return int fundne organisationId
     * */
    public int findOrganisationId()
    {
        String organisationAndJobTypeString = Integer.toString(organisationAndJobType);
        
        return Integer.parseInt(organisationAndJobTypeString.substring(0,3));
    }
 
    
    @Override
    /**
     * Finder jobTypeId ved at adskille de sidste 2 cifre fra organisationAndJobType-attributten
     *
     * @return int fundne jobTypeId
     * */
    public int findJobTypeId()
    {
        String organisationAndJobTypeString = Integer.toString(organisationAndJobType);
    
        return Integer.parseInt(organisationAndJobTypeString.substring(3,5));
    }
    
    // Comparable-interface
    @Override
    public int compareTo(User user)
    {
        //if(user.isTennisBall() && !isTennisBall())
        if(isProjectManager() && user.isTeamMember())
        {
            return 1; //hvis this.obj > param.obj
        }
        else if(isTeamMember() && user.isProjectManager())
        {
            return -1; // hvis this.obj < param.obj
        }
        else if(isProjectManager() && user.isProjectManager())
        {
            return 0; // this.obj == param.obj
        }
        
   
        // else hvis de begge er TeamMember's
        return 0; // this.obj == param.obj
    }
    
    
    // ANDRE METODER
    public boolean isProjectManager()
    {
        return false;
    }
    public boolean isTeamMember()
    {
        return false;
    }
    
    public String getClassName()
    {
        return getClass().getSimpleName();
    }
    
    public String findClassNameUrlPath()
    {
        String urlPath = getClass().getSimpleName();
    
        urlPath = Character.toLowerCase(urlPath.charAt(0)) + urlPath.substring(1);
        
        return urlPath;
    }
    
    @Override
    public String toString()
    {
        return "User{" +
                       "id=" + id +
                       ", organisationAndJobType=" + organisationAndJobType +
                       ", firstname='" + firstname + '\'' +
                       ", surname='" + surname + '\'' +
                       ", email='" + email + '\'' +
                       ", password='" + password + '\'' +
                       ", manhours=" + manhours +
                       ", joinedTeamsList=" + joinedTeamList +
                       '}';
    }
    
    // TODO
    /**
     * Opretter et byte[] ud fra genericProfilePicture (det som tildeles automatisk ved oprettelse af user)
     *
     * @return byte[] ByteArray'et der indeholder genericProfilePicture
     */
    public byte[] createGenericProfilePictureBytes()
    {
        byte[] genericProfilePictureBytes = new byte[0];
        
        try
        {
            
            //File.seperator gør at stien virker på alle systemer
            File genericProfilePictureFile = new File("src" +File.separator + "main" + File.separator + "resources" + File.separator +
                                                              "static" + File.separator + "images" + File.separator + "generic-profile-picture.png");
            
            genericProfilePictureBytes = Files.readAllBytes(genericProfilePictureFile.toPath());
        }
        catch(IOException e)
        {
            System.err.println("Error in createGenericProfilePictureBytes: " + e.getMessage());
        }
        
        return genericProfilePictureBytes;
    }
    
    private String byteArrayAs64String()
    {
        return Base64.encodeBase64String(this.profilePictureBytes);
    }
    
    
    
}
