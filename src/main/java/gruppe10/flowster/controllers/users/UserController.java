package gruppe10.flowster.controllers.users;

import gruppe10.flowster.models.users.User;
import gruppe10.flowster.repositories.OrganisationRepository;
import gruppe10.flowster.services.UserService;
import gruppe10.flowster.viewModels.CreateUserViewModel;
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
    
    // TODO: nu kalder vi denne direkte herinde
    OrganisationRepository organisationRepository = new OrganisationRepository();
    
    @GetMapping("/")
    public String index(Model createUserModel)
    {
        createUserModel.addAttribute("createUserModel", createUserModel);
        
        return "General/index"; // html
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
        createUserViewModel = userService.createCreateUserModelFromForm(dataFromCreateUserForm);
        
        /* passende url som skal returneres findes ud fra createUserModel-obj
        // if: createUserModel == projectManager: "redirect:/projectManager/frontPage"
        // if: createUserModel == teamMember: "redirect:/frontPage"
        // if createUserModel == ukorrekte oplydninger: "redirect:/" - herfra vises de oplysninger som brugeren
        // tastede ind i formen
        
         */
        String returnUrl = "redirect:/"; // tror automatisk der er sket en fejl
        
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
            returnUrl = userService.retrieveFrontPageUrl();
            
            
            /*
            // opretter nyt User-obj ud fra createUserViewModel
            User newUser = userService.createUserFromCreateUserModel(createUserViewModel);
            
            // tilføjer nyt User-obj til db
            organisationRepository.insertUserIntoDb(newUser);
            
             */
    
    
            return
        }
        
        
        return returnUrl;
    }
    
    @GetMapping("/logIn")
    public String logIn(Model logInViewModel)
    {
    
    
    
    
    }




}
