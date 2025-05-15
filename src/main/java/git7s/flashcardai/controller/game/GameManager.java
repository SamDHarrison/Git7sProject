package git7s.flashcardai.controller.game;

import git7s.flashcardai.Main;
import git7s.flashcardai.dao.CardDAO;
import git7s.flashcardai.dao.ResultDAO;
import git7s.flashcardai.model.Card;
import git7s.flashcardai.model.CardManager;
import git7s.flashcardai.model.ResultManager;

import java.util.List;

/**
 * Very simple class used to simplify the CardDeckController - acts as an advanced struct
 */
public class GameManager {
    /**
     * This boolean determines whether the question is currently being displayed
     */
    private boolean showingFront = true;
    /**
     * This boolean displays the current results
     */
    private boolean[] results;
    /**
     * This list holds all the cards the user is currently playing
     */
    private List<Card> cardDeck;
    /**
     * Tracks how far through the cards is the user?
     */
    private int deckTracker;
    /**
     * Card Manager for retrieving cards
     */
    private CardManager cardManager;
    /**
     * Result Manager for posting results
     */
    private ResultManager resultManager;
    /**
     * Set possible gamestates
     */
    public enum GAMESTATE {
        GAME_PLAY, GAME_AI, GAME_FINISH
    }
    /**
     * Track game state
     */
    public GAMESTATE gamestate;
    /**
     * Constructor - sets up the List and GameVars
     */
    public GameManager() {
        cardManager = new CardManager(new CardDAO());
        resultManager = new ResultManager(new ResultDAO());
        deckTracker = 1;
        gamestate = GAMESTATE.GAME_PLAY;
        showingFront = true;

        setupDeck();
        results = new boolean[cardDeck.size()];

    }

    /**
     * Gets the results to display to the user
     * @return boolean[] - True/False for each flashcard
     */
    public boolean[] getResults() {
        return results;
    }

    /**
     * Returns the current card deck which is used at the start of gameplay to init
     * @return cardDeck
     */
    private void setupDeck() {
        switch (Main.currentDeck) {
            case ("ALL"): {
                cardDeck = cardManager.searchByUserID(Main.loggedInUserID);
                break;
            }
            default: {
                cardDeck = cardManager.searchCardsByTopic(Main.currentDeck);
                break;
            }
        }
    }

    /**
     * Sets the results
     * @param result Boolean that specifies outcome of flashcard
     */
    public void setResult(boolean result) {
        this.results[deckTracker-1] = result;
    }
    /**
     * Tracks if the question or answer is currently showing
     * @return boolean True front is showing
     */
    public boolean isShowingFront() {
        return showingFront;
    }

    public void setShowingFront(boolean showingFront) {
        this.showingFront = showingFront;
    }

    public int getDeckTracker() {
        return deckTracker;
    }

    public void setDeckTracker(int deckTracker) {
        deckTracker = deckTracker;
    }

    public void iterateFlashCardTracker() {
        System.out.println(deckTracker + " " + cardDeck.size());
        if (deckTracker < cardDeck.size()) {
            deckTracker++;
        }
        else {
            gamestate = GAMESTATE.GAME_FINISH;
        }
    }

    public String generateTitle(){
        if (Main.currentDeck.equalsIgnoreCase("ALL")){
            return "Testing all subjects";
        }
        else {
            return "Testing " + Main.currentDeck;
        }
    }

    public String generateResultText(){
        String cardResultsText = "";
        int iterate = 0;
        for (int i= 1; i < cardDeck.size()+1; i++) {
            if (results[i-1] == true){
                cardResultsText += "Question " + i + ": Correct!\n";
            }
            else {
                cardResultsText += "Question " + i + ": Incorrect...\n";
            }
        }
        return "Well done, you achieved the following results:\n\n" + cardResultsText;
    }

    public String getFlashCardDisplay(){
        if (showingFront) {
            return cardDeck.get(deckTracker-1).getFront();
        } else {
            return cardDeck.get(deckTracker-1).getBack();
        }
    }

    public String generateTrackerText(){
        return deckTracker + " of " + cardDeck.size();
    }

    public GAMESTATE getGamestate() {
        return gamestate;
    }

    public void setGamestate(GAMESTATE gamestate) {
        this.gamestate = gamestate;
    }

    public String getTextForQuery(){
        return cardDeck.get(deckTracker-1).getFront() + " : " + cardDeck.get(deckTracker-1).getBack();
    }
}
