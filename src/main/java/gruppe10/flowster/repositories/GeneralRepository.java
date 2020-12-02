package gruppe10.flowster.repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GeneralRepository
{
    
    
    
    
    /**
     * Laver en connection til tilfældig-databasen
     *
     * @param dbName Navnet på db som vi connecter til
     *
     * @return Connection Den oprettede connection ELLER null ved fejl i oprettelsen af connection
     */
    public Connection establishConnection(String dbName)
    {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+ dbName +
                                                             "?serverTimezone=UTC", "gruppe10", "gruppe10");
        }
        catch(SQLException e)
        {
            System.out.println("Error in establishConnection: " + e.getMessage());
        }
        
        return connection;
    }
    
    
    
    
    
    
}
