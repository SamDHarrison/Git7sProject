package git7s.flashcardai.controller;

import git7s.flashcardai.dao.CardDAO;
import git7s.flashcardai.model.Card;
import git7s.flashcardai.Main;
import git7s.flashcardai.model.CardManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;


import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Controller for the Update Flashcards screen.
 * Allows the user to view and update flashcards within a selected topic.
 */
public class UpdateFlashcardsController {

    @FXML public Button updateButton;
    @FXML public Button deleteButton;
    @FXML public Button createButton;
    @FXML public Label updateFlashCardsTitle;
    @FXML private ListView<String> flashcardListView;
    @FXML private TextField frontField;
    @FXML private TextField backField;

    private List<Card> flashcards;
    private ObservableList<String> flashcardNameList;
    private ObservableList<Map.Entry<String, Integer>> flashcardNameHash;
    private Card selectedCard;

    private CardManager cardManager;

    private enum UpdateFlashCardMessage {
        UPDATED("Your flashcard has been updated"),
        ERROR("There was an error, try again"),
        DELETED("Your flashcard was deleted");

        private String result;

        UpdateFlashCardMessage(String result){
            this.result = result;
        }

        public String getResult(){
            return result;
        }

    }

    /**
     * Initializes the controller.
     * Loads flashcards from the current topic set in Main.currentDeck and binds them to the ListView.
     * Also sets up a listener to populate the input fields when a card is selected.
     */
    @FXML
    public void initialize() {
        String currentTopic = Main.currentDeck;
        cardManager = new CardManager(new CardDAO());

        flashcards = cardManager.searchCardsByTopic(currentTopic);

        flashcardNameHash = FXCollections.observableArrayList();
        flashcardNameList = FXCollections.observableArrayList();

        for (Card f : flashcards) {
            String displayName = f.getFront();
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

            selectedCard = flashcards.stream().filter(card -> card.getCardID() == selectedCardID.get()).findFirst().get();
            frontField.setText(selectedCard.getFront());
            backField.setText(selectedCard.getBack());
            updateFlashCardsTitle.setText("Viewing Flashcards for: " + flashcards.getFirst().getSubject() + ", " + flashcards.getFirst().getTopic());
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
            cardManager.updateCard(selectedCard);
        }
        initialize();
    }


    /**
     * Delete a specific flashcard
     */
    public void handleDeleteFlashcard() {
        if (selectedCard != null) {
            cardManager.delete(selectedCard.getCardID());
        }
        flashcardListView.getSelectionModel().selectFirst();;
        initialize();

    }

    /**
     * Create a new flashcard
     */
    public void handleCreateFlashcard() {
        cardManager.addCard(new Card(Main.loggedInUserID, flashcards.getFirst().getSubject(), flashcards.getFirst().getTopic(), "ENTER NEW FRONT", "ENTER NEW BACK"));
        initialize();
        flashcardListView.getSelectionModel().select("ENTER NEW FRONT");

    }
}