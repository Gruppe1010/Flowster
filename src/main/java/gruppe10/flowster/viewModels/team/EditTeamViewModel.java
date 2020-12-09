package gruppe10.flowster.viewModels.team;

import gruppe10.flowster.viewModels.user.PreviewUserViewModel;

import java.util.ArrayList;

public class EditTeamViewModel
{
    private int id;
    private String name;
    private ArrayList<PreviewUserViewModel> userList;
    
    // constructors
    public EditTeamViewModel(){
   
    }
    
    public EditTeamViewModel(int id, String name, ArrayList<PreviewUserViewModel> userList)
    {
        this.id = id;
        this.name = name;
        this.userList = userList;
    }
    
    // getters + setters
    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public ArrayList<PreviewUserViewModel> getUserList()
    {
        return userList;
    }
    public void setUserList(ArrayList<PreviewUserViewModel> userList)
    {
        this.userList = userList;
    }
}
