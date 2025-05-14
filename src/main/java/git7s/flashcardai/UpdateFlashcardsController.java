package git7s.flashcardai;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;


import java.util.List;

/**
 * Controller for the Update Flashcards screen.
 * Allows the user to view and update flashcards within a selected topic.
 */
public class UpdateFlashcardsController {

    @FXML private ListView<Card> flashcardListView; // Displays the list of flashcards for the current topic
    @FXML private TextField frontField; // Input for editing the flashcard question (front)
    @FXML private TextField backField;  // Input for editing the flashcard answer (back)

    private List<Card> flashcards;     // Holds the flashcards retrieved from the database
    private Card selectedCard;         // The currently selected flashcard being edited

    /**
     * Initializes the controller.
     * Loads flashcards from the current topic set in Main.currentDeck and binds them to the ListView.
     * Also sets up a listener to populate the input fields when a card is selected.
     */
    @FXML
    public void initialize() {
        String currentTopic = Main.currentDeck;
        int userId = Main.loggedInUser.getId();

        flashcards = Main.cardDAO.getByTopicAndUser(currentTopic, userId);
        flashcardListView.setItems(FXCollections.observableArrayList(flashcards));

        // Listen for selection and update the text fields
        flashcardListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedCard = newVal;
                frontField.setText(selectedCard.getFront());
                backField.setText(selectedCard.getBack());
            }
        });
    }

    /**
     * Called when the user clicks the "Save Changes" button.
     * Updates the selected flashcard with new front and back values in the database.
     */
    @FXML
    private void handleUpdateFlashcard() {
        if (selectedCard != null) {
            selectedCard.setFront(frontField.getText());
            selectedCard.setBack(backField.getText());
            Main.cardDAO.updateCard(selectedCard);
            showAlert("Flashcard updated successfully!");
        }
    }

    /**
     * Displays an alert message to the user.
     * @param msg The message to display in the alert box.
     */
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}