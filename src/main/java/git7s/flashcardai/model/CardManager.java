package git7s.flashcardai.model;

import git7s.flashcardai.Main;
import git7s.flashcardai.dao.CardDAO;
import git7s.flashcardai.dao.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The DAO object for interacting with the Card Table over the specified connection
 */
public class CardManager {
    /**
     * Local DAO object that is filled by calling screen
     */
    private CardDAO cardDAO;
    /**
     * The Constructor which takes the caller's DAO object and updates local
     *
     * @param cardDAO
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
                .filter(card -> (card.getSubject().equalsIgnoreCase(subjectQuery)))
                .toList();
    }

    /**
     * Search Function that gets a list of cards for handling GUI-side
     */
    public List<Card> searchCardsByTopic(String topicQuery) {
        return cardDAO.getAll()
                .stream()
                .filter(card -> (card.getTopic().equalsIgnoreCase(topicQuery)))
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
    public void deleteSubject(String subject, String topic){
        cardDAO.deleteBySubjectAndTopic(Main.loggedInUserID, subject, topic);
    }

    /**
     * Search Function that gets a specific card
     */
    public Card getCardID(int cardID) {
        return cardDAO.getById(cardID);
    }

    /**
     * Update a user's card
     * @param card The new card
     */
    public void updateCard(Card card){
        cardDAO.update(card);
    }

    /**
     * Add many cards for AI generation
     * @param cards Generated cards
     */
    public void addBulk(List<Card> cards) {
        for (Card card : cards) {
            cardDAO.insert(card);
        }
    }
}