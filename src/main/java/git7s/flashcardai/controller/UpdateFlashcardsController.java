package git7s.flashcardai.controller;

import git7s.flashcardai.dao.CardDAO;
import git7s.flashcardai.model.Card;
import git7s.flashcardai.model.CardManager;
import git7s.flashcardai.service.GameService;
import git7s.flashcardai.service.SessionService;
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
public class UpdateFlashcardsController extends  AbstractController implements  IController {
    /**
     * Public default constructor.
     */
    public UpdateFlashcardsController() {
    }
    /**
     * Buttons
     */
    @FXML public Button updateButton, deleteButton, createButton;
    /**
     * Labels
     */
    @FXML public Label updateFlashCardsTitle;
    /**
     * Listview
     */
    @FXML private ListView<String> flashcardListView;
    /**
     * Text field
     */
    @FXML private TextField frontField, backField;
    /**
     * Flashcards currently displayed
     */
    private List<Card> flashcards;
    /**
     * Links the names to the actual card object
     */
    private ObservableList<Map.Entry<String, Integer>> flashcardNameHash;
    /**
     * Currently selected card
     */
    private Card selectedCard;
    /**
     * Card Manager for DAO access
     */
    private CardManager cardManager;

    /**
     * Initializes the controller.
     * Loads flashcards from the current topic set in Main.currentDeck and binds them to the ListView.
     * Also sets up a listener to populate the input fields when a card is selected.
     */
    @FXML
    public void initialize() {
        cardManager = new CardManager(new CardDAO());
        setupUIData();
    }

    /**
     * Sets up the UI with correct data
     */
    @Override
    public void setupUIData() {
        String currentTopic = GameService.getInstance().getCurrentDeck();

        if (GameService.getInstance().getCurrentGameMode() == 0) {
            flashcards = cardManager.searchCardsBySubject(currentTopic);
        } else {
            flashcards = cardManager.searchCardsByTopic(currentTopic);
        }
        flashcardNameHash = FXCollections.observableArrayList();
        ObservableList<String> flashcardNameList = FXCollections.observableArrayList();

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
            if (GameService.getInstance().getCurrentGameMode() == 0) {
                updateFlashCardsTitle.setText("Viewing Flashcards for: " + flashcards.getFirst().getSubject());
            } else {
                updateFlashCardsTitle.setText("Viewing Flashcards for: " + flashcards.getFirst().getSubject() + ", " + flashcards.getFirst().getTopic());
            }
        });
        setPrefColours(createButton, updateButton, deleteButton);
    }

    /**
     * Back button
     */
    @Override
    public void handleBackButton() {
        //Nil required, closed by window
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
        flashcardListView.getSelectionModel().selectFirst();
        initialize();

    }

    /**
     * Create a new flashcard
     */
    public void handleCreateFlashcard() {
        cardManager.addCard(new Card(SessionService.getInstance().loggedInID, flashcards.getFirst().getSubject(), flashcards.getFirst().getTopic(), "ENTER NEW FRONT", "ENTER NEW BACK"));
        initialize();
        flashcardListView.getSelectionModel().select("ENTER NEW FRONT");

    }

}