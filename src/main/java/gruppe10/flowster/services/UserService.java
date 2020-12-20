package gruppe10.flowster.services;

import gruppe10.flowster.models.users.TeamMember;
import gruppe10.flowster.models.users.ProjectManager;
import gruppe10.flowster.models.users.User;
import gruppe10.flowster.repositories.FlowsterRepository;
import gruppe10.flowster.repositories.OrganisationRepository;
import gruppe10.flowster.viewModels.user.CreateUserViewModel;
import gruppe10.flowster.viewModels.user.LogInViewModel;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

@Service
public class UserService
{
    public static User loggedInUser = null;
    ProjectManager projectManager = new ProjectManager();
    TeamMember teamMember = new TeamMember();
    
    FlowsterRepository flowsterRepository = new FlowsterRepository();
    OrganisationRepository organisationRepository = new OrganisationRepository();
    
    String error = null;
    
    // getters + setters
    public String getError()
    {
        return error;
    }
    
    // ANDRE METODER
    
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
        
        error = "Der er allerede oprettet en bruger med denne email";
        
        boolean dataIsValid = false;
        
        // tjek om email er brugt
        boolean emailIsAvailable = flowsterRepository.isEmailAvailable(createUserViewModel.getEmail());
    
        // hvis email!=brugt
        if(emailIsAvailable)
        {
            error = "Organisationskoden findes ikke - indtast venligst koden du har fået udleveret af din virksomhed";
            
            int organisationId = createUserViewModel.findOrganisationId();
    
            // tjek om orgkode findes
            boolean organisationsIdExists = flowsterRepository.doesOrganisationExist(organisationId);
            
            // if orgkode == findes
            if(organisationsIdExists)
            {
                // her er error stadig at org-koden ikke findes - fordi jobType er en del af orgkoden
                
                int jobTypeId = createUserViewModel.findJobTypeId();
                
                // tjek om jobType findes
                boolean jobTypeIdExists = flowsterRepository.doesJobTypeExist(jobTypeId);
    
                // if jobType findes
                if(jobTypeIdExists)
                {
                    error = "Adgangskoderne matcher ikke - angiv venligst igen";
                    
                    // tjek om password + confirmpassword match
                    if(checkIfPasswordsMatch(createUserViewModel.getPassword(), createUserViewModel.getConfirmPassword()))
                    {
                        // her står vi KUN hvis alle tjek er successfulde
                        // derfor er der INGEN fejl-meddelelse
                        error = null;
                        
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
    
    public void insertNewUserIntoDbAndSetLoggedInUser(CreateUserViewModel createUserViewModel)
    {
        User newUser = createUserFromCreateUserModel(createUserViewModel);
    
        // tilføj til newUser til db
        organisationRepository.insertUserIntoDb(newUser);
    }
    
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
        error = "Der er ikke oprettet en bruger med denne email";
        
        boolean logInInfoIsValid = false;
        
        String email = logInViewModel.getEmail();
        
        //tjek om email eksisterer i emails-table
        boolean emailExistsInDb = !(flowsterRepository.isEmailAvailable(email));
    
        //  = if(emailExists): finde org_name ud fra email
        if(emailExistsInDb)
        {
            error = "Adgangskoden mathcer ikke emailen";
            // finder organisationName udfra email
            String organisationName = flowsterRepository.retrieveOrganisationNameFromEmail(email);
    
            // konverterer organisationName til at være dbName - dvs. små bogstaver og underscore hvor mellemrum
            String dbName = convertOrganisationNameToDbName(organisationName);
    
            // finder User-obj i users-tabel i dbName-db ud fra logInViewModel - HVIS password ikke matcher email, er
            // loggedInUser null
            loggedInUser = organisationRepository.retrieveUserFromDb(logInViewModel, dbName);
            
            if(loggedInUser != null)
            {
                error = null;
                
                logInInfoIsValid = true;
            }
        }
        
        return logInInfoIsValid;
    }
    
    public void resetLoggedInUser()
    {
        loggedInUser = null;
    }
    
    /**
     * Finder/opretter orgDbName ud fra loggedInUser
     *
     * @return String oprettede orgDbName
     * */
    public String findOrgDbName()
    {
        if(loggedInUser != null)
        {
            String orgName = flowsterRepository.retrieveOrganisationNameFromEmail(loggedInUser.getEmail());
    
            return convertOrganisationNameToDbName(orgName);
        }
        return null;
    }
    
  
}
