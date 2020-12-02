package gruppe10.flowster.repositories;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import gruppe10.flowster.models.users.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
                                           (newUser.findOrganisationIdFromOrganisationAndJopType());
        
        // opretter ny String med underscore i stedet for mellemrum i organisationName
        String dbName = "flowster_" + organisationName.replaceAll(" ", "_");
        
        // FØRST indsættes email i emails-tabel i flowster-db
        flowsterRepository.insertEmailIntoDb(newUser.getEmail());
        
        // DERNÆST indsættes resten af userData i users-tabel i organisationName-db
        insertUserDataIntoOrganisationDb(dbName, newUser);
    }
    
    /**
     * Indsætter et nyt User-obj i users-tabellen i dbName-databasen
     *
     * @param dbName navnet på db'en User-obj skal indsættes i
     * @param newUser det nye User-obj som skal indsættes i db
     * @return void
     * */
    public void insertUserDataIntoOrganisationDb(String dbName, User newUser)
    {
        // finder tilsvarende id'er til jopType + email - da de lagres i db som foreign keys m. reference til andre tab
        int jobTypeId = newUser.findJobTypeIdFromOrganisationAndJopType();
        int emailId = flowsterRepository.retrieveEmailId(newUser.getEmail());
        
        organisationConnection = generalRepository.establishConnection(dbName);
        
        // lægge ALT data på newUser ned
        try
        {
            String sqlCommand = "INSERT INTO users (f_id_job_type, f_id_email, firstname, surname, password, manhours)";
    
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
    
    
    
    
  
    



}
