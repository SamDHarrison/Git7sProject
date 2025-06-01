package git7s.flashcardai.controller;


import git7s.flashcardai.AppDefaults;
import git7s.flashcardai.service.GameService;
import git7s.flashcardai.llm.LLMGenerator;
import git7s.flashcardai.service.SessionService;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * A class to control the GUI for test screen
 */
public class TestController extends AbstractController implements IController{
    /**
     * Protected default constructor.
     * <p>
     * This constructor is intentionally protected because this class is abstract
     * and should only be subclassed.
     * </p>
     */
    protected TestController() {
    }
    /**
     * Buttons
     */
    @FXML
    public Button correctAnswerButton, incorrectAnswerButton, askAI, flipCardButton;
    /**
     * This label controls the title text
     */
    @FXML
    public Label titleLabel, flashcardLabel, flashcardCounterLabel;
    /**
     * This button takes the user back to the main screen
     */
    @FXML
    private Hyperlink backToTopicsLink;
    /**
     * Private AIManager to handle query generation
     */
    private LLMGenerator llm;
    /**
     * This timeline handles the Async Response
     */
    Timeline asyncTracker;
    /**
     * GameManager will keep track of all the states of gameplay
     */
    private GameService gameManager;
    /**
     * A method that runs when the FXML GUI is first loaded:
     * 1. Pull the DB for the Card Deck
     * 2. Setup of labels and text items
     * 3. Game initialise
     */
    @FXML
    public void initialize() {
        //Init Managers
        llm = new LLMGenerator();
        gameManager = GameService.getInstance();
        gameManager.newGame();
        setupUIData();
    }

    @Override
    public void setupUIData() {
        titleLabel.setText(gameManager.generateTitle());
        backToTopicsLink.setOnAction(event -> {
            handleBack();});
        flipCardButton.setOnAction(event -> flipCard());
        correctAnswerButton.setOnAction(event -> writeResult(true));
        incorrectAnswerButton.setOnAction(event -> writeResult(false));
        askAI.setOnAction(event -> handleAIExplanation());
        //Final Setup to Begin
        updateFXMLElements();
        flashcardLabel.setText(gameManager.getFlashCardDisplay());
        setPrefColours(correctAnswerButton, incorrectAnswerButton, flipCardButton, askAI);
        backToTopicsLink.setStyle("-fx-background-color: " + SessionService.getInstance().uiColour + "; -fx-text-fill: white; -fx-font-weight: bold;");
    }
    @Override
    public void handleBackButton() {
        handleBack();
    }
    /**
     * Change the card from front to back
     */
    public void flipCard(){
        flipRotation();
        updateFXMLElements();

    }
    /**
     * Update the current flashcard, show the correct text. If showing front, show the correct buttons, vice versa
     */
    private void updateFXMLElements() {
        switch (gameManager.getGamestate()) {
            case GAME_PLAY -> {
                if (gameManager.isShowingFront()) {
                    flashcardCounterLabel.setText(gameManager.generateTrackerText());
                    incorrectAnswerButton.setVisible(false);
                    correctAnswerButton.setVisible(false);
                    flipCardButton.setVisible(true);
                    askAI.setVisible(false);
                } else {
                    askAI.setVisible(true);
                    incorrectAnswerButton.setVisible(true);
                    correctAnswerButton.setVisible(true);
                    flipCardButton.setVisible(false);
                }
            }
            case GAME_AI -> {
                incorrectAnswerButton.setVisible(false);
                correctAnswerButton.setVisible(false);
                flipCardButton.setVisible(false);
                askAI.setVisible(false);
                flashcardCounterLabel.setText("AI is thinking...");
            }
            case GAME_FINISH -> {
                askAI.setVisible(false);
                incorrectAnswerButton.setVisible(false);
                correctAnswerButton.setVisible(false);
                flipCardButton.setVisible(false);
                flashcardLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: normal; -fx-text-alignment: left;");
            }
        }
    }
    /**
     * Write the results to the boolean[] to be displayed in the nextCard() function
     * @param answer The result of the test
     */
    private void writeResult(boolean answer) {
        if (gameManager.getGamestate().equals(GameService.GAMESTATE.GAME_AI)) {
            gameManager.setGamestate(GameService.GAMESTATE.GAME_PLAY);
            updateFXMLElements();
        }
        gameManager.setResult(answer);
        gameManager.iterateFlashCardTracker();
        flipRotation();
        updateFXMLElements();

    }
    /**
     * Manages the GUI's response when AI button pressed
     */
    public void handleAIExplanation(){
        gameManager.setGamestate(GameService.GAMESTATE.GAME_AI);
        updateFXMLElements();
        llm.sendQuery(LLMGenerator.QueryType.EXPLAIN_QUERY, gameManager.getTextForQuery(),1);
        asyncWait();
    }

    /**
     * Flip transition aids to smooth text transition
     */
    private void flipRotation(){
        gameManager.setShowingFront(!gameManager.isShowingFront());
        RotateTransition flip = new RotateTransition(Duration.seconds(0.5));
        flip.setNode(flashcardLabel);
        flip.setFromAngle(0);
        flip.setToAngle(360);
        flip.setAxis(Rotate.Y_AXIS);
        flip.setCycleCount(1);
        flip.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (newTime.toMillis() > 125) {
                flashcardLabel.setText("");
            }
            if (newTime.toMillis() > 375) {
                if(!gameManager.getGamestate().equals(GameService.GAMESTATE.GAME_FINISH)) {
                    flashcardLabel.setText(gameManager.getFlashCardDisplay());
                }
                else {
                    showFeedbackPopup(gameManager.generateResultText());
                }
            }
        });

        flip.play();
    }
    /**
     * Manage the async response - starts a timeline that is referenced to confirm completion of the API response.
     */
    private void asyncWait() {
        asyncTracker = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    if (llm.getResponse() != null) {
                        asyncTracker.stop(); // Stop the timeline once done
                        correctAnswerButton.setVisible(true);
                        flashcardLabel.setText(llm.getResponse());
                        flashcardCounterLabel.setText("AI Response");
                        showFeedbackPopup(llm.getResponse());
                    }
                })
        );
        asyncTracker.setCycleCount(Animation.INDEFINITE);
        asyncTracker.play();
    }
    /**
     * Loads and displays the "My Subjects" view, replacing the current scene.
     */
    private void handleBack(){
        changeView(AppDefaults.ViewTitles.SUBJECTS_VIEW, backToTopicsLink);
    }

    /**
     * Show Feedback gives the end of game feedback to the user
     * @param text Results
     */
    public void showFeedbackPopup(String text) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Feedback");

        TextArea feedbackTextArea = new TextArea(text);
        feedbackTextArea.setEditable(false);
        feedbackTextArea.setWrapText(true);
        feedbackTextArea.setPrefSize(400, 200);

        ScrollPane scrollPane = new ScrollPane(feedbackTextArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        VBox layout = new VBox(scrollPane);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 450, 250);
        popupStage.setScene(scene);
        popupStage.show();
    }
}
