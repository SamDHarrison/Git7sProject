package git7s.flashcardai.model;
import git7s.flashcardai.dao.CardDAO;
import git7s.flashcardai.service.SessionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * The DAO object for interacting with the Card Table over the specified connection
 */
public class CardManager {
    /**
     * Local DAO object that is filled by calling screen
     */
    private final CardDAO cardDAO;
    /**
     * The Constructor which takes the caller's DAO object and updates local
     *
     * @param cardDAO DAO that enables the Manager
     */
    public CardManager(CardDAO cardDAO) {
        this.cardDAO = cardDAO;
    }

    /**
     * Search Function that gets a list of cards for handling GUI-side
     */
    public List<Card> searchCardsBySubject(String subjectQuery) {
        return cardDAO.getAll()
                .stream()
                .filter(card -> (card.getSubject().equalsIgnoreCase(subjectQuery)) )
                .toList();
    }

    /**
     * Search Function that gets a list of cards for handling GUI-side
     */
    public List<Card> searchCardsByTopic(String topicQuery) {
        return cardDAO.getAll()
                .stream()
                .filter(card -> (card.getTopic().equalsIgnoreCase(topicQuery) && card.getUserID()==SessionService.getInstance().loggedInID))
                .toList();
    }

    /**
     * Inserts a Result to the db
     *
     * @param card New Card for insertion
     */
    public void addCard(Card card) {
        cardDAO.insert(card);
    }

    /**
     * Deletes the specified result
     *
     * @param cardID Specified result
     */
    public void delete(int cardID) {
        cardDAO.delete(cardID);
    }

    /**
     * Pulls all db results
     *
     * @return List of results
     */
    public List<Card> getAll() {
        return cardDAO.getAll();
    }

    /**
     * Gets results by the user who got the results
     *
     * @param userIDQuery The user ID
     * @return List of results
     */
    public List<Card> searchByUserID(int userIDQuery) {
        return cardDAO.getAll()
                .stream()
                .filter(card -> card.getUserID() == userIDQuery).
                toList();
    }

    /**
     * Mass delete (delete subject)
     */
    public void deleteSubject(String subject){
        cardDAO.deleteBySubject(SessionService.getInstance().loggedInID, subject);
    }
    /**
     * Mass delete (delete topic)
     */
    public void deleteTopic(String subject, String topic){
        cardDAO.deleteBySubjectAndTopic(SessionService.getInstance().loggedInID, subject, topic);
    }
    /**
     * Search Function that gets a specific card
     */
    public Card getCardID(int cardID) {
        return cardDAO.getByID(cardID);
    }

    /**
     * Update a user's card
     * @param card The new card
     */
    public void updateCard(Card card){
        cardDAO.update(card);
    }
    /**
     * This method takes a String s (subject) and returns a list of all the topics of that subject (for GUIs)
     * @param subject The input subject query
     * @return The ObservableList of the topics for the specified inputted subject
     */
    public ObservableList<String> topicSelection(String subject) {
        ObservableList<String> selectedTopics = FXCollections.observableArrayList();
        for (Card card : searchByUserID(SessionService.getInstance().loggedInID)) {
            if (card.getSubject().equals(subject) && !selectedTopics.contains(card.getTopic())) {
                selectedTopics.add(card.getTopic());
            }
        }
        return selectedTopics;
    }
}