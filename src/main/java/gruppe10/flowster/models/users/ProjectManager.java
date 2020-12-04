package gruppe10.flowster.models.users;

import gruppe10.flowster.models.project.Project;
import gruppe10.flowster.models.teams.Team;
import gruppe10.flowster.viewModels.CreateUserViewModel;

import java.util.ArrayList;

public class ProjectManager extends User
{
    
    private ArrayList<Project> managedProjectsList;
    private ArrayList<Team> joinedTeamsList; // TODO: ændret til joinedTeamsList fordi vi godt vil have at
    // ProjectManageren er med på teamsne
    
    //constructors
    public ProjectManager(){}
    
    public ProjectManager(int organisationAndJobType, String firstname, String surname, String email, String password
            , double manhours)
    {
        super(organisationAndJobType, firstname, surname, email, password, manhours);
        this.managedProjectsList = null;
    }
    
    public ProjectManager(int organisationAndJobType, String firstname, String surname, String email, String password
            , double manhours, byte[] profilePictureBytes)
    {
        super(organisationAndJobType, firstname, surname, email, password, manhours, profilePictureBytes);
        this.managedProjectsList = null;
    }
    
    
    // getters + setters
    public ArrayList<Project> getManagedProjectsList()
    {
        return managedProjectsList;
    }
    public void setManagedProjectsList(ArrayList<Project> managedProjectsList)
    {
        this.managedProjectsList = managedProjectsList;
    }
    public ArrayList<Team> getJoinedTeamsList()
    {
        return joinedTeamsList;
    }
    public void setJoinedTeamsList(ArrayList<Team> createdTeamsList)
    {
        this.joinedTeamsList = createdTeamsList;
    }
    
    
    // ANDRE METODER
    
    @Override
    public boolean isProjectManager()
    {
        return true;
    }
    
    public ProjectManager createProjectManagerFromCreateUserModel(CreateUserViewModel createUserViewModel)
    {
        return new ProjectManager(
                Integer.parseInt(createUserViewModel.getOrganisationAndJobType()),
                createUserViewModel.getFirstname(),
                createUserViewModel.getSurname(),
                createUserViewModel.getEmail(),
                createUserViewModel.getPassword(),
                createUserViewModel.getManhours());
    }
    
    
    
}
