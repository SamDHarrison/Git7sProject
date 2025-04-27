package git7s.flashcardai;

import java.sql.Timestamp;

public class Result {

    private int resultID;
    private int userID;
    private int cardID;
    private Timestamp at;
    private boolean correct;


    public Result(int resultID, int userID, int cardID, Timestamp at, boolean correct) {
        this.resultID = resultID;
        this.userID = userID;
        this.cardID = cardID;
        this.at = at;
        this.correct = correct;
    }

    public int getResultID() {
        return resultID;
    }

    public void setResultID(int resultID) {
        this.resultID = resultID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getCardID() {
        return cardID;
    }

    public void setCardID(int cardID) {
        this.cardID = cardID;
    }

    public Timestamp getAt() {
        return at;
    }

    public void setAt(Timestamp at) {
        this.at = at;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
