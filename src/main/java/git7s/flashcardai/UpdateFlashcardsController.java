package git7s.flashcardai;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;


import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class UpdateFlashcardsController {

    @FXML public Button updateButton;
    @FXML public Button deleteButton;
    @FXML public Button createButton;
    @FXML private ListView<String> flashcardListView;
    @FXML private TextField frontField;
    @FXML private TextField backField;

    private List<Card> flashcards;
    private ObservableList<String> flashcardNameList;
    private ObservableList<Map.Entry<String, Integer>> flashcardNameHash;
    private Card selectedCard;

    @FXML
    public void initialize() {
        // Pull current topic from Main
        String currentTopic = Main.currentDeck;
        int userId = Main.loggedInUser.getId();

        flashcards = Main.cardDAO.getByTopicAndUser(currentTopic, userId);
        flashcardNameHash = FXCollections.observableArrayList();
        flashcardNameList = FXCollections.observableArrayList();
        for (Card f : flashcards) {
            String displayName = f.getFront() + " (" + f.getSubject() + ", " + f.getTopic() + ")";
            flashcardNameHash.add(new AbstractMap.SimpleEntry<>(displayName, f.getCardID()));
            flashcardNameList.add(displayName);
        }
        flashcardListView.setItems(flashcardNameList);
        AtomicInteger selectedCardID = new AtomicInteger();
        flashcardListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                for (Map.Entry<String, Integer> m : flashcardNameHash){
                    if (m.getKey().equals(newVal)){
                        selectedCardID.set(m.getValue());
                    }
                }
            }
            selectedCard = Main.cardDAO.getById(selectedCardID.get());
            frontField.setText(selectedCard.getFront());
            backField.setText(selectedCard.getBack());
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

    public void handleDeleteFlashcard() {
        if (selectedCard != null) {
            selectedCard.setFront(frontField.getText());
            selectedCard.setBack(backField.getText());
            Main.cardDAO.delete(selectedCard.getCardID());
            showAlert("Flashcard deleted successfully!");
        }
    }

    public void handleCreateFlashcard() {

    }
}