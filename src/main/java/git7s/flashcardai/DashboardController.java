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


}
