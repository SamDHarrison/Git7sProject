package git7s.flashcardai.dao;

import git7s.flashcardai.model.Card;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The DAO object for interacting with the Card Table over the specified connection
 */
public class CardDAO {
    /**
     * The connection used for connecting to the db
     */
    private Connection connection;

    /**
     * The Constructor which gets the static connection from the main class
     */
    public CardDAO() {
        connection = DatabaseConnection.getInstance();
        createTable();
    }
    /**
     * Creates a Table in the database if not already created.
     */
    public void createTable(){
        
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS cards ("
                            + "cardID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                            + "userID INTEGER NOT NULL, "
                            + "topic VARCHAR NOT NULL, "
                            + "subject VARCHAR NOT NULL, "
                            + "front VARCHAR NOT NULL, "
                            + "back VARCHAR NOT NULL, "
                            + "FOREIGN KEY (userID) REFERENCES users(id)"
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        
    }

    /**
     * Inserts a card to the db
     * @param card New Card for insertion
     */
    public void insert(Card card){
        
        try {
            PreparedStatement insertCard = connection.prepareStatement(
                    "INSERT INTO cards (userID, topic, subject, front, back) VALUES (?, ?, ?, ?, ?)"
            );
            insertCard.setInt(1, card.getUserID());
            insertCard.setString(2, card.getTopic());
            insertCard.setString(3, card.getSubject());
            insertCard.setString(4, card.getFront());
            insertCard.setString(5, card.getBack());
            insertCard.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        
    }

    /**
     * Updates a card in the db
     * @param card The new card
     */
    public void update(Card card){
        int cardID = card.getCardID();
        String newSubject = card.getSubject();
        String newTopic = card.getTopic();
        String newFront = card.getFront();
        String newBack = card.getBack();

        try{
            PreparedStatement updateStatement = connection.prepareStatement("UPDATE cards SET topic = ?, subject = ?, front = ?, back = ? WHERE cardID = ?");
            updateStatement.setInt(5, cardID);
            updateStatement.setString(1, newTopic);
            updateStatement.setString(2, newSubject);
            updateStatement.setString(3, newFront);
            updateStatement.setString(4, newBack);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    /**
     * Deletes a card from the db
     * @param cardID Card to be deleted
     */
    public void delete(int cardID){
        
        try{
            PreparedStatement getStatement = connection.prepareStatement("DELETE FROM cards WHERE cardID = ?");
            getStatement.setInt(1, cardID);
            getStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    /**
     * Get by userID - pulls all cards for the specified userID
     * @param userIDQuery Specified user ID
     * @return List of Cards by user ID
     */
    public List<Card> getByUserID(int userIDQuery){
        
        List<Card> cards = new ArrayList<>();
        try {
            PreparedStatement getStatement = connection.prepareStatement("SELECT * FROM cards WHERE userID = ?");
            getStatement.setInt(1, userIDQuery);
            ResultSet resultSet = getStatement.executeQuery();
            while (resultSet.next()){
                int cardID = resultSet.getInt("cardID");
                int userID = resultSet.getInt("userID");
                String topic = resultSet.getString("topic");
                String subject = resultSet.getString("subject");
                String front = resultSet.getString("front");
                String back = resultSet.getString("back");
                Card card = new Card(userID, topic, subject, front, back);
                card.setCardID(cardID);
                cards.add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return cards;
    }
    /**
     * Get by userID - pulls all cards for the specified topic
     * @param topicQuery Specified topic ID
     * @return List of Cards by topic ID
     */
    public List<Card> getByTopic(String topicQuery){
        
        List<Card> cards = new ArrayList<>();
        try {
            PreparedStatement getStatement = connection.prepareStatement("SELECT * FROM cards WHERE topic = ?");
            getStatement.setString(1, topicQuery);
            ResultSet resultSet = getStatement.executeQuery();
            while (resultSet.next()){
                int cardID = resultSet.getInt("cardID");
                int userID = resultSet.getInt("userID");
                String topic = resultSet.getString("topic");
                String subject = resultSet.getString("subject");
                String front = resultSet.getString("front");
                String back = resultSet.getString("back");
                Card card = new Card(userID, topic, subject, front, back);
                card.setCardID(cardID);
                cards.add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return cards;
    }
    /**
     * Get by userID - pulls all cards for the specified subject
     * @param subjectQuery Specified subject ID
     * @return List of Cards by subject ID
     */
    public List<Card> getBySubject(String subjectQuery){
        
        List<Card> cards = new ArrayList<>();
        try {
            PreparedStatement getStatement = connection.prepareStatement("SELECT * FROM cards WHERE subject = ?");
            getStatement.setString(1, subjectQuery);
            ResultSet resultSet = getStatement.executeQuery();
            while (resultSet.next()){
                int cardID = resultSet.getInt("cardID");
                int userID = resultSet.getInt("userID");
                String topic = resultSet.getString("topic");
                String subject = resultSet.getString("subject");
                String front = resultSet.getString("front");
                String back = resultSet.getString("back");
                Card card = new Card(userID, topic, subject, front, back);
                card.setCardID(cardID);
                cards.add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return cards;
    }

    /**
     * Gets all records from the db
     * @return List of all records
     */
    public List<Card> getAll(){
        
        List<Card> cards = new ArrayList<>();
        try {
            Statement insertStatement = connection.createStatement();
            String query = "SELECT * FROM cards";
            ResultSet resultSet = insertStatement.executeQuery(query);
            while (resultSet.next()){
                int cardID = resultSet.getInt("cardID");
                int userID = resultSet.getInt("userID");
                String topic = resultSet.getString("topic");
                String subject = resultSet.getString("subject");
                String front = resultSet.getString("front");
                String back = resultSet.getString("back");
                Card card = new Card(userID, subject, topic, front, back);
                card.setCardID(cardID);
                cards.add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return cards;
    }

    /**
     * Gets a specific card by its ID
     * @param cardIDQuery Specified Card
     * @return Card Object
     */
    public Card getById(int cardIDQuery) {
        
        try{
            PreparedStatement getStatement = connection.prepareStatement("SELECT * FROM cards WHERE cardID = ?");
            getStatement.setInt(1, cardIDQuery);
            ResultSet resultSet = getStatement.executeQuery();
            if (resultSet.next()){
                int cardID = resultSet.getInt("cardID");
                int userID = resultSet.getInt("userID");
                String topic = resultSet.getString("topic");
                String subject = resultSet.getString("subject");
                String front = resultSet.getString("front");
                String back = resultSet.getString("back");
                Card card = new Card(userID, topic, subject, front, back);
                card.setCardID(cardID);
                return card;
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        
        return null;
    }
    

    /**
     * Deletes all flashcards for a specific user, subject, and topic.
     * Used when a user wants to remove an entire flashcard set.
     *
     * @param userID The ID of the user (student number).
     * @param subject The subject the flashcards belong to.
     * @param topic The topic (e.g., week) under which the flashcards are grouped.
     */
    public void deleteBySubjectAndTopic(int userID, String subject, String topic) {
        
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM cards WHERE userID = ? AND subject = ? AND topic = ?"
            );
            stmt.setInt(1, userID);
            stmt.setString(2, subject);
            stmt.setString(3, topic);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        
    }

    /**
     * Retrieves all flashcards for a specific user and topic.
     * Useful when loading a topic's flashcards for review or editing.
     *
     * @param topic The topic name (e.g., "Week 1").
     * @param userId The ID of the user who owns the flashcards.
     * @return A list of Card objects matching the given topic and user.
     */
    public List<Card> getByTopicAndUser(String topic, int userId) {
        List<Card> result = new ArrayList<>();
        
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM cards WHERE topic = ? AND userID = ?"
            );
            ps.setString(1, topic);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new Card(
                        rs.getInt("cardID"),
                        rs.getInt("userID"),
                        rs.getString("topic"),
                        rs.getString("subject"),
                        rs.getString("front"),
                        rs.getString("back")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return result;
    }
}
