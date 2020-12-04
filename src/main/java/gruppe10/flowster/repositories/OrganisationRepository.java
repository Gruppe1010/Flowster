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
                // find organisationAndJobType
                int jobType = resultSet.getInt("f_id_job_type");
    
                // hvis det er en ProjectManager
                if(jobType == 1)
                {
                    user = createProjectManagerFromResultSet(resultSet);
                }
                // hvis det er en TeamMember
                else if(jobType == 2)
                {
                    user = createTeamMemberFromResultSet(resultSet);
                }
            }
        }
        catch(SQLException e)
        {
            System.out.println("ERROR in createUserFromResultSet: " + e.getMessage());
        }
        
        return user;
    }
    
    /**
     * Opretter et nyt ProjectManager-obj ud fra resultSet
     *
     * @param resultSet ResultSet-obj som bruges til at oprette ProjectManager-obj fra
     * @return ProjectManager nyoprettede ProjectManager-obj
     * */
    public ProjectManager createProjectManagerFromResultSet(ResultSet resultSet)
    {
        ProjectManager projectManager = null;
        
        try
        {
            // find email ud fra emailId
            int emailId = resultSet.getInt("f_id_email");
            String email = flowsterRepository.retrieveEmailFromEmailId(emailId);
    
    
            // find organisationAndJobType
            int jobType = resultSet.getInt("f_id_job_type");
    
            String organisationAndJobTypeString = Integer.toString(
                    flowsterRepository.retrieveOrganisationIdFromEmailId(emailId) + jobType);
    
            int organisationAndJobType = Integer.parseInt(organisationAndJobTypeString);
    
            // find profilePictureBytes
            byte[] profilePictureBytes = convertBlobToByteArray(resultSet.getBlob("profile_picture"));
    
    
            projectManager = new ProjectManager(organisationAndJobType,
                    resultSet.getString("firstname"),
                    resultSet.getString("surname"),
                    email,
                    resultSet.getString("password"),
                    resultSet.getDouble("manhours"),
                    profilePictureBytes);
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in createProjectManagerFromResultSet: " + e.getMessage());
        }
        return projectManager;
    }
    
    /**
     * Opretter et nyt TeamMember-obj ud fra resultSet
     *
     * @param resultSet ResultSet-obj som bruges til at oprette TeamMember-obj fra
     * @return TeamMember nyoprettede TeamMember-obj
     * */
    public TeamMember createTeamMemberFromResultSet(ResultSet resultSet)
    {
        TeamMember teamMember = null;
    
        try
        {
            // find email ud fra emailId
            int emailId = resultSet.getInt("f_id_email");
            String email = flowsterRepository.retrieveEmailFromEmailId(emailId);
        
        
            // find organisationAndJobType
            int jobType = resultSet.getInt("f_id_job_type");
        
            String organisationAndJobTypeString = Integer.toString(
                    flowsterRepository.retrieveOrganisationIdFromEmailId(emailId) + jobType);
        
            int organisationAndJobType = Integer.parseInt(organisationAndJobTypeString);
        
            // find profilePictureBytes
            byte[] profilePictureBytes = convertBlobToByteArray(resultSet.getBlob("profile_picture"));
    
    
            teamMember = new TeamMember(organisationAndJobType,
                    resultSet.getString("firstname"),
                    resultSet.getString("surname"),
                    email,
                    resultSet.getString("password"),
                    resultSet.getDouble("manhours"),
                    profilePictureBytes);
            
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in createTeamMemberFromResultSet: " + e.getMessage());
        }
        return teamMember;
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
