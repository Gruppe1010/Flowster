package gruppe10.flowster.services;

import gruppe10.flowster.models.users.TeamMember;
import gruppe10.flowster.models.users.ProjectManager;
import gruppe10.flowster.models.users.User;
import gruppe10.flowster.repositories.FlowsterRepository;
import gruppe10.flowster.repositories.OrganisationRepository;
import gruppe10.flowster.repositories.TeamRepository;
import gruppe10.flowster.viewModels.user.CreateUserViewModel;
import gruppe10.flowster.viewModels.user.LogInViewModel;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;

public class UserService
{
    public static User loggedInUser = null;
    
    /*
    public ProjectManager loggedInProjectManager = null;
    public TeamMember loggedInTeamMember = null;
     */
    
    String error; // TODO måske lav denne?
    ProjectManager projectManager = new ProjectManager();
    TeamMember teamMember = new TeamMember();
    
    FlowsterRepository flowsterRepository = new FlowsterRepository();
    OrganisationRepository organisationRepository = new OrganisationRepository();
    TeamRepository teamRepository = new TeamRepository();
    
    HashMap<User, String> userHashMap = new HashMap<>();
    
    
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
    public CreateUserViewModel createCreateUserViewModelFromForm(WebRequest dataFromCreateUserForm)
    {
        // fordi vi har sat feltet "organisationAndJobType" som required i html, bliver nullpointeren handlet der -
        // derfor gør vi ikke yderligere her
        return new CreateUserViewModel(
                dataFromCreateUserForm.getParameter("organisation-and-job-type"),
                Double.parseDouble(dataFromCreateUserForm.getParameter("manhours")),
                dataFromCreateUserForm.getParameter("firstname"),
                dataFromCreateUserForm.getParameter("surname"),
                dataFromCreateUserForm.getParameter("email"),
                dataFromCreateUserForm.getParameter("password"),
                dataFromCreateUserForm.getParameter("confirm-password"));
    }
    
    public boolean checkDataFromCreateUserViewModel(CreateUserViewModel createUserViewModel)
    {
    
        /* createUserForm i index-html skal indeholde:
         *
         * organisationAndJobType = required, number, min+max 5 char
         *
         * firstname + surname = required, only letters, max-length: 100
         *
         * email = required, valid email-input
         *
         * password + confirmpassword = required, uppercase, lowercase, number/specialchar, 8-length
         *
         * */
        boolean dataIsValid = false;
        
        // tjek om email er brugt
        boolean emailIsAvailable = flowsterRepository.isEmailAvailable(createUserViewModel.getEmail());
    
        // hvis email!=brugt tjekke om orgkode findes
        if(emailIsAvailable)
        {
            int organisationId = createUserViewModel.findOrganisationId();
    
            // tjek om orgkode er findes
            boolean organisationsIdExists = flowsterRepository.doesOrganisationExist(organisationId);
    
            // if orgkode == findes - tjek om jobType findes
            if(organisationsIdExists)
            {
                int jobTypeId = createUserViewModel.findJobTypeId();
    
                boolean jobTypeIdExists = flowsterRepository.doesJobTypeExist(jobTypeId);
    
                // if jobType findes - tjek om password + confirmpassword match
                if(jobTypeIdExists)
                {
                    if(checkIfPasswordsMatch(createUserViewModel.getPassword(), createUserViewModel.getConfirmPassword()))
                    {
                        
                        /* TODO:  TJEK OM USER BLIVER REGISTRERET SOM ENTEN
                        // boolean newUserIsTeamMember = newUser instanceof TeamMember;
                        // boolean newUserIsProjectManager = newUser instanceof ProjectManager;
                        // System.out.println("TEST in userService: teammember: " + newUserIsTeamMember + "
                        // projectmanager: " + newUserIsProjectManager);
                        
                         */
                        
                        
                        // her står vi KUN hvis alle tjek er successfulde
                        dataIsValid = true;
                    }
                }
            }
        }
        return dataIsValid;
    }
    
    public boolean checkIfPasswordsMatch(String password, String confirmPassword)
    {
        return password.equals(confirmPassword);
    }
    
