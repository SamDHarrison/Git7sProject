package git7s.flashcardai;
import javafx.animation.Timeline;
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
    public Button testAllButton;
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
     * Initialise is run when the GUI is opened.
     */
    @FXML
    public void initialize() {
        // Get Study Data
        String[] studyData = getStudyData();
        // Setting data
        studyProgressBar.setProgress(Double.parseDouble(studyData[2])/100);
        progressTextLabel.setText("Your lifetime score is " + studyData[2] + "% correct!");
        strongestTopicLabel.setText(studyData[0]);
        weakestTopicLabel.setText(studyData[1]);

    }

    /**
     * Generates a String[] that contains pertinent data from the Results table.
     * @return String[] of study data. 1: Strongest Topic, 2: Weakest Topic, 3: Correct/Incorrect Ratio
     */
    private String[] getStudyData(){
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

        //Get Subjects
        for (Card card : Main.cardDAO.getByUserID(Main.loggedInUser.getId())){
            subjects.put(card.getCardID(), card.getSubject());
        }
        //Get Results
        for (Result result : Main.resultDAO.getByUserID(Main.loggedInUser.getId())) {
            Integer cardID = result.getCardID();

            /// Correct
            if (result.isCorrect()){
                if (correctCounter.containsKey(subjects.get(cardID))){
                    Integer count = correctCounter.get(subjects.get(cardID));
                    correctCounter.replace(subjects.get(cardID), count + 1);
                    if (count > strongestSubjectCount) {
                        strongestSubject = subjects.get(cardID);
                        strongestSubjectCount = count;
                        totalCorrect++;
                    }
                }
                else {
                    correctCounter.put(subjects.get(cardID), 1);
                    if (strongestSubjectCount < 1) {
                        strongestSubject = subjects.get(cardID);
                        strongestSubjectCount = 1;
                        totalCorrect++;
                    }
                }
            }
            /// Incorrect
            else {
                if (incorrectCounter.containsKey(subjects.get(cardID))){
                    Integer count = incorrectCounter.get(subjects.get(cardID));
                    incorrectCounter.replace(subjects.get(cardID), count + 1);
                    if (count > weakestSubjectCount) {
                        weakestSubject = subjects.get(cardID);
                        weakestSubjectCount = count;
                        totalIncorrect++;
                    }
                }
                else {
                    incorrectCounter.put(subjects.get(cardID), 1);
                    if (weakestSubjectCount < 1) {
                        weakestSubject = subjects.get(cardID);
                        weakestSubjectCount = 1;
                        totalIncorrect++;
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
            Main.loggedInUser = null;

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
        if (Main.cardDAO.getByUserID(Main.loggedInUser.getId()) != null){
            try {
                CardDeckController.currentDeck = "ALL";

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
            showAlert(Alert.AlertType.ERROR, "You have no cards", "Sorry, you do not have any cards. Press My Subjects to create some");
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


