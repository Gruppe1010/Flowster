package gruppe10.flowster.models.users;

import gruppe10.flowster.models.project.Project;
import gruppe10.flowster.models.teams.Team;
import gruppe10.flowster.viewModels.user.CreateUserViewModel;

import java.util.ArrayList;

public class ProjectManager extends User
{
    private ArrayList<Project> managedProjectsList;

    
    //constructors
    public ProjectManager(){}
    
    public ProjectManager(int organisationAndJobType, String firstname, String surname, String email, String password
            , double manhours)
    {
        super(organisationAndJobType, firstname, surname, email, password, manhours);
    }
    
    public ProjectManager(int id, int organisationAndJobType, String firstname, String surname, String email,
                          String password
            , double manhours, byte[] profilePictureBytes, ArrayList<Team> joinedTeamsList)
    {
        super(id, organisationAndJobType, firstname, surname, email, password, manhours, profilePictureBytes, joinedTeamsList);
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
    
    
    @Override
    public String findClassNameUrlPath()
    {
        String urlPath = getClass().getSimpleName();
        
        urlPath = Character.toLowerCase(urlPath.charAt(0)) + urlPath.substring(1);
        
        return urlPath;
    }
    
    
}
