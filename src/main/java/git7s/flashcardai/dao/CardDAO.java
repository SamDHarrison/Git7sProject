package git7s.flashcardai.dao;

import git7s.flashcardai.Card;
import git7s.flashcardai.DatabaseConnection;

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
     * Creates a Table in the database if not already created.
     */
    public void createTable(){
        open();
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
        close();
    }

    /**
     * Inserts a card to the db
     * @param card New Card for insertion
     */
    public void insert(Card card){
        open();
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
        close();
    }

    /**
     * Updates a card in the db
     * @param cardID New CardID
     * @param newTopic New topic
     * @param newSubject New Subject
     * @param newFront New Front text
     * @param newBack New back text
     */
    public void update(int cardID, String newTopic, String newSubject, String newFront, String newBack){
        open();
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
        close();
    }

    /**
     * Deletes a card from the db
     * @param cardID Card to be deleted
     */
    public void delete(int cardID){
        open();
        try{
            PreparedStatement getStatement = connection.prepareStatement("DELETE * FROM cards WHERE cardID = ?");
            getStatement.setInt(1, cardID);
            getStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
    }

    /**
     * Get by userID - pulls all cards for the specified userID
     * @param userIDQuery Specified user ID
     * @return List of Cards by user ID
     */
    public List<Card> getByUserID(int userIDQuery){
        open();
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
        close();
        return cards;
    }
    /**
     * Get by userID - pulls all cards for the specified topic
     * @param topicQuery Specified topic ID
     * @return List of Cards by topic ID
     */
    public List<Card> getByTopic(String topicQuery){
        open();
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
        close();
        return cards;
    }
    /**
     * Get by userID - pulls all cards for the specified subject
     * @param subjectQuery Specified subject ID
     * @return List of Cards by subject ID
     */
    public List<Card> getBySubject(String subjectQuery){
        open();
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
        close();
        return cards;
    }

    /**
     * Gets all records from the db
     * @return List of all records
     */
    public List<Card> getAll(){
        open();
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
                Card card = new Card(userID, topic, subject, front, back);
                card.setCardID(cardID);
                cards.add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        close();
        return cards;
    }

    /**
     * Gets a specific card by its ID
     * @param cardIDQuery Specified Card
     * @return Card Object
     */
    public Card getById(int cardIDQuery) {
        open();
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
        close();
        return null;
    }

    /**
     * Open the DB connection
     */
    public void open(){
        connection = DatabaseConnection.getInstance();
    }

    /**
     * Close the DB connection
     */
    public void close(){
        try {
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void deleteBySubjectAndTopic(int userID, String subject, String topic) {
        open();
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
        close();
    }

    public void updateCard(Card card) {
        open();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE cards SET front = ?, back = ? WHERE cardID = ?"
            );
            ps.setString(1, card.getFront());
            ps.setString(2, card.getBack());
            ps.setInt(3, card.getCardID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
    }
}
