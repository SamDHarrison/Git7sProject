package git7s.flashcardai;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

public class CreateNewFlashcardsController {

    @FXML
    public TextField subjectField;
    public Button createFlashcardsButton;
    @FXML
    private TextField topicField;
    @FXML
    private TextArea contentArea;
    @FXML
    private Spinner<Integer> flashcardCountSpinner;

    int manageAsyncResponse = 0;
    Timeline timeline = new Timeline();
    LLMGenerator llm = new LLMGenerator();
    FlashCardDraft newCards;

    @FXML
    private void initialize() {
        // Set spinner values
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 30, 10); //Min required flashcards: 10, max: 30, default: 10
        flashcardCountSpinner.setValueFactory(valueFactory);
    }

    @FXML
    private void handleCreateFlashcards() {
        String subject = subjectField.getText();
        String topic = topicField.getText();
        String content = contentArea.getText();
        int count = flashcardCountSpinner.getValue();

        if (topic.isEmpty() || content.isEmpty() || subject.isEmpty()) {
            showAlert("All fields must be filled.");
            return;
        }

        handleGenerateFlashCards();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /// will go into new gui when ready
    public void handleGenerateFlashCards() {

        switch (manageAsyncResponse) {
            case 0:
                llm.fetchFlashCards(contentArea.getText(), flashcardCountSpinner.getValue());
                createFlashcardsButton.setStyle("-fx-background-color: #f9f9f9; -fx-text-fill: black;");
                createFlashcardsButton.setText("Loading...");
                timeline = getTimeline();
                timeline.play();
                showAlert("Generation takes 10-15 seconds, please be patient!");
                break;
            case 1:
                newCards = new FlashCardDraft(llm.getResponse());
                newCards.addFlashCards(subjectField.getText(), topicField.getText());
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
                        createFlashcardsButton.setText("Click to return!");
                        createFlashcardsButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                        manageAsyncResponse = 1;
                        timeline.stop(); // Stop the timeline once done
                    }
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        return timeline;
    }


}