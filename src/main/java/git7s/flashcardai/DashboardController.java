package git7s.flashcardai;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {
    public Label progressTextLabel;
    @FXML private ProgressBar studyProgressBar;
    @FXML private Label strongestTopicLabel;
    @FXML private Label weakestTopicLabel;

    @FXML private Button createDeckButton;
    @FXML private Button viewFlashcardsButton;
    @FXML private Button testAllFlashcardsButton;
    @FXML private Button settingsButton;

    @FXML
    public void initialize() {
        // Get Study Data
        List<Result> studyData = Main.resultDAO.getByUserID(Main.loggedInUser.getId());
        List<String> correctResults = new ArrayList<String>();
        List<String> incorrectResults = new ArrayList<String>();
        Timestamp latest = new Timestamp(0);
        LocalDate today = LocalDate.now();
        String latestDisplay = "No recent history.";
        int latestCardID = -1;
        String flashCardsToday = "0";
        String strongestTopic = "";
        String weakestTopic = "";

        for (Result result : studyData){
            //Times
            if (result.getAt().after(latest)){
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
                latest = result.getAt();
                latestDisplay = dateFormat.format(latest);
                latestCardID = result.getCardID();
            }
            //Flashcards done today
            if (result.getAt().toLocalDateTime().toLocalDate().equals(today)) {
                flashCardsToday = String.valueOf((Integer.parseInt(flashCardsToday)+1));
            }

        }

        //Check Data
        if(flashCardsToday.equals("0")){
            flashCardsToday = "No flash cards completed today!";
        }



        // Setting data
        usernameLabel.setText(Main.loggedInUser.getFullName());
        leagueLabel.setText("Gold League");
        studyProgressBar.setProgress(0.75); // 75% complete
        lastStudiedLabel.setText("You last studied: " + latestDisplay);
        flashcardsTodayLabel.setText("Flash cards completed today: " + flashCardsToday);
        strongestTopicLabel.setText("Mathematics");
        weakestTopicLabel.setText("History");
        // Set placeholder data
        studyProgressBar.setProgress(0.75); // 75% completion
        strongestTopicLabel.setText("Programming");
        weakestTopicLabel.setText("Science");
    }
}
