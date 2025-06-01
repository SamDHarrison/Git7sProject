package git7s.flashcardai.controller;
///Imports
import git7s.flashcardai.AppDefaults;
import git7s.flashcardai.service.AdminService;
import git7s.flashcardai.dao.UserDAO;
import git7s.flashcardai.model.User;
import git7s.flashcardai.model.UserManager;
import git7s.flashcardai.service.SessionService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Controller Class that handles the Account Edit View
 */
public class AccountController extends AbstractController implements IController{
    /**
     * Public default constructor.
     */
    public AccountController() {
    }
    /**
     * Scene Labels
     */
    @FXML
    private Label usernameLabel, errorLabel, accountViewTitle;
    /**
     * Scene TextFields
     */
    @FXML
    private TextField firstNameField, lastNameField;
    /**
     * Scene Password Fields
     */
    @FXML
    private PasswordField oldPasswordField, passwordField, confirmPasswordField;
    /**
     * Scene Buttons
     */
    @FXML
    private Button backButton, deleteAccountButton, applyAdminButton, saveAccountButton, generateFakeResultsButton, databaseConsoleButton;
    /**
     * Scene Checkbox
     */
    @FXML
    private CheckBox changePasswordOption;
    /**
     * Scene Colour picker
     */
    @FXML
    private ColorPicker prefColourPicker;
    /**
     * User Manager for getting user details
     */
    private UserManager userManager;

    /**
     * Standard Method - Initialise the controller and view
     */
    @Override
    public void initialize(){
        userManager = new UserManager(new UserDAO());
        setupUIData();
    }

    /**
     * Add Data to Scene Nodes
     */
    @Override
    public void setupUIData() {
        setPrefColours(backButton, applyAdminButton, saveAccountButton, generateFakeResultsButton, databaseConsoleButton);

        User user = userManager.getUser(SessionService.getInstance().loggedInID);
        accountViewTitle.setText("Editing Account: " + user.getFullName());
        usernameLabel.setText(String.valueOf(user.getId()));
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        prefColourPicker.setValue(Color.web(user.getPrefColour()));

        if (userManager.getUser(SessionService.getInstance().loggedInID).isAdmin()) {
            databaseConsoleButton.setVisible(true);
            generateFakeResultsButton.setVisible(true);
            applyAdminButton.setVisible(false);
        }
    }

    /**
     * Back to the Dashboard
     */
    @Override
    public void handleBackButton() {
        changeView(AppDefaults.ViewTitles.DASHBOARD_VIEW, backButton);
    }

    /**
     * Button Action when user requests admin access.
     */
    public void handleAttemptAdmin() {
        Optional<String> adminRequest = inputDialogue("Admin Request", "Input the Admin Password");

        if (adminRequest.isPresent()) {
            String input = adminRequest.get();
            if (AdminService.getInstance().handleAdminRequest(input)){
                User user = userManager.getUser(SessionService.getInstance().loggedInID);
                user.setAdmin(true);
                userManager.update(user);
                errorLabel.setText(AppDefaults.SuccessMessages.ADMIN_GRANTED.get());

                initialize();

                User admin = userManager.getUser(SessionService.getInstance().loggedInID);
                if (admin.isAdmin()) {
                    System.out.println("ADMIN");
                }
            } else {
                errorLabel.setText(AppDefaults.ErrorMessages.ADMIN_DENIED.get());
            }
        }
    }

    /**
     * Button Action when the user tries to delete their account
     */
    public void handleDeleteAccount() {

        Optional<String> result = inputDialogue("Delete Account", "Enter your password to confirm");

        if (result.isPresent() && userManager.getUser(SessionService.getInstance().loggedInID).authenticate(result.get())){
            userManager.delete(SessionService.getInstance().loggedInID);
            logoutCurrentUser(deleteAccountButton);
        }
        else {
            errorLabel.setText(AppDefaults.ErrorMessages.ACCOUNT_DELETE_FAIL.get());
        }
    }

