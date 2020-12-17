package gruppe10.flowster.services;

import gruppe10.flowster.viewModels.user.CreateUserViewModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest
{

    @Test
    void checkIfPasswordsMatch()
    {
        // arrange
        UserService userService = new UserService();

        String password = "Hejhej123";
        String repeatedPassword = "Hejhej123";
        String differentPassword = "HejHej123";

        boolean expectedTrue = true;
        boolean expectedFalse = false;

        // act

        // password + repeatedPassword  ---> expectedTrue
        boolean result1 = userService.checkIfPasswordsMatch(password, repeatedPassword);

        // password + differentPassword ---> expectedFalse
        boolean result2 = userService.checkIfPasswordsMatch(password, differentPassword);

        // assert
        assertEquals(expectedTrue, result1);
        assertEquals(expectedFalse, result2);
    }

    /*
    * Vi tester ikke ugyldige input, fx manhours = 100 (som skal være mellem 0-99 for at være gyldige) eller manhours
    *  = "hej", fordi vi har håndteret denne slags ugyldige input direkte i manhours-inputtet formen i html. via
    * min="0" max="99" og type="number" step=".1"
    *
    * Vi har valgt ikke at tage alle kombiniationsmuligheder
    *
    *
    * */
    
    
    @Test
    void checkDataFromCreateUserViewModel()
    {
        // ARRANGE
        UserService userService = new UserService();
        boolean expectedTrue = true;
        boolean expectedFalse = false;
    
        // værdier som er ligegyldige for metoden - håndteres af formen
        double manhours = 13.5;
        String firstname = "Tobi";
        String surname = "BojBoy";
   
        // reelle testværdier: organisationAndJobType, email, password + confirmPasword
        String organisationAndJobType = "10001";
        String invalidOrganisationAndJobType = "10003";
   
        String email = "bojboy@tobi.dk";
        String nonAvailableEmail = "vibej@hotmail.com";
        
        String password = "Hejhej123";
        String confirmPassword = "Hejhej123";
        String invalidPassword = "HejHej123";
    
        // KUN valide data
        CreateUserViewModel viewModel1 = new CreateUserViewModel(organisationAndJobType, manhours, firstname,
                surname, email, password, confirmPassword);
        
        //KUN ugyldig organsiationAndJobType
        CreateUserViewModel viewModel2 = new CreateUserViewModel(invalidOrganisationAndJobType, manhours, firstname,
                surname, email, password, confirmPassword);
        
        // KUN ugyldig email
        CreateUserViewModel viewModel3 = new CreateUserViewModel(organisationAndJobType, manhours, firstname,
                surname, nonAvailableEmail, password, confirmPassword);
        
        // KUN ugyldigt password-match
        CreateUserViewModel viewModel4 = new CreateUserViewModel(organisationAndJobType, manhours, firstname,
                surname, email, password, invalidPassword);
        
        // ACT
        boolean result1 = userService.checkDataFromCreateUserViewModel(viewModel1);
        boolean result2 = userService.checkDataFromCreateUserViewModel(viewModel2);
        boolean result3 = userService.checkDataFromCreateUserViewModel(viewModel3);
        boolean result4 = userService.checkDataFromCreateUserViewModel(viewModel4);
        
        // ASSERT (data == hvad den er --> expected result)
        // alle input == gyldig --> true
        assertEquals(expectedTrue, result1);
        
        // organisationAndJobType == ugyldig --> false
        assertEquals(expectedFalse, result2);
        
        // email == ugyldig --> false
        assertEquals(expectedFalse, result3);
        
        // password + confirmPassword == no match --> false
        assertEquals(expectedFalse, result4);
    }
    
    
    /*
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
    */





}