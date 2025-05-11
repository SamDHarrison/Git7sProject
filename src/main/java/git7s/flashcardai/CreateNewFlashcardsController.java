package git7s.flashcardai;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

/**
 * This is a class that handles the Create Flashcard GUI
 */
public class CreateNewFlashcardsController {
    /**
     * This textfield is the input for subject
     */
    @FXML
    public TextField subjectField;
    /**
     * This Button is the create button
     */
    @FXML
    public Button createFlashcardsButton;
    /**
     * This textfield is the input for topics
     */
    @FXML
    private TextField topicField;
    /**
     * This Textarea is the users input for the generation of flashcards
     */
    @FXML
    private TextArea contentArea;
    /**
     * This Spinner dictates how many flashcards will be created
     */
    @FXML
    private Spinner<Integer> flashcardCountSpinner;
    /**
     * This int handles the state of the async response - lets the user know when the API response has returned.
     */
    int manageAsyncResponse = 0;
    /**
     * This timeline helps to handle the Async Response
     */
    Timeline timeline = new Timeline();
    /**
     * This LLMGenerator class calls the generation of flashcard API prompt (REST)
     */
    LLMGenerator llm = new LLMGenerator();
    /**
     * This FlashCardDraft Object recieves the LLMGenerator output and converts it into the formatted data required.
     */
    FlashCardDraft newCards;

    /**
     * This initialise method is called when the program displays the GUI
     * 1. Set initial Spinner value
     */
    @FXML
    private void initialize() {
        // Set spinner values
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 30, 10); //Min required flashcards: 10, max: 30, default: 10
        flashcardCountSpinner.setValueFactory(valueFactory);
    }

    /**
     * This method checks if the inputs are correctly filled and generates the flashcards
     */
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

        handleGenerateFlashCards(count);
    }

    /**
     * A text alert to prompt the user
     * @param message The message for the user
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Handle the generation of flashcards using the LLM and FlashCardDraft objects
     */
    public void handleGenerateFlashCards(int quantity) {

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
                newCards.addFlashCards(subjectField.getText(), topicField.getText(), quantity);
                manageAsyncResponse = 2;
                break;
            case 2:
                /// Do nothing
            default:
                manageAsyncResponse = 0;
                break;
        }

    }

    /**
     * Manage the async response - returns a timeline that is referenced to confirm completion of the API response.
     * @return The Timeline to be later referenced.
     */
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