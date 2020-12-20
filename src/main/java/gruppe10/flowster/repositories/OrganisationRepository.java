package gruppe10.flowster.repositories;

import gruppe10.flowster.models.users.ProjectManager;
import gruppe10.flowster.models.users.TeamMember;
import gruppe10.flowster.models.users.User;
import gruppe10.flowster.services.UserService;
import gruppe10.flowster.viewModels.user.LogInViewModel;
import org.springframework.stereotype.Repository;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.*;

@Repository
public class OrganisationRepository
{
    GeneralRepository generalRepository = new GeneralRepository();
    FlowsterRepository flowsterRepository = new FlowsterRepository();
    
    Connection organisationConnection;
    
    // ----------------- USER/+ subclasses
    
    /**
     * Indsætter nyt User-obj i organisationens user-tabel
     *
     *
     * @param newUser User-obj som skal tilføjes til db
     * @return void
     */
    public void insertUserIntoDb(User newUser)
    {
        String dbName = findDbNameFromOrganisationId(newUser.findOrganisationId());
        String email = newUser.getEmail();
        
        // FØRST indsættes email i emails-tabel i flowster-db
        flowsterRepository.insertEmailIntoEmails(email);
        
        // SÅ indsættes række i emails_organisations-tabel i flowster-db
        flowsterRepository.insertRowIntoEmailsAndOrganisations(newUser);
        
        // DERNÆST indsættes resten af userData i users-tabel i organisationName-db
        insertUserDataIntoUsers(dbName, newUser);
        
        // sæt id på loggedInUser
        UserService.loggedInUser.setId(retrieveUserIdFromEmail(dbName, email));
    }
    
    public int retrieveUserIdFromEmail(String dbName, String email)
    {
        int userId = 0;
        
        organisationConnection = generalRepository.establishConnection(dbName);
        
        try
        {
            String sqlCommand = "SELECT id_user FROM users LEFT JOIN flowster.emails ON f_id_email = id_email WHERE " +
                                        "email = ?";
            
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setString(1, email);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if(resultSet.next())
            {
                userId = resultSet.getInt("id_user");
            }
            
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in retrieveUserIdFromEmail: " + e.getMessage());
        }
        
        return userId;
    }
    
