package git7s.flashcardai.controller;

import git7s.flashcardai.Main;
import git7s.flashcardai.dao.UserDAO;
import git7s.flashcardai.model.User;
import git7s.flashcardai.model.UserManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * This class is the login GUI controller
 */
public class LoginController {
    /**
     * This label displays error when logging in
     */
    @FXML
    public Label errorLabel;
    /**
     * This textfield takes the username input
     */
    @FXML
    private TextField usernameField;
    /**
     * This textfield takes the password input
     */
    @FXML
    private PasswordField passwordField;
    /**
     * User Manager to access DB
     */
    private UserManager userManager;

    /**
     * This handles the button that takes the user to the Create Account gui
     */
    private enum LoginError {
        notFilledFields("Please fill in all fields"),
        failedLogin("Login Failed");
        final String description;

        LoginError(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    @FXML
    public void initialize() {
        userManager = new UserManager(new UserDAO());
    }

    @FXML
    private void handleCreateAccount() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/git7s/flashcardai/create-account-view.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene currentScene = usernameField.getScene();

            stage.setScene(new Scene(root, currentScene.getWidth(), currentScene.getHeight()));
            stage.setTitle("Create Account");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This takes the user through the login process, generating an alert box if not feasible (bad input)
     */
    @FXML
    private void handleLogin() {
        int username;
        User attemptUser;

        try {
            username = Integer.parseInt(usernameField.getText());
        } catch (NumberFormatException err) {
            errorLabel.setText(LoginError.failedLogin.getDescription());
            return;
        }

        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            errorLabel.setText(LoginError.notFilledFields.getDescription());
            return;
        }

        if (userManager.getUser(username) == null) {
            errorLabel.setText(LoginError.failedLogin.getDescription());
            return;
        }

        attemptUser = userManager.getUser(username);
        String attemptPassword = attemptUser.hashPassword(passwordField.getText(), attemptUser.getSalt());

        if (attemptUser.getPasswordHash().equals(attemptPassword)) {
            SuccessfulLogin(username);
        } else {
            errorLabel.setText(LoginError.failedLogin.getDescription());
        }

    }

    /**
     * If the userinput is good, login
     */
    private void SuccessfulLogin(int username){
        try {
            Main.loggedInUserID = username;
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
}
