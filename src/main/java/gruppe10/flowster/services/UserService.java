package gruppe10.flowster.services;

import gruppe10.flowster.models.project.Project;
import gruppe10.flowster.models.users.TeamMember;
import gruppe10.flowster.models.users.ProjectManager;
import gruppe10.flowster.models.users.User;
import gruppe10.flowster.repositories.FlowsterRepository;
import gruppe10.flowster.repositories.OrganisationRepository;
import gruppe10.flowster.viewModels.CreateUserModel;
import org.springframework.web.context.request.WebRequest;

public class UserService
{
    
    public ProjectManager loggedInProjectManager = null;
    public TeamMember loggedInTeamMember = null;
    
    String error;
    ProjectManager projectManager = new ProjectManager();
    TeamMember teamMember = new TeamMember();
    
    FlowsterRepository flowsterRepository = new FlowsterRepository();
    OrganisationRepository organisationRepository = new OrganisationRepository();
    
    
    /*
    * private int organisationAndJobType;
    private String firstname;
    private String surname;
    private String email;
    private String password;*/
    
    
    /**
     * Opretter et createUserModel-obj. ud fra info i createUserForm
     *
     * @param dataFromCreateUserForm data fra createUserForm, som vises på index-html
     *
     * @return CreateUserModel Det oprettede createUserModel-objekt som laves ud fra formen
     * */
    public CreateUserModel createCreateUserModelFromForm(WebRequest dataFromCreateUserForm)
    {
        // fordi vi har sat feltet "organisationAndJobType" som required i html, bliver nullpointeren handlet der -
        // derfor gør vi ikke yderligere her
        return new CreateUserModel(
                dataFromCreateUserForm.getParameter("organisation-and-job-type"),
                Double.parseDouble(dataFromCreateUserForm.getParameter("manhours")),
                dataFromCreateUserForm.getParameter("firstname"),
                dataFromCreateUserForm.getParameter("surname"),
                dataFromCreateUserForm.getParameter("email"),
                dataFromCreateUserForm.getParameter("password"),
                dataFromCreateUserForm.getParameter("confirm-password"));
    }
    
    
    /**
     * Tjekker om info på createUserModel er valid
     * Opretter nyt projectManager-obj ELLER teamMember-obj
     * Tilføjer nyt user-obj til db
     *
     * @param createUserModel Oplysninger som er tastet ind i createUserForm - som vi skal tjekke og oprette en ny
     *                        bruger i db til
     *
     * @return String url, som skal redirectes til - alt ud fra om brugeren oprettes successfuldt eller ej
     * */
    public String checkCreateAndInsertUserIntoDb(CreateUserModel createUserModel)
    {
        /* createUserForm i index-html skal indeholde:
        *
        * organisationAndJobType = required, number, min+max 5 char
        *
        * firstname + surname = required, only letters, max-length: 100
        *
        * email = required, valid email-input,
        *
        * password + confirmpassword = required, uppercase, lowercase, number/specialchar, 8-length
        *
        * */
        
        //           CHECK
        
        // tjek om email er brugt
        boolean emailIsAvailable = flowsterRepository.isEmailAvailable(createUserModel.getEmail());
    
        // hvis email!=brugt tjekke om orgkode findes
        if(emailIsAvailable)
        {
            int organisationId = createUserModel.findOrganisationIdFromOrganisationAndJopType();
            
            // tjek om orgkode er findes
            boolean organisationsIdExists = flowsterRepository.doesOrganisationdExist(organisationId);
    
            // if orgkode == findes - tjek om jobType findes
            if(organisationsIdExists)
            {
                int jobTypeId = createUserModel.findJobTypeIdFromOrganisationAndJopType();
    
                boolean jobTypeIdExists = flowsterRepository.doesJobTypeExist(jobTypeId);
    
                // if jobType findes - tjek om password + confirmpassword match
                if(jobTypeIdExists)
                {
                    if(checkIfPasswordsMatch(createUserModel.getPassword(), createUserModel.getConfirmPassword()))
                    {
                        // CREATE
    
                        // lav et user objekt og gem i enten loggedInProjectManager ELLER loggedInTeamMember
                        User newUser = createUserFromCreateUserModel(createUserModel);
                        
                        /* TODO:  TJEK OM USER BLIVER REGISTRERET SOM ENTEN
                        // boolean newUserIsTeamMember = newUser instanceof TeamMember;
                        // boolean newUserIsProjectManager = newUser instanceof ProjectManager;
                        // System.out.println("TEST in userService: teammember: " + newUserIsTeamMember + "
                        // projectmanager: " + newUserIsProjectManager);
                        
                         */
    
                        // INSERT
                        // tilføjer user-obj til db
                        organisationRepository.insertUserIntoDb(newUser);
                        
                        // redirecter til frontPage - enten til projectManager eller teamMember
                        if(newUser instanceof ProjectManager)
                        {
                            return "redirect:/projectManager/frontPage";
                        }
                        else if(newUser instanceof TeamMember)
                        {
                            return "redirect:/frontPage";
                        }
                    }
                }
            }
        }
        // hvis brugeren ikke oprettes successfyldt - redirectes til index-html igen
        return "redirect:/";
    }
    
