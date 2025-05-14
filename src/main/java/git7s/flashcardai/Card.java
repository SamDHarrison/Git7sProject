package git7s.flashcardai;
/**
 * A Simple Card Class that represents a flashcard - linking the question to the subject, topic and user.
 */
public class Card {
    /**
     * ID of the Card Object - autoincremented when a card is added to the Cards table in SQL.
     */
    private int cardID;
    /**
     * The userID of the user that created the card (student id)
     */
    private int userID;
    /**
     *  Topic (week) of the subject
     */
    private String topic;
    /**
     * Subject the user is studying
     */
    private String subject;
    /**
     * Front text (question) of the flash card
     */
    private String front;
    /**
     * Backtext (answer) of the flash card)
     */
    private String back;
    /**
     * Constructs a new Card with the specified userID, topic, subject, front and back. CardID is autoincremented by SQL.
     * @param userID The ID of the user who made the card.
     * @param topic The name of the topic the card belongs to
     * @param subject The name of the subject the card belongs to
     * @param front The front text of the flashcard
     * @param back  The back text of the flashcard.
     */
    public Card(int userID, String topic, String subject, String front, String back) {
        this.userID = userID;
        this.topic = topic;
        this.subject = subject;
        this.front = front;
        this.back = back;
    }

    // Constructor used when loading an existing card from the database (cardID already assigned)
    public Card(int cardID, int userID, String topic, String subject, String front, String back) {
        this.cardID = cardID;
        this.userID = userID;
        this.topic = topic;
        this.subject = subject;
        this.front = front;
        this.back = back;
    }

    //Getters
    /**
     * Get the current ID of the card, most likely to search the db
     * @return CardID
     */
    public int getCardID() {
        return cardID;
    }
    /**
     * Get the user ID of the card, most likely to search the db for all cards the user created.
     * @return The student's ID
     */
    public int getUserID() {
        return userID;
    }
    /**
     * Get the front text
     * @return The front text prompt
     */
    public String getFront() {
        return front;
    }
    /**
     * Get the back text
     * @return The back text prompt
     */
    public String getBack() {
        return back;
    }
    /**
     * Get the topic text
     * @return The topic (String)
     */
    public String getTopic() {
        return topic;
    }
    /**
     * Get the subject text
     * @return The subject (String)
     */
    public String getSubject() {
        return subject;
    }


    //Setters
    /**
     * Set the card's ID
     * @param cardID The cards ID
     */
    public void setCardID(int cardID) {
        this.cardID = cardID;
    }
    /**
     * Set the card's userID
     * @param userID The user's student ID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }
    /**
     * Set the card's front text
     * @param front The String front text
     */
    public void setFront(String front) {
        this.front = front;
    }
    /**
     * Set the card's back text
     * @param back the String back text
     */
    public void setBack(String back) {
        this.back = back;
    }
    /**
     * Set the card's topic
     * @param topic The card's topic
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }
    /**
     * Set the card's subject
     * @param subject The card's subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Returns the flipped card value (for testing)
     * @return Flipped card (back value)
     */
    public String flip() {
        return getBack();
    }
}
