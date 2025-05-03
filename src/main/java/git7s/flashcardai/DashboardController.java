package git7s.flashcardai;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashboardController {
    public Label progressTextLabel;
    @FXML private ProgressBar studyProgressBar;
    @FXML private Label strongestTopicLabel;
    @FXML private Label weakestTopicLabel;

    @FXML private Button subjectsButton;
    @FXML private Button settingsButton;
    @FXML private Button logOutButton;

    /// Will go into new GUI
    int manageAsyncResponse = 0;
    Timeline timeline = new Timeline();
    LLMGenerator llm = new LLMGenerator();
    FlashCardDraft newCards;


    @FXML
    public void initialize() {
        // Get Study Data
        String[] studyData = getStudyData();
        // Setting data
        studyProgressBar.setProgress(Double.parseDouble(studyData[2])); // 75% complete
        strongestTopicLabel.setText(studyData[0]);
        weakestTopicLabel.setText(studyData[1]);

    }

    private String[] getStudyData(){
        String[] studyData = new String[3];

        HashMap<String, Integer> correctCounter = new HashMap<String, Integer>();
        HashMap<String, Integer> incorrectCounter = new HashMap<String, Integer>();
        HashMap<Integer, String> subjects = new HashMap<Integer, String>();

        double correctPercent = 0;
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
            correctPercent = (totalCorrect/(totalCorrect+totalIncorrect));
        } catch (ArithmeticException e){
            correctPercent = 0;
        }

        studyData[0] = strongestSubject;
        studyData[1] = weakestSubject;
        studyData[2] = String.valueOf(correctPercent);

        return studyData;
    }

    /// will go into new gui when ready
    public void handleGenerateFlashCards() {

        switch (manageAsyncResponse) {
            case 0:
                llm.fetchFlashCards("Programmer defined aggregate type\n" +
                        "Typically declared with (1)\n" +
                        "Struct declaration and access (2)\n" +
                        "\n" +
                        "Structs can be passed between functions and copied normally\n" +
                        "Structs can contain arrays, which means arrays can be copied by passing through structs\n");
                createDeckButton.setStyle("-fx-background-color: #f9f9f9; -fx-text-fill: black;");
                createDeckButton.setText("Loading...");
                timeline = getTimeline();
                timeline.play();
                break;
            case 1:
                newCards = new FlashCardDraft(llm.getResponse());
                newCards.showFlashCards();
                manageAsyncResponse = 2;
                break;
            case 2:
                /// Do nothing
            default:
                manageAsyncResponse = 0;
                break;
        }
    }
    ///FOR NEW GUI TO HANDLE THE FLASHCARD GENERATION
    private Timeline getTimeline() {
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    if (llm.checkResponse()) {
                        createDeckButton.setText("Click to continue!");
                        createDeckButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                        manageAsyncResponse = 1;
                        timeline.stop(); // Stop the timeline once done
                    }
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        return timeline;
    }
}
