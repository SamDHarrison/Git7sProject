package git7s.flashcardai;

import java.util.Date;

public class Result {

    private int resultID;
    private int userID;
    private int cardID;
    private Date date;
    private boolean correct;


    public Result(int resultID, int userID, int cardID, Date date, boolean correct) {
        this.resultID = resultID;
        this.userID = userID;
        this.cardID = cardID;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