    // CreateUser-metoder MED ERROR Inkluderet
    /*
    public String createUser(CreateUserModel createUserModel)
    {
        
        // tjek om email er brugt
        boolean emailIsAvailable = flowsterRepository.isEmailAvailable(createUserModel.getEmail());
        
        // hvis email!=brugt tjekke om orgkode findes
        if(emailIsAvailable)
        {
            int organisationId = Integer.parseInt(createUserModel.getOrganisationAndJobType().substring(0,3));
            
            // tjek om orgkode er findes
            boolean organisationsIdExists = flowsterRepository.doesOrganisationdExist(organisationId);
            
            // if orgkode == findes - tjek om jobType findes
            if(organisationsIdExists)
            {
                int jobTypeId = Integer.parseInt(createUserModel.getOrganisationAndJobType().substring(3,5));
                
                boolean jobTypeIdExists = flowsterRepository.doesJobTypeExist(jobTypeId);
                
                // if jobType findes - tjek om password + confirmpassword match
                if(jobTypeIdExists)
                {
                    if(checkIfPasswordsMatch(createUserModel.getPassword(), createUserModel.getConfirmPassword()))
                    {
                    
                    }
                    else
                    {
                        error = "Kodeordene matcher ikke";
                    }
                }
                else
                {
                    error = "Organisationskode ukorrekt";
                }
            }
            else
            {
                error = "Organisationskode ukorrekt";
            }
        }
        else
        {
            error = "Emailen er allerede i brug";
        }
        
        // if passwords match == opret bruger - sæt til loggedInProjectManager ELLER loggedInTeamMember og gem i db
        
        
        return "redirect:/";
    }
    */
    
    
    public boolean checkIfPasswordsMatch(String password, String confirmPassword)
    {
        return password.equals(confirmPassword);
    }
    
    
    /**
     * Opretter nyt ProjectManager-obj ELLER TeamMember-obj ud fra createUserModel og jobTypeId
     * Dette obj. sættes ENTEN til loggedInProjectManager ELLER loggedInTeamMember
     * Derefter sættes User-obj som returneres til det oprettede obj.
     *
     *
     * @param createUserModel createUserModel-obj som nyt User-obj. oprettes ud fra
     * @return User User-obj som er sat til nyoprettet ProjectManager-obj ELLER TeamMember-obj
     * */
    public User createUserFromCreateUserModel(CreateUserModel createUserModel)
    {
        User newUser = null;
        
        String jobType =
                flowsterRepository.retrieveJobTypeFromJobTypeId(createUserModel.findJobTypeIdFromOrganisationAndJopType());
        
        if(jobType.equalsIgnoreCase("Projectmanager"))
        {
            loggedInProjectManager = projectManager.createProjectManagaerFromCreateUserModel(createUserModel);
            newUser = loggedInProjectManager;
        }
        
        else if(jobType.equalsIgnoreCase("Teammember"))
        {
            loggedInTeamMember = teamMember.createTeamMemberFromCreateUserModel(createUserModel);
            newUser = loggedInTeamMember;
        }
        
        return newUser;
    }
    
    
    
    
    
    
    
}
