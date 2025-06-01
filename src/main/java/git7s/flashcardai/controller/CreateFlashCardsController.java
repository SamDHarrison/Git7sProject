package git7s.flashcardai.controller;

import git7s.flashcardai.AppDefaults;
import git7s.flashcardai.llm.FlashCardGenerateManager;
import git7s.flashcardai.llm.LLMGenerator;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

/**
 * This is a class that handles the Create Flashcard GUI
 */
public class CreateFlashCardsController extends AbstractController implements IController{
    /**
     * Public default constructor.
     */
    public CreateFlashCardsController() {
    }
    /**
     * Textfields
     */
    @FXML
    public TextField subjectField, topicField;
    /**
     * Buttons
     */
    @FXML
    public Button createFlashcardsButton;
    /**
     * Labels
     */
    public Label errorLabel;
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
    private final LLMGenerator llm = new LLMGenerator();
    /**
     * This FlashCardDraft Object recieves the LLMGenerator output and converts it into the formatted data required.
     */
    private FlashCardGenerateManager newCards;
    /**
     * This initialise method is called when the program displays the GUI
     * 1. Set initial Spinner value
     */
    @FXML
    @Override
    public void initialize() {
        setupUIData();
    }

    /**
     * Setup the UI elements
     */
    @Override
    public void setupUIData() {
        // Set spinner values
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 5); //Min, Max, Default
        flashcardCountSpinner.setValueFactory(valueFactory);
        setPrefColours(createFlashcardsButton);
    }

    /**
     * Placeholder for future design implementation
     */
    @Override
    public void handleBackButton() {
        //Not required - only closed via window as popup
    }
    /**
     * This method checks if the inputs are correctly filled and generates the flashcards
     */
    @FXML
    private void handleCreateFlashcards() {
        String content = contentArea.getText();
        int count = flashcardCountSpinner.getValue();

        if (checkFieldsEmpty(topicField, subjectField) || contentArea.getText().isEmpty()) {
            errorLabel.setText(AppDefaults.ErrorMessages.NOT_FILLED_FIELDS.get());
            return;
        }
        createFlashcardsButton.setVisible(false);
        flashcardCountSpinner.setVisible(false);
        topicField.setEditable(false);
        subjectField.setEditable(false);
        contentArea.setEditable(false);
        errorLabel.setText(AppDefaults.SuccessMessages.WAITING_FOR_AI.get());
        llm.sendQuery(LLMGenerator.QueryType.GENERATE_QUERY, content, count);
        asyncWait();
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
                        errorLabel.setText(AppDefaults.SuccessMessages.SUCCESSFUL_GENERATION.get());
                    }
                })
        );
        asyncTracker.setCycleCount(Animation.INDEFINITE);
        asyncTracker.play();
    }
}