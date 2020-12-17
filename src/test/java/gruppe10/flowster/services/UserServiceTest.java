package gruppe10.flowster.services;

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

    @Test
    void checkIfLogInInfoIsValid()
    {
        UserService userService = new UserService();



        boolean expectedTrue = true;
        boolean expectedFalse = false;



    }





}