package git7s.flashcardai.dao;

import git7s.flashcardai.DatabaseConnection;
import git7s.flashcardai.Result;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResultDAO {

    private Connection connection;

    public ResultDAO(){
        connection = DatabaseConnection.getInstance();
    }

    public void createTable(){
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS results ("
                            + "resultID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL, "
                            + "userID INTEGER NOT NULL, "
                            + "cardID INTEGER NOT NULL, "
                            + "at TIMESTAMP NOT NULL, "
                            + "correct BIT NOT NULL,"
                            + "FOREIGN KEY (userID) REFERENCES users(id), "
                            + "FOREIGN KEY (cardID) REFERENCES cards(id)"
                            + ")"
            );
        } catch (SQLException ex) {

            System.err.println(ex);
        }
    }

    public void insert(Result result){
        try {
            PreparedStatement insertResult = connection.prepareStatement(
            "INSERT INTO results (resultID, userID, cardID, at, correct) VALUES (?, ?, ?, ?, ?)"
            );
            insertResult.setInt(1, result.getResultID());
            insertResult.setInt(2, result.getUserID());
            insertResult.setInt(3, result.getCardID());
            insertResult.setTimestamp(4, result.getAt());
            insertResult.setBoolean(5, result.isCorrect());
            insertResult.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void delete(int resultID){
        try{
            PreparedStatement getStatement = connection.prepareStatement("DELETE * FROM results WHERE resultID = ?");
            getStatement.setInt(1, resultID);
            getStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
                Result result = new Result(resultID, userID, cardID, at, correct);
                results.add(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

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
                Result result = new Result(resultID, userID, cardID, at, correct);
                results.add(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public void close(){
        try {
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
}
