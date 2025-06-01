package git7s.flashcardai.controller;

import git7s.flashcardai.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MainTestsLoginAuth {

    private User mainLoggedOut;
    private User mainLoggedIn;

    /**
     * Sets up test users.
     * One logged in user, one null (logged out).
     */
    @BeforeEach
    public void setUp() {
        mainLoggedIn = new User(12345, "Password", "Sam", "Smith", false);
        mainLoggedOut = null;

    }

    /**
     * Tests login state.
     * Verifies that a logged-in user is not null and a logged-out user is null.
     */
    @Test
    public void logInReturnAbility() {
        assertEquals(true, (mainLoggedIn!=null));
        assertEquals(null, (mainLoggedOut=null));
    }
}





