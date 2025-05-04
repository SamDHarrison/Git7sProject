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
