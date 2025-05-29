package git7s.flashcardai.controller;

import git7s.flashcardai.Main;
import git7s.flashcardai.dao.UserDAO;
import git7s.flashcardai.model.User;
import git7s.flashcardai.model.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.Optional;

public class MyAccountController {
    public Label editAccountTitle;
    public Label usernameField;
    public TextField firstNameField;
    public TextField lastNameField;
    public PasswordField oldPasswordField;
    public PasswordField passwordField;
    public PasswordField confirmPasswordField;
    public Button backButton;
    public Button saveAccount;
    public CheckBox changePasswordOption;
    public ColorPicker prefColourPicker;
    public Label errorLabel;
    public Button deleteAccountButton;
    public Button applyAdmin;
    private UserManager userManager;
    private User loggedInUser;

    public void initialize(){
        userManager = new UserManager(new UserDAO());
        loggedInUser = userManager.getUser(Main.loggedInUserID);
        userManager = new UserManager(new UserDAO());
        setPrefColours(Main.prefCol);
        fillDetails();

    }

    public void fillDetails(){

        usernameField.setText(String.valueOf(loggedInUser.getId()));
        firstNameField.setText(loggedInUser.getFirstName());
        lastNameField.setText(loggedInUser.getLastName());

        Color color = Color.web(loggedInUser.getPrefColour());
        prefColourPicker.setValue(color);
    }

    public void handleAttemptAdmin() {
        TextInputDialog dialog = new TextInputDialog("Default Text");
        dialog.setTitle("Admin Request");
        dialog.setHeaderText("Enter the administrative password:");
        dialog.setContentText("Password");

        // Show the dialog and capture the result
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()){
            String input = result.get();
            if (input.equals("AdminAdmin")){
                loggedInUser.setAdmin(true);
                errorLabel.setText("Admin Access Granted");
            }
        }
        else {
            errorLabel.setText("Incorrect Admin Password");
        }


    }

    public void handleDeleteAccount(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog("Default Text");
        dialog.setTitle("Delete Account");
        dialog.setHeaderText("Are you sure? This cannot be undone.");
        dialog.setContentText("Enter '1qaz2wsx' below:");

        // Show the dialog and capture the result
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()){
            String input = result.get();
            if (input.equals("1qaz2wsx")){
                userManager.delete(loggedInUser.getId());

                Main.loggedInUserID = -1;

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/git7s/flashcardai/login-view.fxml"));
                    Parent root = fxmlLoader.load();

                    Stage stage = (Stage) deleteAccountButton.getScene().getWindow();
                    stage.setScene(new Scene(root, Main.WIDTH, Main.HEIGHT));
                    stage.setTitle("Login");
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        else {
            errorLabel.setText("Incorrect Confirmation. Account not deleted.");
        }
    }

    public void handleSaveAccount(){
        if (changePasswordOption.isSelected()){
            if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || confirmPasswordField.getText().isEmpty() || oldPasswordField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                errorLabel.setText("You must fill in all boxes.");
                return;
            }

            if (!passwordField.getText().equals(confirmPasswordField.getText())){
                errorLabel.setText("Password do not match.");
                return;

            }


            User changedUser = new User(Main.loggedInUserID, confirmPasswordField.getText(), firstNameField.getText(), lastNameField.getText(), loggedInUser.isAdmin(), loggedInUser.getPrefColour());
            userManager.update(loggedInUser.getId(), changedUser);
            Main.loggedInUserID = loggedInUser.getId();
            errorLabel.setText("User Details Changed!");


        } else {
            if (usernameField.getText().isEmpty() || firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || oldPasswordField.getText().isEmpty()) {
                errorLabel.setText("You must fill in all boxes.");
                return;

            }

            if (!loggedInUser.authenticate(oldPasswordField.getText())){
                errorLabel.setText("Incorrect Password");
                return;

            }


            User changedUser = new User(Main.loggedInUserID, passwordField.getText(), firstNameField.getText(), lastNameField.getText(), loggedInUser.isAdmin(), loggedInUser.getPrefColour());
            userManager.update(loggedInUser.getId(), changedUser);
            Main.loggedInUserID = loggedInUser.getId();

            errorLabel.setText("User Details Changed!");
        }
    }

    public void handleDashboard() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/git7s/flashcardai/dashboard-view.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root, Main.WIDTH, Main.HEIGHT));
            stage.setTitle("Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void handleChangePasswordOption(){

        boolean opt = changePasswordOption.isSelected();

            passwordField.setVisible(opt);
            confirmPasswordField.setVisible(opt);

        if (opt) {
            oldPasswordField.setPromptText("Old Password");
        }
        else {
            oldPasswordField.setPromptText("Current Password");
        }
    }

    public void handleChangeColour() {
        Color selectedColor = prefColourPicker.getValue();

        String hex = String.format("#%02X%02X%02X",
                (int) (selectedColor.getRed() * 255),
                (int) (selectedColor.getGreen() * 255),
                (int) (selectedColor.getBlue() * 255)
        );

        loggedInUser.setPrefColour(hex);
        userManager.update(loggedInUser.getId(), loggedInUser);
    }

    private void setPrefColours(String colour){
        String s = "-fx-background-color: " + colour + "; -fx-text-fill: white; -fx-font-weight: bold;";

        deleteAccountButton.setStyle(s);
        backButton.setStyle(s);
        applyAdmin.setStyle(s);
        saveAccount.setStyle(s);


    }
}