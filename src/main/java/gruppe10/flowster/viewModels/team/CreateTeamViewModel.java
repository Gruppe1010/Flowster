package gruppe10.flowster.viewModels.team;

import gruppe10.flowster.models.users.User;

import java.util.ArrayList;

public class CreateTeamViewModel
{
    private String name;
    private ArrayList<User> userList;
    
    // constructors
    public CreateTeamViewModel()
    {
   
    }
    public CreateTeamViewModel(String name, ArrayList<User> userList)
    {
        this.name = name;
        this.userList = userList;
    }
    
    // getters + setters
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public ArrayList<User> getUserList()
    {
        return userList;
    }
    public void setUserList(ArrayList<User> userList)
    {
        this.userList = userList;
    }
}
