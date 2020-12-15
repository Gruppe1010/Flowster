package gruppe10.flowster.models.users;

import gruppe10.flowster.models.project.Project;
import gruppe10.flowster.models.teams.Team;
import gruppe10.flowster.viewModels.user.CreateUserViewModel;

import java.util.ArrayList;

public class TeamMember extends User
{
    private ArrayList<Project> joinedProjectsList;
   
    
    // constructors
    public TeamMember(){}
    
    public TeamMember(int organisationAndJobType, String firstname, String surname, String email, String password,
                      double manhours)
    {
        super(organisationAndJobType, firstname, surname, email, password, manhours);
        this.joinedProjectsList = null;
    }
    
    public TeamMember(int id, int organisationAndJobType, String firstname, String surname, String email,
                      String password,
                      double manhours, byte[] profilePictureBytes, ArrayList<Team> joinedTeamsList)
    {
        super(id, organisationAndJobType, firstname, surname, email, password, manhours, profilePictureBytes, joinedTeamsList);
        this.joinedProjectsList = null;

    }
    
    
    // getters + setters
    public ArrayList<Project> getJoinedProjectList()
    {
        return joinedProjectsList;
    }
    public void setJoinedProjectList(ArrayList<Project> joinedProjectList)
    {
        this.joinedProjectsList = joinedProjectList;
    }

    
    // ANDRE METODER
    
    @Override
    public boolean isTeamMember()
    {
        return true;
    }
    
    public TeamMember createTeamMemberFromCreateUserModel(CreateUserViewModel createUserViewModel)
    {
        return new TeamMember(
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
