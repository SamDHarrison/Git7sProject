package git7s.flashcardai.controller;

import git7s.flashcardai.Main;
import git7s.flashcardai.dao.UserDAO;
import git7s.flashcardai.model.User;
import git7s.flashcardai.model.UserManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * A class that handles the Create Account GUI
 */
public class CreateAccountController {
    /**
     * Displays any errors that occur when creating account
     */
    @FXML
    public Label errorLabel;
    /**
     * Textfield for the username input
     */
    @FXML
    private TextField usernameField;
    /**
     * Textfield for the password input
     */
    @FXML
    private PasswordField passwordField;
    /**
     * Textfield for the confirmed password input
     */
    @FXML
    private PasswordField confirmPasswordField;
    /**
     * Textfield for the First Name input
     */
    @FXML
    private TextField firstNameField;
    /**
     * Textfield for the Last Name input
     */
    @FXML
    private TextField lastNameField;
    /**
     * Manager for user objects
     */
    private UserManager userManager;
    /**
     * Enum that holds error text
     */
    private enum CreateAccountError {
        notFilledFields("Please fill in all fields"),
        notConfirmedPassword("Passwords are not matching"),
        notNumericalID("Your Student ID must be all numerical digits"),
        notSatisfactoryPassword("Your password must be over 5 digits"),
        notUniqueUsername("The specified Student ID already exists");
        final String description;

        CreateAccountError(String description) {
            this.description = description;
        }
        public String getDescription(){
            return description;
        }
    }

    /**
     * Initialise Controller
     */
    @FXML
    public void initialize() {
        userManager = new UserManager(new UserDAO());
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
        if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText(CreateAccountError.notFilledFields.getDescription());
            return;
        }
        // Makes sure that password and confirmation match
        if (!password.equals(confirmPassword)) {
            errorLabel.setText(CreateAccountError.notConfirmedPassword.getDescription());
            return;
        }
        // Attempt to parse username (student number) into an integer
        try {
            usernameID = Integer.parseInt(username);
        } catch (NumberFormatException err) {
            errorLabel.setText(CreateAccountError.notNumericalID.getDescription());
            return;
        }
        //Check if username already exists
        if (userManager.getUser(usernameID)!=null) {
            errorLabel.setText(CreateAccountError.notUniqueUsername.getDescription());
            return;
        }
        // Create the User object
        userManager.addUser(new User(usernameID, password, firstName, lastName, false));
        Main.loggedInUserID = usernameID;

        // Navigate to Dashboard view
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/git7s/flashcardai/dashboard-view.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            stage.setTitle("Flashcard AI - Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A method that goes back to the login screen
     */
    @FXML
    private void handleBackToLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/git7s/flashcardai/login-view.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
