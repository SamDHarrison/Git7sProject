package git7s.flashcardai;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class is the login GUI controller
 */
public class LoginController {
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
     * This handles the button that takes the user to the Create Account gui
     */
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
        int username = Integer.parseInt(usernameField.getText());

        /// Database Check
        if (Main.userDAO.getById(username) != null )  {
            User attemptUser = Main.userDAO.getById(username);
            String attemptPassword = attemptUser.hashPassword(passwordField.getText(), attemptUser.getSalt());
            if(attemptUser.getPasswordHash().equals(attemptPassword)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login Successful");
                alert.setHeaderText(null);
                alert.setContentText("Welcome, " + username + "!");
                alert.showAndWait();
                Main.loggedInUser = attemptUser;
                SuccessfulLogin();
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Incorrect username or password.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Incorrect username or password.");
            alert.showAndWait();
        }
    }

    /**
     * If the userinput is good, login
     */
    private void SuccessfulLogin(){
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
}