package git7s.flashcardai.controller;

import git7s.flashcardai.AppDefaults;
import git7s.flashcardai.dao.UserDAO;
import git7s.flashcardai.model.UserManager;
import git7s.flashcardai.service.SessionService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controller for the login screen.
 * Handles user authentication and navigation to account creation or dashboard.
 */
public class LoginController extends AbstractController implements IController{
    /**
     * Protected default constructor.
     * <p>
     * This constructor is intentionally protected because this class is abstract
     * and should only be subclassed.
     * </p>
     */
    protected LoginController() {
    }
    /**
     * Labels
     */
    @FXML
    public Label errorLabel, createAccountButton;
    /**
     * Login Button
     */
    @FXML
    public Button loginButton;
    /**
     * This text field takes the username input
     */
    @FXML
    private TextField usernameField;
    /**
     * This text field takes the password input
     */
    @FXML
    private PasswordField passwordField;
    /**
     * User Manager to access DB
     */
    private UserManager userManager;


    /**
     * Initializes the controller and sets up the user manager.
     */
    @FXML
    public void initialize() {
        userManager = new UserManager(new UserDAO());}

    @Override
    public void setupUIData() {
        //Nil Required yet
    }

    @Override
    public void handleBackButton() {
        handleCreateAccount();
    }

    /**
     * Navigates to the Create Account screen.
     */
    @FXML
    private void handleCreateAccount() {
        changeView(AppDefaults.ViewTitles.CREATE_ACCOUNT_VIEW, createAccountButton);
    }

    /**
     * This takes the user through the login process, generating an alert box if not feasible (bad input)
     */
    @FXML
    private void handleLogin() {
        int username;

        try {
            username = Integer.parseInt(usernameField.getText());
        } catch (NumberFormatException err) {
            errorLabel.setText(AppDefaults.ErrorMessages.NOT_NUMERICAL_ID.get());
            return;
        }

        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            errorLabel.setText(AppDefaults.ErrorMessages.NOT_FILLED_FIELDS.get());
            return;
        }

        if (userManager.getUser(username) == null) {
            errorLabel.setText(AppDefaults.ErrorMessages.INCORRECT_PASSWORD.get());
            return;
        }

        if (userManager.getUser(username).authenticate(passwordField.getText())) {
            SuccessfulLogin(username);
        } else {
            errorLabel.setText(AppDefaults.ErrorMessages.INCORRECT_PASSWORD.get());
        }

    }

    /**
     * If the user input is good, login
     */
    private void SuccessfulLogin(int username){
        SessionService.getInstance().login(username);

        changeView(AppDefaults.ViewTitles.DASHBOARD_VIEW, loginButton);
    }

}
