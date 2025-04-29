package git7s.flashcardai;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

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
    public void initialize() {
        // Setting placeholder data
        usernameLabel.setText("@student123");
        leagueLabel.setText("Gold League");
        studyProgressBar.setProgress(0.75); // 75% complete
        lastStudiedLabel.setText("April 29, 2025 08:30 PM");
        flashcardsTodayLabel.setText("22");
        strongestTopicLabel.setText("Mathematics");
        weakestTopicLabel.setText("History");
    }
}

