package git7s.flashcardai.dao;

import git7s.flashcardai.Card;
import git7s.flashcardai.DatabaseConnection;
import git7s.flashcardai.Result;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardDAO {

    private Connection connection;

    public CardDAO(){
        connection = DatabaseConnection.getInstance();
    }

    public void createTable(){
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS cards ("
                            + "cardID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL, "
                            + "userID INTEGER NOT NULL, "
                            + "topic VARCHAR NOT NULL, "
                            + "subject VARCHAR NOT NULL, "
                            + "front VARCHAR NOT NULL, "
                            + "back VARCHAR NOT NULL"
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void insert(Card card){
        try {
            PreparedStatement insertCard = connection.prepareStatement(
            "INSERT INTO cards (cardID, userID, topic, subject, front, back) VALUES (?, ?, ?, ?, ?, ?)"
            );
            insertCard.setInt(1, card.getCardID());
            insertCard.setInt(2, card.getUserID());
            insertCard.setString(3, card.getTopic());
            insertCard.setString(4, card.getSubject());
            insertCard.setString(5, card.getFront());
            insertCard.setString(6, card.getBack());
            insertCard.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void update(int cardID, String newTopic, String newSubject, String newFront, String newBack){
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

    public void delete(int cardID){
        try{
            PreparedStatement getStatement = connection.prepareStatement("DELETE * FROM cards WHERE cardID = ?");
            getStatement.setInt(1, cardID);
            getStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
                Card card = new Card(cardID, userID, topic, subject, front, back);
                cards.add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cards;
    }

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
                Card card = new Card(cardID, userID, topic, subject, front, back);
                cards.add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cards;
    }

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
                Card card = new Card(cardID, userID, topic, subject, front, back);
                cards.add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cards;
    }

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
                Card card = new Card(cardID, userID, topic, subject, front, back);
                cards.add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cards;
    }

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
                Card card = new Card(cardID, userID, topic, subject, front, back);
                return card;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close(){
        try {
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
}
