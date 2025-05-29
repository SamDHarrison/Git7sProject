package git7s.flashcardai.controller;
import git7s.flashcardai.dao.CardDAO;
import git7s.flashcardai.dao.ResultDAO;
import git7s.flashcardai.dao.UserDAO;
import git7s.flashcardai.model.*;
import git7s.flashcardai.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import java.util.HashMap;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class controls the dashboard GUI
 */
public class DashboardController {
    /**
     * This label is the progress text on the progress bar
     */
    @FXML public Label progressTextLabel;
    /**
     * This button lets the user test all of their flashcards
     */
    @FXML public Button testAllButton;
    /**
     * Error Label for errors
     */
    @FXML public Label errorLabel;
    /**
     * Displays the name on entry
     */
    @FXML public Label welcomeLabel;
    /**
     * Lets the user choose their topic
     */
    @FXML public Button topicsButton;
    /**
     * Lets the user check their stats
     */
    @FXML public Button statsButton;
    /**
     * Lets the user test their strongest subject
     */
    @FXML public Button testStrongestSubject;
    /**
     * Lets the user test their weakest subject
     */
    @FXML public Button testWeakestSubject;
    /**
     * Lets the user change their accessibility settings
     */
    @FXML public Button accessButton;
    /**
     * Lets the user change their password
     */
    @FXML public Button editAccount;
    /**
     * Button to go to subjects GUI
     */
    @FXML private Button subjectsButton;
    /**
     * Button for logging out
     */
    @FXML private Button logOutButton;
    /**
     * Result Manager for accessing DB
     */
    private ResultManager resultManager;
    /**
     * Card Manager for accessing DB
     */
    private CardManager cardManager;
    /**
     * For accessing the user DB
     */
    private UserManager userManager;
    /**
     * Initialise is run when the GUI is opened.
     */
    @FXML
    public void initialize() {
        //DB
        resultManager = new ResultManager(new ResultDAO());
        cardManager = new CardManager(new CardDAO());
        userManager = new UserManager(new UserDAO());
        // Get Study Data
        String[] studyData = resultManager.getBasicStudyData();

        Main.prefCol = userManager.getUser(Main.loggedInUserID).getPrefColour();
        //Populate UI
        testStrongestSubject.setText(studyData[0]);
        testWeakestSubject.setText(studyData[1]);

        welcomeLabel.setText("Welcome, " + userManager.getUser(Main.loggedInUserID).getFullName());

        setPrefColours(Main.prefCol);
    }

    /**
     * Method to go to the Subjects display
     */
    @FXML
    private void handleMySubjects() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/git7s/flashcardai/my-subjects-view.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) subjectsButton.getScene().getWindow();
            stage.setScene(new Scene(root, Main.WIDTH, Main.HEIGHT));
            stage.setTitle("My Subjects");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to log out
     */
    @FXML
    private void handleLogout() {
        try {
            // Clear the logged-in user
            Main.loggedInUserID = -1;

            // Load login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/git7s/flashcardai/login-view.fxml"));
            Parent root = loader.load();
            // Switch scenes
            Stage stage = (Stage) logOutButton.getScene().getWindow();
            stage.setScene(new Scene(root, Main.WIDTH, Main.HEIGHT));
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * This method tests all the users flashcards
     */
    public void handleTestAll() {
        if (!cardManager.searchByUserID(Main.loggedInUserID).isEmpty()){
            try {
                Main.currentGameMode = 4;

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/git7s/flashcardai/card-deck-view.fxml"));
                Parent root = fxmlLoader.load();

                Stage stage = (Stage) testAllButton.getScene().getWindow();
                stage.setScene(new Scene(root, Main.WIDTH, Main.HEIGHT));
                stage.setTitle("Flashcard AI - Test");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            errorLabel.setText("You have no cards! Press My Subjects to create some!");
        }

    }

    public void handleTestStrongest() {
        if (resultManager.getBasicStudyData()[0].equals("Unknown")){
            errorLabel.setText("You need to play more before I can calculate your strongest subject");
        } else {
            try {
                Main.currentGameMode = 2;
                Main.currentDeck = testStrongestSubject.getText();
                if (cardManager.searchCardsBySubject(Main.currentDeck).isEmpty()) {
                    return;
                }
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/git7s/flashcardai/card-deck-view.fxml"));
                Parent root = fxmlLoader.load();

                Stage stage = (Stage) testAllButton.getScene().getWindow();
                stage.setScene(new Scene(root, Main.WIDTH, Main.HEIGHT));
                stage.setTitle("Flashcard AI - Test");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void handleTestWeakest() {
        if (resultManager.getBasicStudyData()[1].equals("Unknown")){
            errorLabel.setText("You need to play more before I can calculate your weakest subject");
        } else {
            try {
                Main.currentGameMode = 2;
                Main.currentDeck = testWeakestSubject.getText();
                if (cardManager.searchCardsBySubject(Main.currentDeck).isEmpty()) {
                    return;
                }
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/git7s/flashcardai/card-deck-view.fxml"));
                Parent root = fxmlLoader.load();

                Stage stage = (Stage) testAllButton.getScene().getWindow();
                stage.setScene(new Scene(root, Main.WIDTH, Main.HEIGHT));
                stage.setTitle("Flashcard AI - Test");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void handleStatistics() {
        if (!resultManager.getByUserID(Main.loggedInUserID).isEmpty()){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/git7s/flashcardai/statistics-view.fxml"));
                Parent root = fxmlLoader.load();

                Stage stage = (Stage) statsButton.getScene().getWindow();
                stage.setScene(new Scene(root, Main.WIDTH, Main.HEIGHT));
                stage.setTitle("Statistics");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void handleEditAccount() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/git7s/flashcardai/my-account-view.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) editAccount.getScene().getWindow();
            stage.setScene(new Scene(root, Main.WIDTH, Main.HEIGHT));
            stage.setTitle("My Account");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPrefColours(String colour){
        String s = "-fx-background-color: " + colour + "; -fx-text-fill: white; -fx-font-weight: bold;";

        testAllButton.setStyle(s);
        testStrongestSubject.setStyle(s);
        testWeakestSubject.setStyle(s);
        subjectsButton.setStyle(s);
        statsButton.setStyle(s);
        editAccount.setStyle(s);
        logOutButton.setStyle(s);
    }


}


