package gruppe10.flowster.repositories;

import gruppe10.flowster.models.users.User;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class FlowsterRepository
{
    
    GeneralRepository generalRepository = new GeneralRepository();
    
    Connection flowsterConnection;
    
    // ------------------ INSERT -------------
    
    /**
     * Indsætter række i emails_organisations-tabel i flowster-db - dvs. tilføjer f_id_email og f_id_organisation
     *
     * @param user User-obj som info der sættes ind i tabel hentes ud fra
     * @return void
     * */
    public void insertRowIntoEmailsAndOrganisations(User user)
    {
        // først find id_eamil
        int id_email = retrieveEmailIdFromEmail(user.getEmail());
        
        // find id_organisation
        int id_organisation = user.findOrganisationId();
        
        flowsterConnection = generalRepository.establishConnection("flowster");
        
        try
        {
            String sqlCommand = "INSERT INTO emails_organisations (f_id_email, f_id_organisation) values (?, ?)";
            
            PreparedStatement preparedStatement = flowsterConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setInt(1, id_email);
            preparedStatement.setInt(2, id_organisation);
            
            preparedStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in insertRowIntoEmailsAndOrganisations: " + e.getMessage());
        }
        finally
        {
            try
            {
                flowsterConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in insertRowIntoEmailsAndOrganisationsFinally: " + e.getMessage());
            }
        }
        
        
        
    }
    
    
    
    // ------------------ JOBTYPE -------------
 
    
    /**
     * Tjekker om et jobType findes i job_types-tabellen i flowster-db ud fra jobTypeId
     *
     * @param jobTypeId id som tjekkes
     * @return boolean true, hvis id findes i db - false, hvis id ikke findes i db
     * */
    public boolean doesJobTypeExist(int jobTypeId)
    {
        flowsterConnection = generalRepository.establishConnection("flowster");
        
        boolean jobTypeIdExists = false;
        
        try
        {
            String sqlCommand = "SELECT * FROM job_types WHERE id_job_type = ?";
            
            // det er vores SQL sætning som vi beder om at få prepared til at blive sendt til databasen:
            PreparedStatement preparedStatement = flowsterConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setInt(1, jobTypeId);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if(resultSet.next())
            {
                jobTypeIdExists = true;
            }
        }
        catch(SQLException e)
        {
            System.err.println("Error in doesJobTypeExist: " + e.getMessage());
        }
        finally
        {
            try
            {
                flowsterConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("Error in doesJobTypeExistFinally: " + e.getMessage());
            }
        }
        
        return jobTypeIdExists;
    }
    
    /**
     * Henter job_type kolonne ud fra id_job_type
     *
     * @param jobTypeId id'et job_type skal findes ud fra
     * @return String fundne job_type
     * */
    public String retrieveJobTypeFromJobTypeId(int jobTypeId)
    {
        flowsterConnection = generalRepository.establishConnection("flowster");
    
        String jobType = null;
    
        try
        {
            String sqlCommand = "SELECT (job_type) FROM job_types WHERE id_job_type = ?";
        
            // det er vores SQL sætning som vi beder om at få prepared til at blive sendt til databasen:
            PreparedStatement preparedStatement = flowsterConnection.prepareStatement(sqlCommand);
        
            preparedStatement.setInt(1, jobTypeId);
        
            ResultSet resultSet = preparedStatement.executeQuery();
        
            if(resultSet.next())
            {
                jobType = resultSet.getString(1);
            }
        }
        catch(SQLException e)
        {
            System.err.println("Error in retrieveJobTypeFromJobTypeId: " + e.getMessage());
        }
        finally
        {
            try
            {
                flowsterConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("Error in retrieveJobTypeFromJobTypeIdFinally: " + e.getMessage());
            }
        }
    
        return jobType;
    }
    
    // ------------------ ORGANISATION -------------
    
    /**
     * Tjekker om et organisation findes i organisations-tabellen i flowster-db ud fra organisationId
     *
     * @param organisationId id som tjekkes
     * @return boolean true, hvis id findes i db - false, hvis id ikke findes i db
     * */
    public boolean doesOrganisationExist(int organisationId)
    {
        flowsterConnection = generalRepository.establishConnection("flowster");
        
        boolean organisationIdExists = false;
        
        try
        {
            String sqlCommand = "SELECT * FROM organisations WHERE id_organisation = ?";
            
            // det er vores SQL sætning som vi beder om at få prepared til at blive sendt til databasen:
            PreparedStatement preparedStatement = flowsterConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setInt(1, organisationId);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if(resultSet.next())
            {
                organisationIdExists = true;
            }
        }
        catch(SQLException e)
        {
            System.err.println("Error in doesOrganisationdExist: " + e.getMessage());
        }
        finally
        {
            try
            {
                flowsterConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("Error in doesOrganisationdExistFinally: " + e.getMessage());
            }
        }
        
        return organisationIdExists;
    }
    
    /**
     * Henter et organisationsnavn ud fra et nyoprettet User-obj
     *
     * @param organisationId det nye User-obj som vi finder organisationsName'et fra
     * @return String det fundne organisationsName
     * */
    public String retrieveOrganisationNameFromOrganisationId(int organisationId)
    {
        String organisationName = null;
        
        flowsterConnection = generalRepository.establishConnection("flowster");
        
        try
        {
            String sqlCommand = "SELECT (organisation_name) FROM organisations WHERE id_organisation = ?";
            
            PreparedStatement preparedStatement = flowsterConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setInt(1, organisationId);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if(resultSet.next())
            {
                organisationName = resultSet.getString(1);
            }
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in retrieveOrganisationNameFromOrganisationId: " + e.getMessage());
        }
        finally
        {
            try
            {
                flowsterConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in retrieveOrganisationNameFromOrganisationIdFinally: " + e.getMessage());
            }
        }
        
     return organisationName;
    }
    
    /**
     * Henter organisationName ud fra email i emails_organisations-tabel i flowster-db
     *
     * @param email email som organisationName hentes ud fra
     * @return String fundne organisationName
     * */
    public String retrieveOrganisationNameFromEmail(String email)
    {
        String organisationName = null;
        
        // find id_email
        int emailId = retrieveEmailIdFromEmail(email);
        
        flowsterConnection = generalRepository.establishConnection("flowster");
        
        try
        {
            // RIGHT JOIN er det samme som RIGHT OUTER JOIN == outer er standard når man laver en join
            String sqlCommand = "SELECT organisation_name FROM emails_organisations RIGHT JOIN organisations ON " +
                                        "f_id_organisation = id_organisation WHERE f_id_email = ?";
            
            PreparedStatement preparedStatement = flowsterConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setInt(1, emailId);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if(resultSet.next())
            {
                organisationName = resultSet.getString(1);
            }
            
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in retrieveOrganisationNameFromEmail: " + e.getMessage());
        }
        finally
        {
            try
            {
                flowsterConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in retrieveOrganisationNameFromEmailFinally: " + e.getMessage());
            }
        }
        
        return organisationName;
    }
    
    /**
     * Henter organisationId ud fra emailId i emails_organisations-tabel i flowster-db
     *
     * @param emailId emailId som organisationId hentes ud fra
     * @return int fundne organisationId
     * */
    public int retrieveOrganisationIdFromEmailId(int emailId)
    {
        int organisationId = 0;
        
        flowsterConnection = generalRepository.establishConnection("flowster");
        
        try
        {
            String sqlCommand = "SELECT f_id_organisation FROM emails_organisations WHERE f_id_email = ?";
        
            PreparedStatement preparedStatement = flowsterConnection.prepareStatement(sqlCommand);
        
            preparedStatement.setInt(1, emailId);
        
            ResultSet resultSet = preparedStatement.executeQuery();
        
            if(resultSet.next())
            {
                organisationId = resultSet.getInt(1);
            }
        
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in retrieveOrganisationIdFromEmailId: " + e.getMessage());
        }
        finally
        {
            try
            {
                flowsterConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in retrieveOrganisationIdFromEmailIdFinally: " + e.getMessage());
            }
        }
    
        return organisationId;
    }
    
    /**
     *
     *
     * */
    public String retrieveOrganisationNameFromEmailId(int emailId)
    {
        String organisationName = null;
    
        flowsterConnection = generalRepository.establishConnection("flowster");
    
        try
        {
            String sqlCommand = "SELECT organisation_name FROM emails_organisations RIGHT JOIN ON f_id_organisation =" +
                                        " id_organisation WHERE f_id_email = ?";
            
            PreparedStatement preparedStatement = flowsterConnection.prepareStatement(sqlCommand);
        
            preparedStatement.setInt(1, emailId);
        
            ResultSet resultSet = preparedStatement.executeQuery();
        
            if(resultSet.next())
            {
                organisationName = resultSet.getString(1);
            }
        
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in retrieveOrganisationNameFromEmailId: " + e.getMessage());
        }
        finally
        {
            try
            {
                flowsterConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in retrieveOrganisationNameFromEmailIdFinally: " + e.getMessage());
            }
        }
    
        return organisationName;
    }
    
    
    // ------------------ EMAIL -------------
    
    
    /**
     * Tjekker om email allerede er gemt på anden user i db
     *
     * @param email Email som tjekkes for om den er optaget
     *
     * @return Boolean true hvis den er ledig, false - hvis ikke ledig
     */
    public boolean isEmailAvailable(String email)
    {
        flowsterConnection = generalRepository.establishConnection("flowster");
        
        boolean emailIsAvailable = true; // sættes til at være available by default
        
        try
        {
            String sqlCommand = "SELECT * FROM emails WHERE email = ?";
            
            // det er vores SQL sætning som vi beder om at få prepared til at blive sendt til databasen:
            PreparedStatement preparedStatement = flowsterConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setString(1, email);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            // hvis der ligger en email i resultSet'et, er email'en ikke tilgængelig, og emailIsAvailable sættes til
            // false
            if(resultSet.next())
            {
                emailIsAvailable = false;
            }
        }
        catch(SQLException e)
        {
            System.err.println("Error in isEmailAvailable: " + e.getMessage());
        }
        finally
        {
            try
            {
                flowsterConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("Error in isEmailAvailableFinally: " + e.getMessage());
            }
        }
        
        return emailIsAvailable;
    }
    
    /**
     * Tilføjer email til emails-tabel i flowster-db
     *
     * @param email email som skal indsættes i db
     * @return void
     * */
    public void insertEmailIntoEmails(String email)
    {
        flowsterConnection = generalRepository.establishConnection("flowster");
        
        try
        {
        
            String sqlCommand = "INSERT INTO emails (email) value (?)";
            
            PreparedStatement preparedStatement = flowsterConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setString(1, email);
            
            preparedStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in insertUserIntoDb: " + e.getMessage());
        }
        finally
        {
            try
            {
                flowsterConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in insertUserIntoDbFinally: " + e.getMessage());
            }
        }
    }
    
    /**
     * Henter emailId fra emails-tabel i flowster-db ud fra specifik email
     *
     * @param email den som bruges til at finde emailId til
     * @return int den fundne emailId
     * */
    public int retrieveEmailIdFromEmail(String email)
    {
        flowsterConnection = generalRepository.establishConnection("flowster");
        
        int emailId = 0;
        
        try
        {
            String sqlCommand = "SELECT (id_email) FROM emails WHERE email = ?";
            
            PreparedStatement preparedStatement = flowsterConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setString(1, email);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if(resultSet.next())
            {
                emailId = resultSet.getInt(1);
            }
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in retrieveEmailId: " + e.getMessage());
        }
        finally
        {
            try
            {
                flowsterConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in retrieveEmailIdFinally: " + e.getMessage());
            }
        }
        
        return emailId;
    }
    
    public String retrieveEmailFromEmailId(int emailId)
    {
    
        flowsterConnection = generalRepository.establishConnection("flowster");
    
        String email = null;
    
        try
        {
            String sqlCommand = "SELECT email FROM emails WHERE id_email = ?";
        
            PreparedStatement preparedStatement = flowsterConnection.prepareStatement(sqlCommand);
        
            preparedStatement.setInt(1, emailId);
        
            ResultSet resultSet = preparedStatement.executeQuery();
        
            if(resultSet.next())
            {
                email = resultSet.getString(1);
            }
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in retrieveEmailFromEmailId: " + e.getMessage());
        }
        finally
        {
            try
            {
                flowsterConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in retrieveEmailFromEmailIdFinally: " + e.getMessage());
            }
        }
    
        return email;
        
        
        
        
    }
    
    public int retrieveUserIdFromEmail(String dbName, String email)
    {
        int userId = 0;
    
        System.out.println("dbname flowsterrepository: " + dbName);
        
        flowsterConnection = generalRepository.establishConnection("flowster");
        
        try
        {
            // SELECT id_user FROM flowster.emails RIGHT JOIN flowster_kea.users ON id_email = f_id_email WHERE email = "vibej@hotmail.com";
            String sqlCommand= "SELECT id_user FROM emails RIGHT JOIN ?.users ON id_email = f_id_email WHERE email = ?";
            
            PreparedStatement preparedStatement = flowsterConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setString(1, dbName);
            preparedStatement.setString(2, email);
            
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
    
    // retrieveJobTypeClassNameFromId
    
}
