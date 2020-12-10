package gruppe10.flowster.controllers;

import gruppe10.flowster.services.UserService;
import gruppe10.flowster.viewModels.user.CreateUserViewModel;
import gruppe10.flowster.viewModels.user.LogInViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
public class PreLoginController
{
    // disse attributter bliver packageprivate fordi vi ikke har givet dem nogle andre accessmodifiers
    UserService userService = new UserService();
    
    CreateUserViewModel createUserViewModel = new CreateUserViewModel();
    LogInViewModel logInViewModel = new LogInViewModel();
    String orgDbName;
    
    @GetMapping("/")
    public String index(Model logInViewModel, Model loggedInUser, Model orgDbNameModel)
    {
        userService.resetLoggedInUser();
    
        orgDbName = userService.findOrgDbName();
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        
        logInViewModel.addAttribute("logInViewModel", this.logInViewModel);
        loggedInUser.addAttribute("loggedInUser", UserService.loggedInUser);
        
        return "pre-login/index"; // html
    }
    
    /**
     * Sender bruger videre til korrekt url, når hun/han trykker "opret" på index-html
     *
     * @param dataFromCreateUserForm Data fra 'createUserForm'
     * @return String url, som redirectes til
     * */
    @PostMapping("/postCreateUser")
    public String postCreateUser(WebRequest dataFromCreateUserForm, Model orgDbNameModel)
    {
        // createUserModel bliver oprettet og gemt med oplysninger som bruger har tastet ind
        createUserViewModel = userService.createCreateUserViewModelFromForm(dataFromCreateUserForm);
        
        // tjekker om indtastet data er valid: orgKoden findes, email != optaget, passwords matcher
        boolean dataFromFormIsValid = userService.checkDataFromCreateUserViewModel(createUserViewModel);
    
        
        if(dataFromFormIsValid)
        {
            // indsæt i database (loggedInUser bliver også sat)
            userService.insertNewUserIntoDbAndSetLoggedInUser(createUserViewModel);

            orgDbName = userService.findOrgDbName();
            orgDbNameModel.addAttribute("orgDbName", orgDbName);
            
            return String.format("redirect:/%s/frontPage", orgDbName);
            // return "redirect:/" + orgDbName + "/frontPage";
        }
        
        return "redirect:/createUser";
    }
    
    @GetMapping("/createUser")
    public String logIn(Model createUserViewModel, Model loggedInUserModel)
    {
        createUserViewModel.addAttribute( "createUserViewModel", this.createUserViewModel);
        loggedInUserModel.addAttribute( "loggedInUser", UserService.loggedInUser);

        return "pre-login/create-user"; // html
    }

    @PostMapping("/postLogIn")
    public String postLogIn(WebRequest dataFromLogInForm, Model orgDbNameModel)
    {
       
        logInViewModel = userService.createLogInViewModelFromForm(dataFromLogInForm);
        
        // i denne metode sættes loggedInUser
        boolean logInInfoIsValid = userService.checkIfLogInInfoIsValid(logInViewModel);
    
        orgDbName = userService.findOrgDbName();
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
    
    
        if(logInInfoIsValid)
        {
            return String.format("redirect:/%s/frontPage", orgDbName);
            // return "redirect:/" + orgDbName +"/frontPage";
        }
        
        // hvis invalid logInInfo
        return "redirect:/";
    }
    
 
    
    
}