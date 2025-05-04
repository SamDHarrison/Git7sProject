
package git7s.flashcardai;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

public class CardDeckController {

    @FXML private Label flashcardLabel;
    @FXML private Label flashcardCounterLabel;
    @FXML private Button nextCardButton;
    @FXML private Button prevCardButton;
    @FXML private Button shuffleDeckButton;
    @FXML private Button flipCardButton;
    @FXML private Hyperlink backToTopicsLink;

    private int currentCardIndex = 0;
    private boolean showingQuestion = true;

    private final String[][] flashcards = {
            {"Q: What is the purpose of the main method in a Java program?", "A: It serves as the entry point for the program."},
            {"Q: What is Encapsulation?", "A: Encapsulation is the bundling of data and methods."},
            {"Q: What is Inheritance?", "A: Inheritance allows a class to use properties of another class."},
            {"Q: What is Polymorphism?", "A: Polymorphism allows methods to perform different tasks."}
    };

    @FXML
    public void initialize() {
        updateFlashcard();

        backToTopicsLink.setOnAction(event -> {
            System.out.println("Back to Topics clicked (no navigation implemented yet).");
        });

        flipCardButton.setOnAction(event -> flipCard());
        nextCardButton.setOnAction(event -> nextCard());
        prevCardButton.setOnAction(event -> prevCard());
        shuffleDeckButton.setOnAction(event -> shuffleDeck());
    }

    private void nextCard() {
        if (currentCardIndex < flashcards.length - 1) {
            currentCardIndex++;
            showingQuestion = true; // Reset to question side
            updateFlashcard();
        }
    }

    private void prevCard() {
        if (currentCardIndex > 0) {
            currentCardIndex--;
            showingQuestion = true; // Reset to question side
            updateFlashcard();
        }
    }

    private void shuffleDeck() {
        currentCardIndex = 0;
        showingQuestion = true; // Reset to question side
        updateFlashcard();
    }

    private void flipCard() {
        showingQuestion = !showingQuestion;
        updateFlashcard();
    }

    private void updateFlashcard() {
        String content = showingQuestion
                ? flashcards[currentCardIndex][0]
                : flashcards[currentCardIndex][1];
        flashcardLabel.setText(content);
        flashcardCounterLabel.setText((currentCardIndex + 1) + " of " + flashcards.length);
    }
}

