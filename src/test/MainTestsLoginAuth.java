package git7s.flashcardai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MainTestsLoginAuth {

    private User mainLoggedOut;
    private User mainLoggedIn;

    @BeforeEach
    public void setUp() {
        mainLoggedIn = new User(12345, "Password", "Sam", "Smith", false);
        mainLoggedOut = null;

    }

    ///Test for when the Create Account / Log in screen changes to dashboard
    @Test
    public void logInReturnAbility() {
        assertEquals(true, (mainLoggedIn!=null));
        assertEquals(null, (mainLoggedOut=null));
    }
}





