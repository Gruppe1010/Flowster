package gruppe10.flowster.models.users;

import gruppe10.flowster.models.project.Project;
import gruppe10.flowster.models.teams.Team;
import gruppe10.flowster.viewModels.CreateUserModel;

import java.util.ArrayList;

public class Employee extends User
{
    private ArrayList<Project> joinedProjectsList;
    private ArrayList<Team> joinedTeamsList;
    
    // constructors
    public Employee(){}
    
    public Employee(int organisationAndJobType, String firstname, String surname, String email, String password, double manhours)
    {
        super(organisationAndJobType, firstname, surname, email, password, manhours);
        this.joinedProjectsList = null;
        this.joinedTeamsList = null;
    }
    
    
    // getters + setters
    public ArrayList<Project> getJoinedProjectsList()
    {
        return joinedProjectsList;
    }
    public void setJoinedProjectsList(ArrayList<Project> joinedProjectsList)
    {
        this.joinedProjectsList = joinedProjectsList;
    }
    public ArrayList<Team> getJoinedTeamsList()
    {
        return joinedTeamsList;
    }
    public void setJoinedTeamsList(ArrayList<Team> joinedTeamsList)
    {
        this.joinedTeamsList = joinedTeamsList;
    }
    
    
    // ANDRE METODER
    
    public Employee createEmployeeFromCreateUserModel(CreateUserModel createUserModel)
    {
        return new Employee(
                Integer.parseInt(createUserModel.getOrganisationAndJobType()),
                createUserModel.getFirstname(),
                createUserModel.getSurname(),
                createUserModel.getEmail(),
                createUserModel.getPassword(),
                createUserModel.getManhours());
    }
    
}
