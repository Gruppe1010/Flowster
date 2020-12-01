package gruppe10.flowster.controllers.users;

import gruppe10.flowster.services.UserService;
import gruppe10.flowster.viewModels.CreateUserModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
public class UserController
{
    // disse attributter bliver packageprivate fordi vi ikke har givet dem nogle andre accessmodifiers
    UserService userService = new UserService();
    CreateUserModel createUserModel = new CreateUserModel();
    
    @GetMapping("/")
    public String index(Model createUserModel)
    {
        createUserModel.addAttribute("createUserModel", createUserModel);
        
        return "index";
    }
    
    
    @PostMapping("/postCreateUser")
    public String postCreateUser(WebRequest dataFromCreateUserForm)
    {
        createUserModel = userService.createCreateUserModelFromForm(dataFromCreateUserForm);
        
        String returnUrl = userService.createUser(createUserModel);
        
        return returnUrl;
    }




}
