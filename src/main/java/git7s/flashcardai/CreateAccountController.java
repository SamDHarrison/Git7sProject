package git7s.flashcardai;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class CreateAccountController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private void handleGetStarted() {
        String username = usernameField.getText();
        int usernameID;
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Password Mismatch", "Passwords do not match.");
            return;
        }

        try {
            usernameID = Integer.parseInt(username);
        } catch (NumberFormatException err) {
            showAlert(Alert.AlertType.ERROR, "Invalid Username", "A valid username must be your student number.");
            return;
        }

        User newUser = new User(usernameID, password, firstName, lastName, false);

        if (Main.userDAO.getById(usernameID) == null){
            Main.userDAO.insert(newUser);
        } else {
            showAlert(Alert.AlertType.ERROR, "Username already used", "This username has already been taken");
            return;
        }

        showAlert(Alert.AlertType.INFORMATION, "Account Created", "Your account has been created successfully!");

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

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