    public void insertNewUserIntoDb(CreateUserViewModel createUserViewModel)
    {
        User newUser = createUserFromCreateUserModel(createUserViewModel);
    
        // tilføj til newUser til db
        organisationRepository.insertUserIntoDb(newUser);
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
    
    
    /**
     * Opretter nyt ProjectManager-obj ELLER TeamMember-obj ud fra createUserModel og jobTypeId
     * Dette obj. sættes ENTEN til loggedInProjectManager ELLER loggedInTeamMember
     * Derefter sættes User-obj som returneres til det oprettede obj.
     *
     *
     * @param createUserViewModel createUserModel-obj som nyt User-obj. oprettes ud fra
     * @return User User-obj som er sat til nyoprettet ProjectManager-obj ELLER TeamMember-obj
     * */
    public User createUserFromCreateUserModel(CreateUserViewModel createUserViewModel)
    {
        String jobType =
                flowsterRepository.retrieveJobTypeFromJobTypeId(createUserViewModel.findJobTypeId());
        
        // TODO ud i metode
        if(jobType.equalsIgnoreCase("Projektleder"))
        {
            loggedInUser = projectManager.createProjectManagerFromCreateUserModel(createUserViewModel);
        }
        
        else if(jobType.equalsIgnoreCase("Almen medarbejder"))
        {
            loggedInUser = teamMember.createTeamMemberFromCreateUserModel(createUserViewModel);
        }
    
        // TODO hvis vi får brug for loggedIndProjectManager og -TeamMember
        //  setLoggedInProjectManagerOrTeamMemberToLoggedInUser();
        
        return loggedInUser;
    }
    
    // TODO hvis vi får brug for loggedInProjectManager og -TeamMember
    /*
    public void setLoggedInProjectManagerOrTeamMemberToLoggedInUser()
    {
        if(jobType.equalsIgnoreCase("Projektleder"))
        {
            loggedInProjectManager = projectManager.createProjectManagaerFromCreateUserModel(createUserViewModel);
            loggedInUser = loggedInProjectManager;
        }
    
        else if(jobType.equalsIgnoreCase("Almen medarbejder"))
        {
            loggedInTeamMember = teamMember.createTeamMemberFromCreateUserModel(createUserViewModel);
            loggedInUser = loggedInTeamMember;
        }
    
    }
     */
    
    /**
     * Finder className på loggedInUser og omdanner til url-del, så den ryger til rigtige requestMapping
     *
     * @return String fundne className-url-del
     * */
    public String retrieveClassNameUrl()
    {
        // getClass == finder klassen - getSimpleName == laver klassenavn til String
        String className = loggedInUser.getClass().getSimpleName();
        
        // laver første forbogstav om til småt  - da ord i url'en står i camelCase
        return Character.toLowerCase(className.charAt(0)) + className.substring(1);
    }
    
    public LogInViewModel createLogInViewModelFromForm(WebRequest dataFromLogInForm)
    {
        LogInViewModel logInViewModel = new LogInViewModel(
                dataFromLogInForm.getParameter("email"),
                dataFromLogInForm.getParameter("password"));
    
        return logInViewModel;
    }
    
    /**
     * Konverterer organisationName til db-name
     *
     * @param organisationName organisationName som skal konverteres til db-name
     * @return String konverterede db-name
     * */
    public String convertOrganisationNameToDbName(String organisationName)
    {
        // laver underscore hvor der er mellemrum i orgName
        String convertedOrganisationName = organisationName.replaceAll(" ", "_");
    
        // laver til små bogstaver
        convertedOrganisationName = convertedOrganisationName.toLowerCase();
        
        return "flowster_" + convertedOrganisationName;
    }
 
    public boolean checkIfLogInInfoIsValid(LogInViewModel logInViewModel)
    {
        boolean logInInfoIsValid = false;
        
        String email = logInViewModel.getEmail();
        
        //tjek om email eksisterer i emails-table
        boolean emailExistsInDb = !(flowsterRepository.isEmailAvailable(email));
    
        //  = if(emailExists): finde org_name ud fra email
        if(emailExistsInDb)
        {
            // finder organisationName udfra email
            String organisationName = flowsterRepository.retrieveOrganisationNameFromEmail(email);
    
            // konverterer organisationName til at være dbName - dvs. små bogstaver og underscore hvor mellemrum
            String dbName = convertOrganisationNameToDbName(organisationName);
    
            // finder User-obj i users-tabel i dbName-db ud fra logInViewModel
            loggedInUser = organisationRepository.retrieveUserFromDb(logInViewModel, dbName);
            
            if(loggedInUser != null)
            {
                logInInfoIsValid = true;
            }
        }
        
        return logInInfoIsValid;
    }
    
    public void resetLoggedInUser()
    {
        loggedInUser = null;
    }
    
    public void updateJoinedTeamsList()
    {
        
        String dbName = convertOrganisationNameToDbName
                                (flowsterRepository.retrieveOrganisationNameFromEmail(loggedInUser.getEmail()));
    
        loggedInUser.setJoinedTeamsList(teamRepository.retrieveTeamsArrayListFromUserId(dbName,
                loggedInUser.getId()));
    }
    
    
    
}
