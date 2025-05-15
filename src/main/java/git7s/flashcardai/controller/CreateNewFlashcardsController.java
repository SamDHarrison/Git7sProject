package git7s.flashcardai.controller;

import git7s.flashcardai.dao.CardDAO;
import git7s.flashcardai.llm.FlashCardGenerateManager;
import git7s.flashcardai.llm.LLMGenerator;
import git7s.flashcardai.model.CardManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
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
     * Error label for bad inputs
     */
    public Label errorLabel;
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
     * This timeline helps to handle the Async Response
     */
    private Timeline asyncTracker = new Timeline();
    /**
     * This LLMGenerator class calls the generation of flashcard API prompt (REST)
     */
    private LLMGenerator llm = new LLMGenerator();
    /**
     * This FlashCardDraft Object recieves the LLMGenerator output and converts it into the formatted data required.
     */
    private FlashCardGenerateManager newCards;
    /**
     * Card Manager for db interaction
     */
    private CardManager cardManager;
    /**
     * Parent controller
     */
    private MySubjectsController parent;
    /**
     * For testing
     * @param keyEvent The type of key pressed
     */
    public void handleEnterKey(KeyEvent keyEvent) {
        System.out.println(keyEvent.getText());
    }
    /**
     * Enum to provide feedback to user on errors
     */
    private enum CreateFlashCardsError {
        notFilledFields("Please fill in all fields"),
        waitingForAI("Waiting for AI to generate"),
        successfullGeneration("You may exit this window!");
        final String description;

        CreateFlashCardsError(String description) {
            this.description = description;
        }
        public String getDescription(){
            return description;
        }
    }
    /**
     * This initialise method is called when the program displays the GUI
     * 1. Set initial Spinner value
     */
    @FXML
    private void initialize() {
        // Set spinner values
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 5); //Min, Max, Default
        flashcardCountSpinner.setValueFactory(valueFactory);
        cardManager = new CardManager(new CardDAO());
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
            errorLabel.setText(CreateFlashCardsError.notFilledFields.getDescription());
            return;
        }
        createFlashcardsButton.setVisible(false);
        flashcardCountSpinner.setVisible(false);
        topicField.setEditable(false);
        subjectField.setEditable(false);
        contentArea.setEditable(false);
        errorLabel.setText(CreateFlashCardsError.waitingForAI.getDescription());
        llm.sendQuery(LLMGenerator.QueryType.GENERATE_QUERY, content, count);
        asyncWait();
        System.out.println("Sent to LLM");
    }
    /**
     * Manage the async response - starts a timeline that is referenced to confirm completion of the API response.
     */
    private void asyncWait() {
        asyncTracker = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    if (llm.getResponse() != null) {
                        asyncTracker.stop(); // Stop the timeline once done
                        newCards = new FlashCardGenerateManager(llm.getResponse());
                        newCards.addFlashCards(subjectField.getText(), topicField.getText(), flashcardCountSpinner.getValue());
                        errorLabel.setText(CreateFlashCardsError.successfullGeneration.getDescription());
                        parent.setSelected(subjectField.getText(), topicField.getText());
                    }
                })
        );
        asyncTracker.setCycleCount(Animation.INDEFINITE);
        asyncTracker.play();
    }

    public void setParent(MySubjectsController parent){
        this.parent = parent;
    }


}