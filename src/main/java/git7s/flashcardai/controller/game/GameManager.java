package git7s.flashcardai.controller.game;

import git7s.flashcardai.Main;
import git7s.flashcardai.dao.CardDAO;
import git7s.flashcardai.dao.ResultDAO;
import git7s.flashcardai.model.Card;
import git7s.flashcardai.model.CardManager;
import git7s.flashcardai.model.Result;
import git7s.flashcardai.model.ResultManager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        switch (Main.currentGameMode) {
            case 4: {
                cardDeck = cardManager.searchByUserID(Main.loggedInUserID);
                break;
            }
            case 3: {

                List<Card> tempDeck = cardManager.searchByUserID(Main.loggedInUserID);
                int required = Integer.parseInt(Main.currentDeck);

                cardDeck = new ArrayList<>();

                cardDeck.add(tempDeck.getFirst());
                while (cardDeck.size() <= required) {
                    Random random = new Random();
                    int choice = random.nextInt(tempDeck.size());
                    Card card = tempDeck.get(choice);
                    if (!cardDeck.contains(card)){
                        cardDeck.add(card);
                    }
                }
                break;
            }
            case 1: {
                cardDeck = cardManager.searchCardsByTopic(Main.currentDeck);
                break;
            }
            default: {
                cardDeck = cardManager.searchCardsBySubject(Main.currentDeck);
                break;
            }
        }
    }

    /**
     * Sets the results
     * @param result Boolean that specifies outcome of flashcard
     */
    public void setResult(boolean result) {
        Card card = cardDeck.get(deckTracker-1);
        resultManager.addResult(new Result(Main.loggedInUserID, card.getCardID(), new Timestamp(System.currentTimeMillis()), result, card.getSubject(), card.getTopic()));
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
        if (deckTracker < cardDeck.size()) {
            deckTracker++;
        }
        else {
            gamestate = GAMESTATE.GAME_FINISH;
        }
    }

    public String generateTitle(){
        if (Main.currentGameMode == 4){
            return "Testing all subjects";
        } else if (Main.currentGameMode == 3) {
            return "Random Test";
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
