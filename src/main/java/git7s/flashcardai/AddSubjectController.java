package git7s.flashcardai;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

public class AddSubjectController {
    public Label progressTextLabel;
    @FXML private ProgressBar studyProgressBar;
    @FXML private Label strongestTopicLabel;
    @FXML private Label weakestTopicLabel;

    @FXML private Button createDeckButton;
    @FXML private Button viewFlashcardsButton;
    @FXML private Button testAllFlashcardsButton;
    @FXML private Button settingsButton;
    int manageAsyncResponse = 0;
    Timeline timeline = new Timeline();
    LLMGenerator llm = new LLMGenerator();
    FlashCardDraft newCards;

    @FXML
    public void initialize() {
        // Set placeholder data
        studyProgressBar.setProgress(0.75); // 75% completion
        strongestTopicLabel.setText("Programming");
        weakestTopicLabel.setText("Science");
    }


}
