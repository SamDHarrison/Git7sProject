import git7s.flashcardai.Main;
import git7s.flashcardai.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreateAccountTest {

    private String[] notConfirmed;
    private String[] correct;
    private String[] noFirstName;
    private String[] noLastName;
    private String[] noUsername;

    @BeforeEach
    public void setUp() {
        notConfirmed = new String[] {"123", "password", "Steve", "Smith", "XYZ"};
        correct = new String[]{"123", "password", "Steve", "Smith", "password"};
        noFirstName = new String[]{"123", "password", "", "Smith", "password"};
        noLastName = new String[]{"123", "password", "Steve", "", "password"};
        noUsername = new String[]{"ABC", "password", "Steve", "Smith", "password"};

    }
    @Test
    public void createAccountTest() {
        assertEquals(true, createAccount(correct));
        assertEquals(false, createAccount(notConfirmed));
        assertEquals(false, createAccount(noFirstName));
        assertEquals(false, createAccount(noLastName));
        assertEquals(false, createAccount(noUsername));
    }

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