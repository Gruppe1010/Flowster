package gruppe10.flowster.repositories;

import gruppe10.flowster.models.teams.Team;
import gruppe10.flowster.models.users.ProjectManager;
import gruppe10.flowster.models.users.TeamMember;
import gruppe10.flowster.models.users.User;
import gruppe10.flowster.viewModels.LogInViewModel;

import java.sql.*;
import java.util.ArrayList;

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
        
        // FØRST indsættes email i emails-tabel i flowster-db
        flowsterRepository.insertEmailIntoEmails(newUser.getEmail());
        
        // SÅ indsættes række i emails_organisations-tabel i flowster-db
        flowsterRepository.insertRowIntoEmailsAndOrganisations(newUser);
        
        // DERNÆST indsættes resten af userData i users-tabel i organisationName-db
        insertUserDataIntoUsers(dbName, newUser);
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
    /* TODO GAMMEL
    public User createUserFromResultSet(ResultSet resultSet)
    {
        User user = null;
        
        try
        {
            if(resultSet.next())
            {
                
               
                
                // find organisationAndJobType
                int jobType = resultSet.getInt("f_id_job_type");
    
    
    
    
    
                // TODO: lav først user obj. her
                // TODO: derefter: if(jopType == 1 osv. )
    
    
    
                
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
            System.err.println("ERROR in createUserFromResultSet: " + e.getMessage());
        }
        
        return user;
    }
    
     */
    
    
    // TODO HER HER HER
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
                // TODO: hent teams
                ArrayList<Team> joinedTeamsList = retrieveTeamsArrayListFromUserId(dbName, id);
    
                // TODO: hent projekter
                // projectManager.setJoinedTeamsList(retrieveTeamsArrayListFromUserId(projectManager.getId()));
                
                
                user = new User(id,
                        organisationAndJobType,
                        resultSet.getString("firstname"),
                        resultSet.getString("surname"),
                        email,
                        resultSet.getString("password"),
                        resultSet.getDouble("manhours"),
                        profilePictureBytes,
                        joinedTeamsList);
                
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
    
    // TODO ny
    public ProjectManager convertUserToProjectManager(User user)
    {
        // først konverter
        ProjectManager projectManager = new ProjectManager(user.getId(), user.getOrganisationAndJobType(),
                user.getFirstname(), user.getSurname(), user.getEmail(), user.getPassword(),
                user.getManhours(), user.getProfilePictureBytes(), user.getJoinedTeamsList());
     
        
        // TODO: hent projekter
        // projectManager.setJoinedTeamsList(retrieveTeamsArrayListFromUserId(projectManager.getId()));
    
    
    
    // hent begge lister
    
        return projectManager;
    }
    
    // TODO ny-done
    public ArrayList<Team> retrieveTeamsArrayListFromUserId(String dbName, int userId)
    {
        ArrayList<Team> joinedTeamsList = null;
    
        organisationConnection = generalRepository.establishConnection(dbName);
    
        try
        {
            String sqlCommand = "SELECT f_id_team FROM teams_users WHERE f_id_user = ?";
            
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setInt(1, userId);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            joinedTeamsList = createJoinedTeamsListFromResultSet(resultSet);
        
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in retrieveTeamsArrayListFromUserId: " + e.getMessage());
        }
        finally
        {
            try
            {
                organisationConnection.close();
            }
            catch(SQLException e)
            {
                System.err.println("ERROR in retrieveTeamsArrayListFromUserIdFinally: " + e.getMessage());
            }
        }
        
        return joinedTeamsList;
    }
    
    /**
     * Opretter joinedTeamsList ud fra resultSet der indeholder teamId'er
     *
     * @param resultSet indeholder teamId'er
     * @return joinedTeamsList
     * */
    // TODO ny-done
    public ArrayList<Team> createJoinedTeamsListFromResultSet(ResultSet resultSet)
    {
        ArrayList<Team> joinedTeamsList = null;
    
        try
        {
            // liste med teamsId'er lavet ud fra resultSet
            ArrayList<Integer> teamIdsList = new ArrayList<>();
            
            // så længe der er flere teamId'er i resultSet'et
            while(resultSet.next())
            {
                int teamId = resultSet.getInt("f_id_team");
                
                // tilføjes teamId'et til teamIdsList
                teamIdsList.add(teamId);
            }
            
            // if der er noget på teamIdList (== resultSettet ikke var tomt)
            if(teamIdsList.size() > 0)
            {
                joinedTeamsList = new ArrayList<>();
                
                // find Team ud fra id og tilføj til joinedTeamsList
                for(Integer teamId : teamIdsList)
                {
                    joinedTeamsList.add(retrieveTeamFromId(teamId));
                }
            }
            
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in createJoinedTeamsListFromResultSet: " + e.getMessage());
        }
        
        return joinedTeamsList;
    }
    
    // TODO ny-done
    public Team retrieveTeamFromId(int teamId)
    {
        Team team = null;
    
        try
        {
            String sqlCommand = "SELECT * FROM teams WHERE id_team = ?";
            
            PreparedStatement preparedStatement = organisationConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setInt(1, teamId);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            team = createTeamFromResultSet(resultSet);
        
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in createTeamFromId: " + e.getMessage());
        }
        
        return team;
    }
    
    // TODO ny-done - her mangler lige lidt også
    public Team createTeamFromResultSet(ResultSet resultSet)
    {
        Team team = null;
        try
        {
    
            if(resultSet.next())
            {
                // TODO: VI SKAL OGSÅ give teamet de to lister!!!!! - men det gad jeg ikke lige
                team = new Team(resultSet.getInt("id_team"), resultSet.getString("team_name"));
            }
        }
        catch(SQLException e)
        {
            System.err.println("ERROR in createTeamFromResultSet: " + e.getMessage());
        }
        
        return team;
        
    }
    
    // TODO ny
    public TeamMember convertUserToTeamMember(User user)
    {
        // først konverter
        TeamMember teamMember = new TeamMember(user.getId(), user.getOrganisationAndJobType(),
                user.getFirstname(), user.getSurname(), user.getEmail(), user.getPassword(),
                user.getManhours(), user.getProfilePictureBytes(), user.getJoinedTeamsList());
        
     
        
        return teamMember;
    }
    
    
    // TODO: denne er måske lige meget
    /**
     * Opretter et nyt ProjectManager-obj ud fra resultSet
     *
     * @param resultSet ResultSet-obj som bruges til at oprette ProjectManager-obj fra
     * @return ProjectManager nyoprettede ProjectManager-obj
     * */
    /*
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
            
            
            // TODO: hent teams
            // retrieveTeamsArrayListFromUserId()
            
            // TODO: hent projekter
            
            
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
    
     */
    
    // TODO: denne er måske lige meget
    /**
     * Opretter et nyt TeamMember-obj ud fra resultSet
     *
     * @param resultSet ResultSet-obj som bruges til at oprette TeamMember-obj fra
     * @return TeamMember nyoprettede TeamMember-obj
     * */
    /*
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
    
     */
    
    
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
