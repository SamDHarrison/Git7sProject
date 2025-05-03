package git7s.flashcardai;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class DashboardController {
    public Label progressTextLabel;
    @FXML private ProgressBar studyProgressBar;
    @FXML private Label strongestTopicLabel;
    @FXML private Label weakestTopicLabel;

    @FXML private Button subjectsButton;
    @FXML private Button settingsButton;
    @FXML private Button logOutButton;

    @FXML
    public void initialize() {
        // Set placeholder data
        studyProgressBar.setProgress(0.75); // 75% completion
        strongestTopicLabel.setText("Programming");
        weakestTopicLabel.setText("Science");
    }
}
