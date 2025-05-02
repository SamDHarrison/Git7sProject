package git7s.flashcardai;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class DashboardController {
    @FXML private ProgressBar studyProgressBar;
    @FXML private Label strongestTopicLabel;
    @FXML private Label weakestTopicLabel;

    @FXML private Button createDeckButton;
    @FXML private Button viewFlashcardsButton;
    @FXML private Button testAllFlashcardsButton;
    @FXML private Button settingsButton;

    @FXML
    public void initialize() {
        // Set placeholder data
        studyProgressBar.setProgress(0.75); // 75% completion
        strongestTopicLabel.setText("Programming");
        weakestTopicLabel.setText("Science");
    }
}
