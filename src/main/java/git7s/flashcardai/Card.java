package git7s.flashcardai;

import java.util.Date;

public class Card {

    private int cardID;
    private int creatorID;
    private String topic;
    private String subject;
    private String question;
    private String correct;
    private String incorrect1;
    private String incorrect2;
    private String incorrect3;
    private boolean isPublic;


    public Card(int cardID, int creatorID, String topic, String subject, String question, String correct, String incorrect1, String incorrect2, String incorrect3, boolean isPublic) {
        this.cardID = cardID;
        this.creatorID = creatorID;
        this.topic = topic;
        this.subject = subject;
        this.question = question;
        this.correct = correct;
        this.incorrect1 = incorrect1;
        this.incorrect2 = incorrect2;
        this.incorrect3 = incorrect3;
        this.isPublic = isPublic;
    }

    public int getCardID() {
        return cardID;
    }

    public void setCardID(int cardID) {
        this.cardID = cardID;
    }

    public int getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(int creatorID) {
        this.creatorID = creatorID;
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public String getIncorrect1() {
        return incorrect1;
    }

    public void setIncorrect1(String incorrect1) {
        this.incorrect1 = incorrect1;
    }

    public String getIncorrect2() {
        return incorrect2;
    }

    public void setIncorrect2(String incorrect2) {
        this.incorrect2 = incorrect2;
    }

    public String getIncorrect3() {
        return incorrect3;
    }

    public void setIncorrect3(String incorrect3) {
        this.incorrect3 = incorrect3;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
