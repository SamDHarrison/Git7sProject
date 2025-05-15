package git7s.flashcardai.controller;
import git7s.flashcardai.dao.CardDAO;
import git7s.flashcardai.dao.ResultDAO;
import git7s.flashcardai.dao.UserDAO;
import git7s.flashcardai.model.*;
import git7s.flashcardai.Main;
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
    public Button testAllButton;
    /**
     * Error Label for errors
     */
    public Label errorLabel;
    /**
     * Displays the name on entry
     */
    public Label welcomeLabel;
    /**
     * The progress bar to track correct and incorrect
     */
    @FXML private ProgressBar studyProgressBar;
    /**
     * Strongest topic displayed
     */
    @FXML private Label strongestTopicLabel;
    /**
     * Weakest topic displayed
     */
    @FXML private Label weakestTopicLabel;
    /**
     * Button to go to subjects GUI
     */
    @FXML private Button subjectsButton;
    /**
     * Button for settings GUI
     */
    @FXML private Button settingsButton;
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
        String[] studyData = getStudyData();
        // Setting data
        studyProgressBar.setProgress(Double.parseDouble(studyData[2])/100);
        progressTextLabel.setText("Your lifetime score is " + studyData[2] + "% correct!");
        strongestTopicLabel.setText(studyData[0]);
        weakestTopicLabel.setText(studyData[1]);
        welcomeLabel.setText("Welcome, " + userManager.getUser(Main.loggedInUserID).getFullName());
    }

    /**
     * Generates a String[] that contains pertinent data from the Results table.
     * @return String[] of study data. 1: Strongest Topic, 2: Weakest Topic, 3: Correct/Incorrect Ratio
     */
    private String[] getStudyData(){ /// Really inefficient, recommend studydatamanger class
        String[] studyData = new String[3];

        HashMap<String, Integer> correctCounter = new HashMap<String, Integer>();
        HashMap<String, Integer> incorrectCounter = new HashMap<String, Integer>();
        HashMap<Integer, String> subjects = new HashMap<Integer, String>();

        int correctPercent = 0;
        String strongestSubject = "Play more to find out!";
        String weakestSubject = "Play more to find out!";
        int strongestSubjectCount = 0;
        int weakestSubjectCount = 0;
        double totalCorrect = 0;
        double totalIncorrect = 0;

        //Get Results
        for (Result result : resultManager.getByUserID(Main.loggedInUserID)) {
            /// Correct
            if (result.isCorrect()){
                totalCorrect++;
                if (correctCounter.containsKey(result.getSubject())){
                    Integer count = correctCounter.get(result.getSubject());
                    correctCounter.replace(result.getSubject(), count + 1);
                    if (count > strongestSubjectCount) {
                        strongestSubject = result.getSubject();
                        strongestSubjectCount = count;
                    }
                }
                else {
                    correctCounter.put(result.getSubject(), 1);
                    if (strongestSubjectCount < 1) {
                        strongestSubject = result.getSubject();
                        strongestSubjectCount = 1;
                    }
                }
            }
            /// Incorrect
            else {
                totalIncorrect++;
                if (incorrectCounter.containsKey(result.getSubject())){
                    Integer count = incorrectCounter.get(result.getSubject());
                    incorrectCounter.replace(result.getSubject(), count + 1);
                    if (count > weakestSubjectCount) {
                        weakestSubject = result.getSubject();
                        weakestSubjectCount = count;
                    }

                } else {
                    incorrectCounter.put(result.getSubject(), 1);
                    if (weakestSubjectCount < 1) {
                        weakestSubject = result.getSubject();
                        weakestSubjectCount = 1;
                    }
                }
            }
        }

        try {
            correctPercent = (int) (totalCorrect/(totalCorrect+totalIncorrect)*100);

        } catch (ArithmeticException e){
            correctPercent = 0;
        }
        studyData[0] = strongestSubject;
        studyData[1] = weakestSubject;
        studyData[2] = String.valueOf(correctPercent);
        return studyData;
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
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
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
            stage.setScene(new Scene(root));
            stage.sizeToScene();
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
                Main.currentDeck = "ALL";

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/git7s/flashcardai/card-deck-view.fxml"));
                Parent root = fxmlLoader.load();

                Stage stage = (Stage) testAllButton.getScene().getWindow();
                stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
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

}


