package git7s.flashcardai.dao;

import git7s.flashcardai.model.Result;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * The DAO object for interacting with the Results Table over the specified connection
 */
public class ResultDAO {
    /**
     * The connection used for connecting to the db
     */
    private Connection connection;
    /**
     * The Constructor which gets the static connection from the main class
     */
    public ResultDAO() {
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
                    "CREATE TABLE IF NOT EXISTS results ("
                            + "resultID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                            + "userID INTEGER NOT NULL, "
                            + "cardID INTEGER NOT NULL, "
                            + "at TIMESTAMP NOT NULL, "
                            + "correct BIT NOT NULL, "
                            + "subject VARCHAR NOT NULL, "
                            + "topic VARCHAR NOT NULL, "
                            + "FOREIGN KEY (userID) REFERENCES users(id), "
                            + "FOREIGN KEY (cardID) REFERENCES cards(id)"
                            + ")"
            );
        } catch (SQLException ex) {

            System.err.println(ex);
        }
        
    }
    /**
     * Inserts a Result to the db
     * @param result New Result for insertion
     */
    public void insert(Result result){
        
        try {
            PreparedStatement insertResult = connection.prepareStatement(
                    "INSERT INTO results (userID, cardID, at, correct) VALUES (?, ?, ?, ?)"
            );
            insertResult.setInt(1, result.getUserID());
            insertResult.setInt(2, result.getCardID());
            insertResult.setTimestamp(3, result.getAt());
            insertResult.setBoolean(4, result.isCorrect());
            insertResult.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        
    }

    /**
     * Deletes the specified result
     * @param resultID Specified result
     */
    public void delete(int resultID){
        
        try{
            PreparedStatement getStatement = connection.prepareStatement("DELETE * FROM results WHERE resultID = ?");
            getStatement.setInt(1, resultID);
            getStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    /**
     * Gets results for a specified Card
     * @param cardIDQuery Specified Card
     * @return List of results
     */
    public List<Result> getByCardID(int cardIDQuery){

        List<Result> results = new ArrayList<>();
        try {
            PreparedStatement getStatement = connection.prepareStatement("SELECT * FROM results WHERE cardID = ?");
            getStatement.setInt(1, cardIDQuery);
            ResultSet resultSet = getStatement.executeQuery();
            while (resultSet.next()){
                int resultID = resultSet.getInt("resultID");
                int userID = resultSet.getInt("userID");
                int cardID = resultSet.getInt("cardID");
                Timestamp at = resultSet.getTimestamp("at");
                boolean correct = resultSet.getBoolean("correct");
                String subject = resultSet.getString("subject");
                String topic = resultSet.getString("topic");
                Result result = new Result(userID, cardID, at, correct, subject, topic);
                result.setResultID(resultID);
                results.add(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    /**
     * Pulls all db results
     * @return List of results
     */
    public List<Result> getAll(){
        List<Result> results = new ArrayList<>();
        try {
            Statement insertStatement = connection.createStatement();
            String query = "SELECT * FROM results";
            ResultSet resultSet = insertStatement.executeQuery(query);
            while (resultSet.next()){
                int resultID = resultSet.getInt("resultID");
                int userID = resultSet.getInt("userID");
                int cardID = resultSet.getInt("cardID");
                Timestamp at = resultSet.getTimestamp("at");
                boolean correct = resultSet.getBoolean("correct");
                String subject = resultSet.getString("subject");
                String topic = resultSet.getString("topic");
                Result result = new Result(userID, cardID, at, correct, subject, topic);
                result.setResultID(resultID);
                results.add(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return results;
    }

    /**
     * Gets results by the user who got the results
     * @param userIDQuery The user ID
     * @return List of results
     */
    public List<Result> getByUserID(int userIDQuery){

        List<Result> results = new ArrayList<>();
        try {
            PreparedStatement getStatement = connection.prepareStatement("SELECT * FROM results WHERE userID = ?");
            getStatement.setInt(1, userIDQuery);
            ResultSet resultSet = getStatement.executeQuery();
            while (resultSet.next()){
                int resultID = resultSet.getInt("resultID");
                int userID = resultSet.getInt("userID");
                int cardID = resultSet.getInt("cardID");
                Timestamp at = resultSet.getTimestamp("at");
                boolean correct = resultSet.getBoolean("correct");
                String subject = resultSet.getString("subject");
                String topic = resultSet.getString("topic");
                Result result = new Result(userID, cardID, at, correct, subject, topic);
                result.setResultID(resultID);
                results.add(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;

    }

    
}
