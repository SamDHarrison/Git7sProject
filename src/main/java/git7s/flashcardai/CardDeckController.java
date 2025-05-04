
package git7s.flashcardai;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.util.List;

public class CardDeckController {

    public Button correctAnswerButton;
    public Button incorrectAnswerButton;
    public Label titleLabel;
    @FXML private Label flashcardLabel;
    @FXML private Label flashcardCounterLabel;
    @FXML private Button flipCardButton;
    @FXML private Hyperlink backToTopicsLink;

    private boolean showingQuestion = true;
    private boolean[] results;
    private boolean finished = false;

    private List<Card> cardDeck;

    @FXML
    public void initialize() {
        //Pull all topic cards
        if (Main.currentDeck != "ALL") {
            cardDeck = Main.cardDAO.getByTopic(Main.currentDeck);
        } else {
            cardDeck = Main.cardDAO.getByUserID(Main.loggedInUser.getId());
        }

        titleLabel.setText(Main.currentDeck);
        results = new boolean[cardDeck.size()];

        updateFlashcard();

        backToTopicsLink.setOnAction(event -> {
            System.out.println("Back to Topics clicked (no navigation implemented yet).");
        });
        flashcardCounterLabel.setText((Main.currentDeckProgress+1) + " of " + cardDeck.size());
        flipCardButton.setOnAction(event -> flipCard());
        correctAnswerButton.setOnAction(event -> writeResult(true));
        incorrectAnswerButton.setOnAction(event -> writeResult(false));

    }

    private void nextCard() {
        if (!finished) {
            Main.currentDeckProgress++;
            showingQuestion = true;
            flashcardCounterLabel.setText((Main.currentDeckProgress+1) + " of " + cardDeck.size());
            updateFlashcard();
        } else {
            flipCardButton.setVisible(false);
            incorrectAnswerButton.setVisible(false);
            flashcardCounterLabel.setVisible(false);
            titleLabel.setText("Results: " + Main.currentDeck);
            setResults();

        }
    }

    private void flipCard() {
        showingQuestion = !showingQuestion;
        updateFlashcard();
    }

    private void updateFlashcard() {
        if (showingQuestion) {
            incorrectAnswerButton.setVisible(false);
            correctAnswerButton.setVisible(false);
            flipCardButton.setVisible(true);
            flashcardLabel.setText(cardDeck.get(Main.currentDeckProgress).getFront());
        } else {
            incorrectAnswerButton.setVisible(true);
            correctAnswerButton.setVisible(true);
            flipCardButton.setVisible(false);
            flashcardLabel.setText(cardDeck.get(Main.currentDeckProgress).getBack());
        }
    }

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
        System.out.println(Main.currentDeckProgress);
        System.out.println(cardDeck.size());
        if ((Main.currentDeckProgress+2) < cardDeck.size()) {
            results[Main.currentDeckProgress] = answer;
            Main.resultDAO.insert(new Result(Main.loggedInUser.getId(), cardDeck.get(Main.currentDeckProgress).getCardID(), new Timestamp(System.currentTimeMillis()), answer));
        } else {
            finished = true;
        }

        nextCard();

    }

    public void setResults() {
        String cardResultsText = "";
        int iterate = 0;
        for (Card card : cardDeck) {
            cardResultsText += "Question: " + card.getFront() + "\nAnswer: " + card.getBack() +
                    "\nYou answered: " + Boolean.toString(results[iterate]) + "\n";
            iterate++;
        }
        String resultsText = "Well done, you achieved the following results:\n" + cardResultsText;
        flashcardLabel.setText(resultsText);
        flashcardLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: normal; -fx-text-alignment: left;");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

