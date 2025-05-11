package git7s.flashcardai;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * A class that handles the Create Account GUI
 */
public class CreateAccountController {
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

        // Makes sure that that no fields are empty
        if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill in all fields.");
            return;
        }

        // Makes sure that password and confirmation match
        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Password Mismatch", "Passwords do not match.");
            return;
        }

        // Attempt to parse username (student number) into an integer
        try {
            usernameID = Integer.parseInt(username);
        } catch (NumberFormatException err) {
            showAlert(Alert.AlertType.ERROR, "Invalid Username", "A valid username must be your student number.");
            return;
        }

        // Create new User object
        User newUser = new User(usernameID, password, firstName, lastName, false);

        if (Main.userDAO.getById(usernameID) == null){
            Main.userDAO.insert(newUser);
        } else {
            showAlert(Alert.AlertType.ERROR, "Username already used", "This username has already been taken");
            return;
        }
        Main.loggedInUser = newUser;
        showAlert(Alert.AlertType.INFORMATION, "Account Created", "Your account has been created successfully!");

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

    /**
     * Simple alert GUI method
     * @param alertType The type of alert
     * @param title Title of the alert box
     * @param message Message of the alert box
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
