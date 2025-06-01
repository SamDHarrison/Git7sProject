package git7s.flashcardai.controller;
/// Imports
import git7s.flashcardai.AppDefaults;
import git7s.flashcardai.dao.UserDAO;
import git7s.flashcardai.model.User;
import git7s.flashcardai.model.UserManager;
import git7s.flashcardai.service.SessionService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controller for the "Create Account" screen.
 * Handles user input, validation, and account creation logic.
 */
public class CreateAccountController extends AbstractController implements IController{
    /**
     * Displays any errors that occur when creating account
     */
    @FXML
    private Label errorLabel;
    /**
     * Button for the login screen
     */
    @FXML
    private Button backButton, getStartedButton;
    /**
     * Password Fields
     */
    @FXML
    private PasswordField passwordField, confirmPasswordField;
    /**
     * Textfields
     */
    @FXML
    private TextField firstNameField, lastNameField, usernameField;
    /**
     * Manager for user objects
     */
    private UserManager userManager;
    /**
     * Initializes the controller and sets up the user manager.
     */
    @FXML
    public void initialize() {
        userManager = new UserManager(new UserDAO());
        setupUIData();
    }
    /**
     * Sets up UI Data
     */
    @Override
    public void setupUIData(){
        //Nothing to do here yet
    }
    /**
     * Method to return the user to the login screen.
     */
    @FXML
    @Override
    public void handleBackButton() {
        changeView(AppDefaults.ViewTitles.LOGIN_VIEW, backButton);
    }
    /**
     * This is the method called when the get started button is pressed, generating a user if inputs are correct
     */
    @FXML
    private void handleGetStarted() {
        String username = usernameField.getText();
        int usernameID;
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        // Makes sure that no fields are empty
        if (checkFieldsEmpty(usernameField, passwordField, confirmPasswordField, firstNameField, lastNameField)) {
            errorLabel.setText(AppDefaults.ErrorMessages.NOT_FILLED_FIELDS.get());
            return;
        }
        // Makes sure that password and confirmation match
        if (!password.equals(confirmPassword)) {
            errorLabel.setText(AppDefaults.ErrorMessages.NOT_CONFIRMED_PASSWORD.get());
            return;
        }
        // Attempt to parse username (student number) into an integer
        try {
            usernameID = Integer.parseInt(username);
        } catch (NumberFormatException err) {
            errorLabel.setText(AppDefaults.ErrorMessages.NOT_NUMERICAL_ID.get());
            return;
        }
        //Check if username already exists
        if (userManager.getUser(usernameID)!=null) {
            errorLabel.setText(AppDefaults.ErrorMessages.NOT_UNIQUE_USERNAME.get());
            return;
        }
        // Create the User object
        userManager.addUser(new User(usernameID, password, firstName, lastName, false, "#60C3D8"));
        SessionService.getInstance().loggedInID = usernameID;

        changeView(AppDefaults.ViewTitles.DASHBOARD_VIEW, getStartedButton);
    }





}
