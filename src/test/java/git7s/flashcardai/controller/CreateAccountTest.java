package git7s.flashcardai.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests the logic for creating an account, checking edge cases like missing fields or password mismatch.
 */
public class CreateAccountTest {

    private String[] notConfirmed;
    private String[] correct;
    private String[] noFirstName;
    private String[] noLastName;
    private String[] noUsername;

    /**
     * Sets up different input cases for testing account creation validation.
     */
    @BeforeEach
    public void setUp() {
        notConfirmed = new String[] {"123", "password", "Steve", "Smith", "XYZ"};
        correct = new String[]{"123", "password", "Steve", "Smith", "password"};
        noFirstName = new String[]{"123", "password", "", "Smith", "password"};
        noLastName = new String[]{"123", "password", "Steve", "", "password"};
        noUsername = new String[]{"ABC", "password", "Steve", "Smith", "password"};

    }

    /**
     * Tests the createAccount method for various edge cases and one valid case.
     */
    @Test
    public void createAccountTest() {
        assertEquals(true, createAccount(correct));
        assertEquals(false, createAccount(notConfirmed));
        assertEquals(false, createAccount(noFirstName));
        assertEquals(false, createAccount(noLastName));
        assertEquals(false, createAccount(noUsername));
    }

    /**
     * Validates the creation of a new account with basic checks:
     * - no field is empty
     * - password matches confirmation
     * - username is a valid integer (student ID)
     *
     * @param s String array of [username, password, firstName, lastName, confirmPassword]
     * @return true if all conditions are met, false otherwise
     */
    private boolean createAccount(String[] s) {
        String username = s[0];
        String password = s[1];
        String firstName = s[2];
        String lastName= s[3];
        String confirmPassword = s[4];
        int usernameID;
        // Makes sure that that no fields are empty
        if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {

            return false;
        }

        // Makes sure that password and confirmation match
        if (!password.equals(confirmPassword)) {

            return false;
        }

        // Attempt to parse username (student number) into an integer
        try {
            usernameID = Integer.parseInt(username);
        } catch (NumberFormatException err) {

            return false;
        }

        return true;
    }
}