package git7s.flashcardai.dao;

import git7s.flashcardai.model.Result;
import git7s.flashcardai.service.DatabaseService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * The DAO object for interacting with the Results Table over the specified connection
 */
public class ResultDAO implements IDAO<Result> {
    /**
     * The connection used for connecting to the db
     */
    private final Connection connection;
    /**
     * The Constructor which gets the static connection from the main class
     */
    public ResultDAO() {
        this.connection = DatabaseService.getInstance().getConnection();
        createTable();
    }
    /**
     * Constructor for testing with a custom database connection.
     * @param connection The connection to use.
     */
    public ResultDAO(Connection connection) {
        this.connection = connection;
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
            System.err.println(ex.getMessage());
        }
        
    }
    /**
     * Inserts a Result to the db
     * @param result New Result for insertion
     */
    public void insert(Result result){
        try {
            PreparedStatement insertResult = connection.prepareStatement(
                    "INSERT INTO results (userID, cardID, at, correct, subject, topic) VALUES (?, ?, ?, ?, ?, ?)"
            );
            insertResult.setInt(1, result.getUserID());
            insertResult.setInt(2, result.getCardID());
            insertResult.setTimestamp(3, result.getAt());
            insertResult.setBoolean(4, result.isCorrect());
            insertResult.setString(5, result.getSubject());
            insertResult.setString(6, result.getTopic());
            insertResult.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        
    }

    /**
     * Deletes the specified result
     * @param resultID Specified result
     */
    public void delete(int resultID){
        
        try{
            PreparedStatement getStatement = connection.prepareStatement("DELETE FROM results WHERE resultID = ?");
            getStatement.setInt(1, resultID);
            getStatement.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
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
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
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
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
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
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return results;

    }

    @Override
    public void update(Result result) {
        try {
            PreparedStatement updateResult = connection.prepareStatement(
                    "UPDATE results SET userID = ?, cardID = ?, at = ?, correct = ?, subject = ?, topic = ? WHERE resultID = ?)"
            );
            updateResult.setInt(1, result.getUserID());
            updateResult.setInt(2, result.getCardID());
            updateResult.setTimestamp(3, result.getAt());
            updateResult.setBoolean(4, result.isCorrect());
            updateResult.setString(5, result.getSubject());
            updateResult.setString(6, result.getTopic());
            updateResult.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    @Override
    public Result getByID(int ID) {
        try {
            PreparedStatement getStatement = connection.prepareStatement("SELECT * FROM results WHERE resultID = ?");
            getStatement.setInt(1, ID);
            ResultSet resultSet = getStatement.executeQuery();
            if (resultSet.next()){
                int resultID = resultSet.getInt("resultID");
                int userID = resultSet.getInt("userID");
                int cardID = resultSet.getInt("cardID");
                Timestamp at = resultSet.getTimestamp("at");
                boolean correct = resultSet.getBoolean("correct");
                String subject = resultSet.getString("subject");
                String topic = resultSet.getString("topic");
                Result result = new Result(userID, cardID, at, correct, subject, topic);
                result.setResultID(resultID);
                return result;
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }
    
}
