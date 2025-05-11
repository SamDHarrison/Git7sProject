
package git7s.flashcardai;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * A class to control the GUI for test screen
 */
public class CardDeckController {
    /**
     * This button says the user answered correctly, and cycles the game
     */
    public Button correctAnswerButton;
    /**
     * This button says the user answered incorrectly
     */
    public Button incorrectAnswerButton;
    /**
     * This label controls the title text
     */
    public Label titleLabel;
    /**
     * This Button gets the AIs in depth explanation
     */
    public Button askAI;
    /**
     * This label displays the current flashcard front or back
     */
    @FXML private Label flashcardLabel;
    /**
     * This label shows the current position in the test
     */
    @FXML private Label flashcardCounterLabel;
    /**
     * This button flips the card
     */
    @FXML private Button flipCardButton;
    /**
     * This button takes the user back to the main screen
     */
    @FXML private Hyperlink backToTopicsLink;
    /**
     * This boolean determines whether the question is currently being displayed
     */
    private boolean showingQuestion = true;
    /**
     * This boolean displays the current results
     */
    private boolean[] results;
    /**
     * This boolean holds whether finished or still playing, cueing the next button to escape to the topics page.
     */
    private boolean finished = false;
    /**
     * This list holds all the cards the user is currently playing
     */
    private List<Card> cardDeck;
    /**
     * The current deck of cards being played (topicID)
     */
    public static String currentDeck;
    /**
     * How far through the cards is the user?
     */
    public static int currentDeckProgress;
    /**
     * This int handles the state of the async response - lets the user know when the API response has returned.
     */
    int manageAsyncResponse = 0;
    /**
     * This timeline helps to handle the Async Response
     */
    Timeline timeline = new Timeline();
    /**
     * This LLMGenerator class calls the generation of API prompt (REST)
     */
    LLMGenerator llm = new LLMGenerator();
    /**
     * A method that runs when the FXML GUI is first loaded:
     * 1. Pull the DB for the Card Deck
     * 2. Setup of labels and text items
     * 3. Game initialise
     */
    @FXML
    public void initialize() {
        //Pull all topic cards
        if (!Objects.equals(currentDeck, "ALL")) {
            cardDeck = Main.cardDAO.getByTopic(currentDeck);
        } else {
            cardDeck = Main.cardDAO.getByUserID(Main.loggedInUser.getId());
            System.out.println(currentDeck);
        }

        titleLabel.setText(cardDeck.getFirst().getSubject() + ": " + currentDeck);
        results = new boolean[cardDeck.size()];
        currentDeckProgress = 0;
        updateFlashcard();

        backToTopicsLink.setOnAction(event -> {
            finished = true;
            writeResult(true);
        });
        flashcardCounterLabel.setText((currentDeckProgress+1) + " of " + cardDeck.size());
        flipCardButton.setOnAction(event -> flipCard());
        correctAnswerButton.setOnAction(event -> writeResult(true));
        incorrectAnswerButton.setOnAction(event -> writeResult(false));
        askAI.setOnAction(event -> getAIExplanation());

    }
    /**
     * Move to the next card in the deck, if finished, show the results
     */
    private void nextCard() {
        if (!finished) {
            currentDeckProgress++;
            showingQuestion = true;
            flashcardCounterLabel.setText((currentDeckProgress+1) + " of " + cardDeck.size());
            updateFlashcard();
        } else {
            flipCardButton.setVisible(false);
            incorrectAnswerButton.setVisible(false);
            flashcardCounterLabel.setVisible(false);
            askAI.setVisible(false);
            titleLabel.setText("Results: " + currentDeck);
            setResults();

        }
    }

    /**
     * Flip the current card, show the answer
     */
    private void flipCard() {
        showingQuestion = !showingQuestion;
        updateFlashcard();
    }

    /**
     * Update the current flashcard, show the correct text. If showing front, show the correct buttons, vice versa
     */
    private void updateFlashcard() {
        if (showingQuestion) {
            incorrectAnswerButton.setVisible(false);
            correctAnswerButton.setVisible(false);
            flipCardButton.setVisible(true);
            askAI.setVisible(false);
            flashcardLabel.setText(cardDeck.get(currentDeckProgress).getFront());
        } else {
            askAI.setVisible(true);
            incorrectAnswerButton.setVisible(true);
            correctAnswerButton.setVisible(true);
            flipCardButton.setVisible(false);
            flashcardLabel.setText(cardDeck.get(currentDeckProgress).getBack());
        }
    }

    /**
     * Write the results to the boolean[] to be displayed in the nextCard() function
     * @param answer
     */
    private void writeResult(boolean answer) {
        if (finished) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/git7s/flashcardai/my-subjects-view.fxml"));
                Parent root = fxmlLoader.load();

                Stage stage = (Stage) correctAnswerButton.getScene().getWindow();
                stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
                stage.setTitle("Flashcard AI - My Subjects");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            if ((currentDeckProgress+1) < cardDeck.size()) {
                results[currentDeckProgress] = answer;
                Main.resultDAO.insert(new Result(Main.loggedInUser.getId(), cardDeck.get(currentDeckProgress).getCardID(), new Timestamp(System.currentTimeMillis()), answer));
            } else {
                finished = true;
            }

            nextCard();
        }
    }

    /**
     * Generate the results for the user
     */
    public void setResults() {
        String cardResultsText = "";
        int iterate = 0;
        for (int i= 0; i < cardDeck.size(); i++) {
            if (results[i] == true){
                cardResultsText += "Question " + i + ": Correct!\n";
            }
            else {
                cardResultsText += "Question " + i + ": Incorrect...\n";
            }
        }
        String resultsText = "Well done, you achieved the following results:\n\n" + cardResultsText;
        flashcardLabel.setText(resultsText);
        flashcardLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: normal; -fx-text-alignment: left;");
    }

    /**
     * Simple Alert GUI if needed.
     * @param message What is shown to the user
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * This method gets an explanation from the AI for the current flashcard answer
     */
    private void getAIExplanation(){
        String query = "Please explain this concept in a paragraph: " +
                cardDeck.get(currentDeckProgress).getFront() + " : " + cardDeck.get(currentDeckProgress).getBack();
        llm.fetchResponse(query);
        incorrectAnswerButton.setVisible(false);
        correctAnswerButton.setVisible(false);
        flipCardButton.setVisible(false);
        askAI.setVisible(false);
        flashcardCounterLabel.setText("AI is thinking...");
        timeline = getTimeline();
        timeline.play();
        showAlert("Generation takes 10-15 seconds, please be patient!");

    }

    /**
     * Manage the async response - returns a timeline that is referenced to confirm completion of the API response.
     * @return The Timeline to be later referenced.
     */
    private Timeline getTimeline() {
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    if (llm.checkResponse()) {
                        timeline.stop(); // Stop the timeline once done
                        incorrectAnswerButton.setVisible(true);
                        correctAnswerButton.setVisible(true);
                        String jsonBody = llm.getResponse().body();
                        JsonObject jsonObject = JsonParser.parseString(jsonBody).getAsJsonObject();
                        String responseParam = jsonObject.get("response").getAsString();
                        flashcardLabel.setText("The AI has responded: " + responseParam);
                    }
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        return timeline;
    }

}