    public String findDbNameFromOrganisationId(int organisationId)
    {
        String organisationName =
                flowsterRepository.retrieveOrganisationNameFromOrganisationId(organisationId);
    
        // opretter ny String med underscore i stedet for mellemrum i organisationName
        return "flowster_" + organisationName.replaceAll(" ", "_");
    }
    
    
    /**
     * Indsætter et nyt User-obj i users-tabellen i dbName-databasen
     *
     * @param dbName navnet på db'en User-obj skal indsættes i
     * @param newUser det nye User-obj som skal indsættes i db
     * @return void
     * */
    public void insertUserDataIntoUsers(String dbName, User newUser)
    {
        // finder tilsvarende id'er til jopType + email - da de lagres i db som foreign keys m. reference til andre tab
        int jobTypeId = newUser.findJobTypeId();
        int emailId = flowsterRepository.retrieveEmailIdFromEmail(newUser.getEmail());
        
        organisationConnection = generalRepository.establishConnection(dbName);
        
        // lægge ALT data på newUser ned
        try
        {
            Blob profilePictureBlob = new SerialBlob(newUser.getProfilePictureBytes());
            
            String sqlCommand = "INSERT INTO users (f_id_job_type, f_id_email, firstname, surname, password, " +
                                        "manhours, profile_picture) values(?, ?, ?, ?, ?, ?, ?)";
    
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setInt(1, jobTypeId);
            preparedStatement.setInt(2, emailId);
            preparedStatement.setString(3, newUser.getFirstname());
            preparedStatement.setString(4, newUser.getSurname());
            preparedStatement.setString(5, newUser.getPassword());
            preparedStatement.setDouble(6, newUser.getManhours());
            preparedStatement.setBlob(7, profilePictureBlob);
        
            preparedStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in insertUserDataIntoOrganisationDb: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in insertUserDataIntoOrganisationDbFinally: " + e.getMessage());
            }
        }
    }
    
    /**
     * Henter User fra organisation-db (dbName-db) ud fra logInViewModel's email og password
     * Kalder createUserFromResultSet - som kalder createProjectManagerFromResultSet og createTeamMemberFromResultSet
     *
     * @param logInViewModel model vi har gemt email og password på
     * @param dbName databasen vi skal ned i
     * @return User User-obj hentet i db
     * */
    public User retrieveUserFromDb(LogInViewModel logInViewModel, String dbName)
    {
        User user = null;
        
        organisationConnection = generalRepository.establishConnection(dbName);
        
        try
        {
            
            int emailId = flowsterRepository.retrieveEmailIdFromEmail(logInViewModel.getEmail());
            
            String sqlCommand = "SELECT * FROM users WHERE f_id_email = ? and password = ?";
            
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setInt(1, emailId);
            preparedStatement.setString(2, logInViewModel.getPassword());
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            user = createUserFromResultSet(resultSet);
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in retrieveUserFromDb: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in retrieveUserFromDbFinally: " + e.getMessage());
            }
        }
        
        return user;
    }
    
    /**
     * Opretter nyt User-obj som afhængigt af jobType enten tildeles ProjectManager eller TeamMember-obj - ud fra
     * resultSet
     *
     * @param resultSet ResultSet som User-obj oprettes ud fra
     * @return User Nyoprettede User-obj
     * */
    public User createUserFromResultSet(ResultSet resultSet)
    {
        User user = null;
        
        try
        {
            if(resultSet.next())
            {
                // foreløbige variabler:
                int jobType = resultSet.getInt("f_id_job_type");
                int emailId = resultSet.getInt("f_id_email");
                int organisationId = flowsterRepository.retrieveOrganisationIdFromEmailId(emailId);
                String dbName = findDbNameFromOrganisationId(organisationId);
                String organisationAndJobTypeString = "" + organisationId + jobType;
                
                // variabler som bruges til at oprette User-obj. med
                int organisationAndJobType = Integer.parseInt(organisationAndJobTypeString);
                String email = flowsterRepository.retrieveEmailFromEmailId(emailId);
                byte[] profilePictureBytes = convertBlobToByteArray(resultSet.getBlob("profile_picture"));
                
                int id = resultSet.getInt("id_user");
                
                user = new User(id,
                        organisationAndJobType,
                        resultSet.getString("firstname"),
                        resultSet.getString("surname"),
                        email,
                        resultSet.getString("password"),
                        resultSet.getDouble("manhours"),
                        profilePictureBytes, null);
                
                // hvis det er en ProjectManager
                if(jobType == 1)
                {
                    // omdan til ProjectManager-obj
                    user = convertUserToProjectManager(user);
                }
                // hvis det er en TeamMember
                else if(jobType == 2)
                {
                    // omdan til TeamMember-obj
                    user = convertUserToTeamMember(user);
                }
            }
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in createUserFromResultSet: " + e.getMessage());
        }
        
        return user;
    }
    
    /**
     * Konverterer User-obj til ProjectManager-obj
     *
     * @param user User-obj som skal konverteres
     * @return ProjectManager nyoprettede ProjectManager-obj. udfra User-obj
     * */
    public ProjectManager convertUserToProjectManager(User user)
    {
       return new ProjectManager(user.getId(), user.getOrganisationAndJobType(),
                user.getFirstname(), user.getSurname(), user.getEmail(), user.getPassword(),
                user.getManhours(), user.getProfilePictureBytes(), user.getJoinedTeamList());
        
    }
    
    /**
     * Konverterer User-obj til TeamMember-obj
     *
     * @param user User-obj som skal konverteres
     * @return TeamMember nyoprettede TeamMember-obj. udfra User-obj
     * */
    public TeamMember convertUserToTeamMember(User user)
    {
        return new TeamMember(user.getId(), user.getOrganisationAndJobType(),
                user.getFirstname(), user.getSurname(), user.getEmail(), user.getPassword(),
                user.getManhours(), user.getProfilePictureBytes(), user.getJoinedTeamList());
    }
    
    // ----------------- ANDRE
    
    /**
     * Konverterer blob til et byteArray
     *
     * @param blob Blob-obj som skal konverteres
     * @return byte[] konverterede blob
     * */
    public byte[] convertBlobToByteArray(Blob blob)
    {
        // byte[0] == null-value til byte[]
        byte[] profilePictureBytes = new byte[0];
        
        if(blob != null)
        {
            try
            {
                profilePictureBytes = blob.getBytes(1, (int) blob.length());
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in convertBlobToByteArray: " + e.getMessage());
            }
        }
        
        return profilePictureBytes;
    }
}
