package git7s.flashcardai.service;

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
public class GameService {
    /**
     * Protected constructor - cannot be instantiated
     */
    protected GameService() {
        cardManager = new CardManager(new CardDAO());
        resultManager = new ResultManager(new ResultDAO());
    }
    /**
     * Subclass which acts as container
     */
    private static class Game {
        private static final GameService INSTANCE = new GameService();
    }

    /**
     * getInstance returns the static INSTANCE of AdminService
     * @return INSTANCE
     */
    public static GameService getInstance() {
        return GameService.Game.INSTANCE;
    }
    /**
     * Stores the currently selected topic to remediate transition between GUIs
     */
    private static String currentDeck;
    /**
     * Stores the currently selected gamemode 0 - subject, 1 - topic, 2 - subject targeted, 3 - random, 4 - all
     */
    public static int currentGameMode;
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
    private final CardManager cardManager;
    /**
     * Result Manager for posting results
     */
    private final ResultManager resultManager;
    /**
     * Set possible gamestates
     */
    public enum GAMESTATE {
        GAME_PLAY, GAME_AI, GAME_FINISH
    }
    /**
     * Track game state
     */
    private GAMESTATE gamestate;
    /**
     * New Game sets up a new game
     */
    public void newGame(){
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
     * Sets up the current card deck which is used at the start of gameplay to init
     */
    private void setupDeck() {
        switch (currentGameMode) {
            case 4: {
                cardDeck = cardManager.searchByUserID(SessionService.getInstance().loggedInID);
                break;
            }
            case 3: {
                List<Card> tempDeck = cardManager.searchByUserID(SessionService.getInstance().loggedInID);
                int required = Integer.parseInt(currentDeck);
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
                cardDeck = cardManager.searchCardsByTopic(currentDeck);
                break;
            }
            default: {
                cardDeck = cardManager.searchCardsBySubject(currentDeck);
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
        resultManager.addResult(new Result(SessionService.getInstance().loggedInID, card.getCardID(), new Timestamp(System.currentTimeMillis()), result, card.getSubject(), card.getTopic()));
        this.results[deckTracker-1] = result;
    }
    /**
     * Tracks if the question or answer is currently showing
     * @return boolean True front is showing
     */
    public boolean isShowingFront() {
        return showingFront;
    }

    /**
     * Sets the flashcard to show front or back
     * @param showingFront State
     */
    public void setShowingFront(boolean showingFront) {
        this.showingFront = showingFront;
    }

    /**
     * Iterates the flashcards in the tracker
     */
    public void iterateFlashCardTracker() {
        if (deckTracker < cardDeck.size()) {
            deckTracker++;
        }
        else {
            gamestate = GAMESTATE.GAME_FINISH;
        }
    }

    /**
     * Generates the title of the game for the title label.
     * @return Title text
     */
    public String generateTitle(){
        if (currentGameMode == 4){
            return "Testing all subjects";
        } else if (currentGameMode == 3) {
            return "Random Test";
        }
        else {
            return "Testing " + currentDeck;
        }
    }

    /**
     * Generates the end of game results text
     * @return Results Text
     */
    public String generateResultText(){
        StringBuilder cardResultsText = new StringBuilder();
        for (int i= 1; i < cardDeck.size()+1; i++) {
            if (results[i - 1]){
                cardResultsText.append("Question ").append(i).append(": Correct!\n");
            }
            else {
                cardResultsText.append("Question ").append(i).append(": Incorrect...\n");
            }
        }
        return "Well done, you achieved the following results:\n\n" + cardResultsText;
    }

    /**
     * Gets what should be currently displayed
     * @return Flashcard Display Text
     */
    public String getFlashCardDisplay(){
        if (showingFront) {
            return cardDeck.get(deckTracker-1).getFront();
        } else {
            return cardDeck.get(deckTracker-1).getBack();
        }
    }

    /**
     * Generates text for the tracker label
     * @return Tracker text
     */
    public String generateTrackerText(){
        return deckTracker + " of " + cardDeck.size();
    }
    /**
     * Gets the latest front and back to input into the AI
     * @return String Query text
     */
    public String getTextForQuery(){
        return cardDeck.get(deckTracker-1).getFront() + " : " + cardDeck.get(deckTracker-1).getBack();
    }

    /// Getters and Setters
    public int getCurrentGameMode() {
        return currentGameMode;
    }

    public void setCurrentGameMode(int currentGameMode) {
        GameService.currentGameMode = currentGameMode;
    }

    public String getCurrentDeck() {
        return currentDeck;
    }

    public void setCurrentDeck(String currentDeck) {
        GameService.currentDeck = currentDeck;
    }

    public GAMESTATE getGamestate() {
        return gamestate;
    }

    public void setGamestate(GAMESTATE gamestate) {
        this.gamestate = gamestate;
    }
}
