package git7s.flashcardai;

import java.util.Date;

public class Card {

    private int cardID;
    private int userID;
    private String topic;
    private String subject;
    private String front;
    private String back;

    public Card(int cardID, int userID, String topic, String subject, String front, String back) {
        this.cardID = cardID;
        this.userID = userID;
        this.topic = topic;
        this.subject = subject;
        this.front = front;
        this.back = back;
    }

    //getters
    public int getCardID() {
        return cardID;
    }

    public int getUserID() {
        return userID;
    }

    public String getFront() {
        return front;
    }

    public String getBack() {
        return back;
    }

    public String getTopic() {
        return topic;
    }

    public String getSubject() {
        return subject;
    }


    //setters
    public void setCardID(int cardID) {
        this.cardID = cardID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }

    //methods
    //Flip card to back when user ready to review answer within test mode
    public String flip() {
        return this.back;}

}
