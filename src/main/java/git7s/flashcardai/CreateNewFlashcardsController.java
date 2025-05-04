package git7s.flashcardai;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CreateNewFlashcardsController {

    @FXML
    private TextField topicField;

    @FXML
    private TextArea contentArea;

    @FXML
    private Spinner<Integer> flashcardCountSpinner;

    @FXML
    private void initialize() {
        // Set spinner values
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 30, 10); //Min required flashcards: 10, max: 30, default: 10
        flashcardCountSpinner.setValueFactory(valueFactory);
    }

    @FXML
    private void handleCreateFlashcards() {
        String topic = topicField.getText();
        String content = contentArea.getText();
        int count = flashcardCountSpinner.getValue();

        if (topic.isEmpty() || content.isEmpty()) {
            showAlert("All fields must be filled.");
            return;
        }

        // To do: Add flashcard generation logic here

        showAlert("Flashcards created for topic: " + topic + " (" + count + " cards)");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}