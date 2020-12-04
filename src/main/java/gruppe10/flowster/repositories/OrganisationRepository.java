package gruppe10.flowster.repositories;

import gruppe10.flowster.models.users.ProjectManager;
import gruppe10.flowster.models.users.TeamMember;
import gruppe10.flowster.models.users.User;
import gruppe10.flowster.viewModels.LogInViewModel;

import java.sql.*;

public class OrganisationRepository
{
    
    GeneralRepository generalRepository = new GeneralRepository();
    FlowsterRepository flowsterRepository = new FlowsterRepository();
    
    Connection organisationConnection;
    
    
    /**
     * Indsætter nyt User-obj i organisationens user-tabel
     *
     *
     * @param newUser User-obj som skal tilføjes til db
     * @return void
     */
    public void insertUserIntoDb(User newUser)
    {
        // finder organisationName ud fra organisationId
        String organisationName = flowsterRepository.retrieveOrganisationNameFromOrganisationId
                                           (newUser.findOrganisationId());
        
        // opretter ny String med underscore i stedet for mellemrum i organisationName
        String dbName = "flowster_" + organisationName.replaceAll(" ", "_");
        
        // FØRST indsættes email i emails-tabel i flowster-db
        flowsterRepository.insertEmailIntoEmails(newUser.getEmail());
        
        // SÅ indsættes række i emails_organisations-tabel i flowster-db
        flowsterRepository.insertRowIntoEmailsAndOrganisations(newUser);
        
        // DERNÆST indsættes resten af userData i users-tabel i organisationName-db
        insertUserDataIntoUsers(dbName, newUser);
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
            String sqlCommand = "INSERT INTO users (f_id_job_type, f_id_email, firstname, surname, password, " +
                                        "manhours) values(?, ?, ?, ?, ?, ?)";
    
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setInt(1, jobTypeId);
            preparedStatement.setInt(2, emailId);
            preparedStatement.setString(3, newUser.getFirstname());
            preparedStatement.setString(4, newUser.getSurname());
            preparedStatement.setString(5, newUser.getPassword());
            preparedStatement.setDouble(6, newUser.getManhours());
        
            preparedStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            System.out.println("ERROR in insertUserDataIntoOrganisationDb: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.out.println("ERROR in insertUserDataIntoOrganisationDbFinally: " + e.getMessage());
            }
        }
    }
    
    public User retrieveUserFromDb(LogInViewModel logInViewModel, String dbName)
    {
        User user = null;
        
        organisationConnection = generalRepository.establishConnection(dbName);
        
        try
        {
            String sqlCommand = "SELECT * FROM users WHERE email = ? and password = ?";
            
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setString(1, logInViewModel.getEmail());
            preparedStatement.setString(2, logInViewModel.getPassword());
    
    
            ResultSet resultSet = preparedStatement.executeQuery();
            
            
            user = createUserFromResultSet(resultSet);
            
            
            
        }
        catch(SQLException e)
        {
            System.out.println("ERROR in retrieveUserFromDb: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.out.println("ERROR in retrieveUserFromDbFinally: " + e.getMessage());
            }
        }
        
        
        //
    
    
        return user;
    }
    
    
    
    
    // TODO: kald 2 metoder herinde: createProjecTManagerFromResultSet
    public User createUserFromResultSet(ResultSet resultSet)
    {
        User userToReturn = null;
        
        try
        {
            if(resultSet.next())
            {
                // find email ud fra emailId
                int emailId = resultSet.getInt("f_id_email");
                String email = flowsterRepository.retrieveEmailFromEmailId(emailId);
    
    
                // find organisationAndJobType
                int jobType = resultSet.getInt("f_id_job_type")
                
                String organisationAndJobTypeString = Integer.toString(
                        flowsterRepository.retrieveOrganisationIdFromEmailId(emailId) + jobType);
                
                int organisationAndJobType = Integer.parseInt(organisationAndJobTypeString);

                // find profilePictureBytes
                byte[] profilePictureBytes = convertBlobToByteArray(resultSet.getBlob("profile_picture"));

                User user = new User(organisationAndJobType,
                        resultSet.getString("firstname"),
                        resultSet.getString("surname"),
                        email,
                        resultSet.getString("password"),
                        resultSet.getDouble("manhours"),
                        profilePictureBytes);
                
                // TODO: find ud af om den brokker sig bare for at brokke sig
                if(jobType == 1)
                {
                    userToReturn = (ProjectManager) user;
                }
                else if(jobType == 2)
                {
                    userToReturn = (TeamMember) user;
                }
                
            }

        }
        catch(SQLException e)
        {
            System.out.println("ERROR in createUserFromResultSet: " + e.getMessage());
        }
        
        
        return userToReturn;
    }
    
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
                System.out.println("ERROR in convertBlobToByteArray: " + e.getMessage());
            }
        }
        
        return profilePictureBytes;
    }
    
    
  
    



}