    /**
     * Save button action, depending on the change password checkbox, updates the current user
     */
    public void handleSaveAccount(){
        if (changePasswordOption.isSelected()){
            if (checkFieldsEmpty(firstNameField, lastNameField, confirmPasswordField, oldPasswordField, passwordField)) {
                errorLabel.setText(AppDefaults.ErrorMessages.NOT_FILLED_FIELDS.get());
                return;
            }

            if (!passwordField.getText().equals(confirmPasswordField.getText())){
                errorLabel.setText(AppDefaults.ErrorMessages.NOT_CONFIRMED_PASSWORD.get());
                return;
            }

            if (!userManager.getUser(SessionService.getInstance().loggedInID).authenticate(oldPasswordField.getText())){
                errorLabel.setText(AppDefaults.ErrorMessages.INCORRECT_PASSWORD.get());
                return;
            }

            User thisUser = userManager.getUser(SessionService.getInstance().loggedInID);
            User changedUser = new User(thisUser.getId(), confirmPasswordField.getText(), firstNameField.getText(), lastNameField.getText(), thisUser.isAdmin(), thisUser.getPrefColour());
            userManager.update(changedUser);
            errorLabel.setText(AppDefaults.SuccessMessages.ACCOUNT_CHANGED.get());

        } else {
            if (checkFieldsEmpty(oldPasswordField, firstNameField, lastNameField)) {
                errorLabel.setText(AppDefaults.ErrorMessages.NOT_FILLED_FIELDS.get());
                return;
            }

            if (!userManager.getUser(SessionService.getInstance().loggedInID).authenticate(oldPasswordField.getText())){
                errorLabel.setText(AppDefaults.ErrorMessages.INCORRECT_PASSWORD.get());
                return;
            }

            User thisUser = userManager.getUser(SessionService.getInstance().loggedInID);
            thisUser.setFirstName(firstNameField.getText());
            thisUser.setLastName(lastNameField.getText());
            userManager.update(thisUser);
            errorLabel.setText(AppDefaults.SuccessMessages.ACCOUNT_CHANGED.get());
        }
    }

    /**
     * Manipulates the UI - if the user does not wish to change their password, hides those fields.
     */
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

    /**
     * The user's prefColour is automatically updated.
     */
    public void handleChangeColour() {
        Color selectedColor = prefColourPicker.getValue();

        String hex = String.format("#%02X%02X%02X",
                (int) (selectedColor.getRed() * 255),
                (int) (selectedColor.getGreen() * 255),
                (int) (selectedColor.getBlue() * 255)
        );

        User thisUser = userManager.getUser(SessionService.getInstance().loggedInID);
        thisUser.setPrefColour(hex);
        userManager.update(thisUser);

        initialize();
    }

    /**
     * Admins access to generate fake results using the admin service
     */
    public void handleGenerateResults() {

        int requestedUserID = -1;
        int requestedWeeksAgo = 30;

       Optional<String> userresult = inputDialogue("User ID Required", "Input the requested User");
       Optional<String> weekresult = inputDialogue("Results Dates", "How many weeks ago to generate the results?");

        if (userresult.isPresent()) {
            try {
                requestedUserID = Integer.parseInt(userresult.get().trim());
            } catch (NumberFormatException e) {
                return;
            }
        }

        if (weekresult.isPresent()) {
            try {
                requestedWeeksAgo = Integer.parseInt(weekresult.get().trim());
            } catch (NumberFormatException e) {
                return;
            }
        }

        if (requestedUserID != -1 || (userManager.getUser(requestedUserID) != null)) {
            AdminService.getInstance().generateFakeResults(requestedUserID, requestedWeeksAgo);
        } else {
            errorDialogue("Incorrect User ID");
        }
    }

    /**
     * Administrator access to directly interact with the database
     */
    public void handleDatabaseConsole() {

        Optional<String> sqlInject =  inputDialogue("SQL Inject", "Insert your SQLite Prompt");

        if (sqlInject.isPresent()) {
            try {
                AdminService.getInstance().directDataBase(sqlInject.get());
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}