import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;


public class DashboardController {
    @FXML
    private Label usernameLabel;
    @FXML
    private Label leagueLabel;
    @FXML
    private ProgressBar studyProgressBar;
    @FXML
    private Label lastStudiedLabel;
    @FXML
    private Label flashcardsTodayLabel;
    @FXML
    private Label strongestTopicLabel;
    @FXML
    private Label weakestTopicLabel;
    @FXML
    private Button shareButton;

    private int manageAsyncResponse = 0;
    private LLMGenerator llm = new LLMGenerator();
    private FlashCardDraft newCards;
    Timeline timeline;

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
    }
     
    public void handleGenerateFlashCards() {
        switch (manageAsyncResponse) {
            case 0:
                llm.fetchFlashCards("Programmer defined aggregate type\n" +
                        "Typically declared with (1)\n" +
                        "Struct declaration and access (2)\n" +
                        "\n" +
                        "Structs can be passed between functions and copied normally\n" +
                        "Structs can contain arrays, which means arrays can be copied by passing through structs\n");
                shareButton.setStyle("-fx-background-color: #f9f9f9; -fx-text-fill: black;");
                shareButton.setText("Loading...");
                timeline = getTimeline();
                timeline.play();
                break;
            case 1:
                newCards = new FlashCardDraft(llm.getResponse());
                manageAsyncResponse = 2;
                break;
            case 2:
                /// Do nothing
            default:
                manageAsyncResponse = 0;
                break;
        }
    }

    private Timeline getTimeline() {
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    if (llm.checkResponse()) {
                        shareButton.setText("Click to continue!");
                        shareButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                        manageAsyncResponse = 1;
                        timeline.stop(); // Stop the timeline once done
                    }
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        return timeline;
    }


}

