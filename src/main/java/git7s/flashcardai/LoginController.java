package git7s.flashcardai;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import git7s.flashcardai.Main;


public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

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


    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        int usernameID = Integer.parseInt(username);

        /// Database Check
        if (Main.userDAO.getById(usernameID) != null )  {
            if(Main.userDAO.getById(usernameID).getPasswordHash().equals(password)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login Successful");
                alert.setHeaderText(null);
                alert.setContentText("Welcome, " + username + "!");
                alert.showAndWait();
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