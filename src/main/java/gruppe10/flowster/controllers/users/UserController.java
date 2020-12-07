package gruppe10.flowster.controllers.users;

import gruppe10.flowster.repositories.OrganisationRepository;
import gruppe10.flowster.services.UserService;
import gruppe10.flowster.viewModels.CreateUserViewModel;
import gruppe10.flowster.viewModels.LogInViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
public class UserController
{
    // disse attributter bliver packageprivate fordi vi ikke har givet dem nogle andre accessmodifiers
    UserService userService = new UserService();
    CreateUserViewModel createUserViewModel = new CreateUserViewModel();
    LogInViewModel logInViewModel = new LogInViewModel();
    
    // TODO: nu kalder vi denne direkte herinde
    OrganisationRepository organisationRepository = new OrganisationRepository();
    
    String redirect = "redirect:/";
    
    @GetMapping("/")
    public String index(Model logInViewModel, Model loggedInUser)
    {
        /* TODO måske sæt ind igen - hvis footer th-fucker
        if(UserService.loggedInUser != null)
        {
            return redirect + UserService.loggedInUser.findClassNameUrlPath() + "/frontPage";
        }
        
         */
        
        logInViewModel.addAttribute("logInViewModel", this.logInViewModel);
        loggedInUser.addAttribute("loggedInUser", UserService.loggedInUser);
        
        return "general/index"; // html
    }
    
    /**
     * Sender bruger videre til korrekt url, når hun/han trykker "opret" på index-html
     *
     * @param dataFromCreateUserForm Data fra 'createUserForm'
     * @return String url, som redirectes til
     * */
    @PostMapping("/postCreateUser")
    public String postCreateUser(WebRequest dataFromCreateUserForm)
    {
        // createUserModel bliver oprettet og gemt med oplysninger som bruger har tastet ind
        createUserViewModel = userService.createCreateUserViewModelFromForm(dataFromCreateUserForm);
        
        /* passende url som skal returneres findes ud fra createUserModel-obj
        // if: createUserModel == projectManager: "redirect:/projectManager/frontPage"
        // if: createUserModel == teamMember: "redirect:/frontPage"
        // if createUserModel == ukorrekte oplydninger: "redirect:/" - herfra vises de oplysninger som brugeren
        // tastede ind i formen
         */
        
        // tjekker om indtastet data er valid
        /*
        organisationAndJobType-koden findes
        email er ikke optaget
        passwords matcher hinanden
         */
        boolean dataFromFormIsValid = userService.checkDataFromCreateUserViewModel(createUserViewModel);
        
        if(dataFromFormIsValid)
        {
            // indsæt i database
            userService.insertNewUserIntoDb(createUserViewModel);
            
            // afhængigt af hvilken type user der lige er blevet oprette, guide til en url
            String className = userService.retrieveClassNameUrl();
    
            return redirect + className + "/frontPage";
        }
        
        return redirect;
    }
    
    @GetMapping("/createUser")
    public String logIn(Model createUserViewModel, Model loggedInUserModel)
    {
        createUserViewModel.addAttribute( "createUserViewModel", this.createUserViewModel);
        createUserViewModel.addAttribute( "loggedInUser", loggedInUserModel);

        return "general/create-user"; // html

    }

    @PostMapping("/postLogIn")
    public String postLogIn(WebRequest dataFromLogInForm)
    {
        logInViewModel = userService.createLogInViewModelFromForm(dataFromLogInForm);
        
        // TODO HER
        boolean logInInfoIsValid = userService.checkIfLogInInfoIsValid(logInViewModel);
        
        if(logInInfoIsValid)
        {
            String className = userService.retrieveClassNameUrl();
    
            return redirect + className + "/frontPage";
        }
        
        return redirect;
    }
    
    
    @GetMapping("/contact")
    public String contact(Model loggedInUserModel)
    {
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        
        return "general/contact"; // html
    }
    
    @GetMapping("/about")
    public String about(Model loggedInUserModel)
    {
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        
        return "general/about"; // html
    }
    
    
    
}
