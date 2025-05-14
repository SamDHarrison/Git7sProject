package git7s.flashcardai;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;


import java.util.List;

public class UpdateFlashcardsController {

    @FXML private ListView<Card> flashcardListView;
    @FXML private TextField frontField;
    @FXML private TextField backField;

    private List<Card> flashcards;
    private Card selectedCard;

    @FXML
    public void initialize() {
        // Pull current topic from Main
        String currentTopic = Main.currentDeck;
        int userId = Main.loggedInUser.getId();

        flashcards = Main.cardDAO.getByTopicAndUser(currentTopic, userId);

        flashcardListView.setItems(FXCollections.observableArrayList(flashcards));
        flashcardListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedCard = newVal;
                frontField.setText(selectedCard.getFront());
                backField.setText(selectedCard.getBack());
            }
        });
    }

    @FXML
    private void handleUpdateFlashcard() {
        if (selectedCard != null) {
            selectedCard.setFront(frontField.getText());
            selectedCard.setBack(backField.getText());
            Main.cardDAO.updateCard(selectedCard);
            showAlert("Flashcard updated successfully!");
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}