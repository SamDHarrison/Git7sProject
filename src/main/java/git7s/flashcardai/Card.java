package git7s.flashcardai;

import java.util.Date;

public class Card {

    private int cardID;
    private int userID;
    private String topic;
    private String subject;
    private String front;
    private String back;
    /// Constructor
    public Card(int cardID, int userID, String topic, String subject, String front, String back) {
        this.cardID = cardID;
        this.userID = userID;
        this.topic = topic;
        this.subject = subject;
        this.front = front;
        this.back = back;
    }

    public int getCardID() {
        return cardID;
    }

    public void setCardID(int cardID) {
        this.cardID = cardID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}
