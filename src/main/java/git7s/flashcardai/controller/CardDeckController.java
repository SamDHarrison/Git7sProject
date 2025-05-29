package git7s.flashcardai.controller;


import git7s.flashcardai.Main;
import git7s.flashcardai.controller.game.GameManager;
import git7s.flashcardai.llm.LLMGenerator;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
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
public class CardDeckController {
    /**
     * This button says the user answered correctly, and cycles the game
     */
    @FXML
    public Button correctAnswerButton;
    /**
     * This button says the user answered incorrectly
     */
    @FXML
    public Button incorrectAnswerButton;
    /**
     * This label controls the title text
     */
    @FXML
    public Label titleLabel;
    /**
     * This Button gets the AIs in depth explanation
     */
    @FXML
    public Button askAI;
    /**
     * This label displays the current flashcard front or back
     */
    @FXML
    private Label flashcardLabel;
    /**
     * This label shows the current position in the test
     */
    @FXML
    private Label flashcardCounterLabel;
    /**
     * This button flips the card
     */
    @FXML
    private Button flipCardButton;
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
    private GameManager gameManager;

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
        gameManager = new GameManager();

        //Setup Elements
        titleLabel.setText(gameManager.generateTitle());
        //Buttons (need to fix)
        backToTopicsLink.setOnAction(event -> {returnToSubjectsView();});
        flipCardButton.setOnAction(event -> flipCard());
        correctAnswerButton.setOnAction(event -> writeResult(true));
        incorrectAnswerButton.setOnAction(event -> writeResult(false));
        askAI.setOnAction(event -> handleAIExplanation());
        //Final Setup to Begin
        updateFXMLElements();
        flashcardLabel.setText(gameManager.getFlashCardDisplay());

        setPrefColours(Main.prefCol);
    }

    /**
     * Change the card from front to back
     */
    private void flipCard(){
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
        if (gameManager.gamestate.equals(GameManager.GAMESTATE.GAME_AI)) {
            gameManager.setGamestate(GameManager.GAMESTATE.GAME_PLAY);
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
    private void handleAIExplanation(){
        gameManager.setGamestate(GameManager.GAMESTATE.GAME_AI);
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
                if(!gameManager.gamestate.equals(GameManager.GAMESTATE.GAME_FINISH)) {
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
    private void returnToSubjectsView(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/git7s/flashcardai/my-subjects-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) correctAnswerButton.getScene().getWindow();
            stage.setScene(new Scene(root, Main.WIDTH, Main.HEIGHT));
            stage.setTitle("Flashcard AI - My Subjects");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    private void setPrefColours(String colour){
        String s = "-fx-background-color: " + colour + "; -fx-text-fill: white; -fx-font-weight: bold;";

        flipCardButton.setStyle(s);
        correctAnswerButton.setStyle(s);
        correctAnswerButton.setStyle(s);
        incorrectAnswerButton.setStyle(s);
        askAI.setStyle(s);

    }


}
