package git7s.flashcardai;

import java.sql.Timestamp;

/**
 * A simple Model Class that represents a result - Who, What, When, Correct?
 */
public class Result {
    /**
     * Auto-incremented by SQL DB - ResultID
     */
    private int resultID;
    /**
     * UserID is the user who had this result
     */
    private int userID;
    /**
     * The card that was tested on
     */
    private int cardID;
    /**
     * The Time it happened
     */
    private Timestamp at;
    /**
     * Did the user get the correct answer?
     */
    private boolean correct;

    /**
     * This constructor creates a new result
     * @param userID The user ID
     * @param cardID The card ID
     * @param at The time it happened
     * @param correct The result of the test
     */
    public Result(int userID, int cardID, Timestamp at, boolean correct) {
        this.userID = userID;
        this.cardID = cardID;
        this.at = at;
        this.correct = correct;
    }

    //Getters

    /**
     * This method returns the ResultID
     * @return Result ID
     */
    public int getResultID() {
        return resultID;
    }
    /**
     * This method returns the UserID
     * @return User ID
     */
    public int getUserID() {
        return userID;
    }
    /**
     * This method returns the CardID
     * @return Card ID
     */
    public int getCardID() {
        return cardID;
    }
    /**
     * This method returns the Timestamp
     * @return Timestamp
     */
    public Timestamp getAt() {
        return at;
    }
    /**
     * This method returns the result boolean
     * @return True (correct) or false (incorrect)
     */
    public boolean isCorrect() {
        return correct;
    }

    //setters
    /**
     * This method sets the ResultID
     */
    public void setResultID(int resultID) {
        this.resultID = resultID;
    }
    /**
     * This method sets the UserID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }
    /**
     * This method sets the CardID
     */
    public void setCardID(int cardID) {
        this.cardID = cardID;
    }
    /**
     * This method sets the Timestamp
     */
    public void setAt(Timestamp at) {
        this.at = at;
    }
    /**
     * This method sets the answer status
     */
    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
