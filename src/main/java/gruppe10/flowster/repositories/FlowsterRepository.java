package gruppe10.flowster.repositories;

import gruppe10.flowster.models.users.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FlowsterRepository
{
    
    GeneralRepository generalRepository = new GeneralRepository();
    
    Connection flowsterConnection;
    
    
    
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
            System.out.println("Error in isEmailAvailable: " + e.getMessage());
        }
        finally
        {
            try
            {
                flowsterConnection.close();
            }
            catch(SQLException e)
            {
                System.out.println("Error in isEmailAvailableFinally: " + e.getMessage());
            }
        }
        
        return emailIsAvailable;
    }
    
    /**
     * Tjekker om et organisation findes i organisations-tabellen i flowster-db ud fra organisationId
     *
     * @param organisationId id som tjekkes
     * @return boolean true, hvis id findes i db - false, hvis id ikke findes i db
     * */
    public boolean doesOrganisationdExist(int organisationId)
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
            System.out.println("Error in doesOrganisationdExist: " + e.getMessage());
        }
        finally
        {
            try
            {
                flowsterConnection.close();
            }
            catch(SQLException e)
            {
                System.out.println("Error in doesOrganisationdExistFinally: " + e.getMessage());
            }
        }
    
        return organisationIdExists;
    }
    
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
            System.out.println("Error in doesJobTypeExist: " + e.getMessage());
        }
        finally
        {
            try
            {
                flowsterConnection.close();
            }
            catch(SQLException e)
            {
                System.out.println("Error in doesJobTypeExistFinally: " + e.getMessage());
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
            System.out.println("Error in retrieveJobTypeFromJobTypeId: " + e.getMessage());
        }
        finally
        {
            try
            {
                flowsterConnection.close();
            }
            catch(SQLException e)
            {
                System.out.println("Error in retrieveJobTypeFromJobTypeIdFinally: " + e.getMessage());
            }
        }
    
        return jobType;
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
            System.out.println("ERROR in retrieveOrganisationNameFromOrganisationId: " + e.getMessage());
        }
        finally
        {
            try
            {
                flowsterConnection.close();
            }
            catch(SQLException e)
            {
                System.out.println("ERROR in retrieveOrganisationNameFromOrganisationIdFinally: " + e.getMessage());
            }
        }
        
     return organisationName;
    }
    
    /**
     * Tilføjer email til emails-tabel i flowster-db
     *
     * @param email email som skal indsættes i db
     * @return void
     * */
    public void insertEmailIntoDb(String email)
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
            System.out.println("ERROR in insertUserIntoDb: " + e.getMessage());
        }
        finally
        {
            try
            {
                flowsterConnection.close();
            }
            catch(SQLException e)
            {
                System.out.println("ERROR in insertUserIntoDbFinally: " + e.getMessage());
            }
        }
    }
    
    /**
     * Henter emailId fra emails-tabel i flowster-db ud fra specifik email
     *
     * @param email den som bruges til at finde emailId til
     * @return int den fundne emailId
     * */
    public int retrieveEmailId(String email)
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
            System.out.println("ERROR in retrieveEmailId: " + e.getMessage());
        }
        finally
        {
            try
            {
                flowsterConnection.close();
            }
            catch(SQLException e)
            {
                System.out.println("ERROR in retrieveEmailIdFinally: " + e.getMessage());
            }
        }
        
        return emailId;
    }
    
    // retrieveJobTypeClassNameFromId
    
}
