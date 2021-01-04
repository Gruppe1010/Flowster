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
    private UserService userService = new UserService();
    
    private CreateUserViewModel createUserViewModel;
    private LogInViewModel logInViewModel;
    
    private String orgDbName;
    private String error = null;
    
    
    @GetMapping("/")
    public String index(Model logInViewModel, Model loggedInUser, Model orgDbNameModel, Model errorModel)
    {
        // fordi man ryger herhen når man logger ud, skal loggedInUser resettes
        // TODO hvis vi ændrer static loggedInUser, skal denne slettes??
        userService.resetLoggedInUser();
        
        orgDbNameModel.addAttribute("orgDbName", null);
        
        logInViewModel.addAttribute("logInViewModel", this.logInViewModel);
        loggedInUser.addAttribute("loggedInUser", null);
        errorModel.addAttribute("error", error);
        
        return "pre-login/index"; // html
    }
    
    @PostMapping("/logIn")
    public String postLogIn(WebRequest dataFromLogInForm, Model orgDbNameModel)
    {
        error = null;
        
        logInViewModel = userService.createLogInViewModelFromForm(dataFromLogInForm);
        
        // i denne metode sættes loggedInUser
        boolean logInInfoIsValid = userService.checkIfLogInInfoIsValid(logInViewModel);
        
        orgDbNameModel.addAttribute("orgDbName", userService.findOrgDbName());
        
        if(logInInfoIsValid)
        {
            // reset modellen, så den ikke længere vises i formen, da logIn var successfuldt
            logInViewModel = null;
            orgDbName = userService.findOrgDbName(); //TODO ÆNDRING tilføjet d. 4-01-2021
            
            return String.format("redirect:/%s/frontPage", orgDbName);
        }
    
        // man er her, hvis ugyldig logInInfo
        error = userService.getError();
        
        return "redirect:/#error-popup";
    }
 
    @GetMapping("/createUser")
    public String createUser(Model createUserViewModel, Model loggedInUserModel, Model errorModel)
    {
        createUserViewModel.addAttribute( "createUserViewModel", this.createUserViewModel);
        loggedInUserModel.addAttribute( "loggedInUser", null);
        errorModel.addAttribute("error", error);

        return "pre-login/create-user"; // html
    }
    
    /**
     * Sender bruger videre til korrekt url, når hun/han trykker "opret" på index-html
     *
     * @param dataFromCreateUserForm Data fra 'createUserForm'
     * @return String url, som redirectes til
     * */
    @PostMapping("/createUser")
    public String postCreateUser(WebRequest dataFromCreateUserForm, Model orgDbNameModel)
    {
        error = null;
        
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
            
            // reset createUserViewModel, så den ikke bliver vist i formen - da bruger er oprettet successfuldt
            createUserViewModel = null;
            
            return String.format("redirect:/%s/frontPage", orgDbName);
        }
        
        // man er her, hvis ugyldig dataFromForm
        error = userService.getError();
        
        return "redirect:/createUser/#error-popup";
    }
    
    
    
    
    
}
