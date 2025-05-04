package git7s.flashcardai.dao;

import git7s.flashcardai.Card;
import git7s.flashcardai.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardDAO {

    private Connection connection;

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


    public void open(){
        connection = DatabaseConnection.getInstance();
    }
    public void close(){
        try {
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
}
