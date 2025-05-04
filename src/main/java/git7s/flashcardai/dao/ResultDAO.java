package git7s.flashcardai.dao;

import git7s.flashcardai.DatabaseConnection;
import git7s.flashcardai.Result;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResultDAO {

    private Connection connection;

    public void createTable(){
        open();
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS results ("
                            + "resultID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
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
        close();
    }

    public void insert(Result result){
        open();
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
        close();
    }

    public void delete(int resultID){
        open();
        try{
            PreparedStatement getStatement = connection.prepareStatement("DELETE * FROM results WHERE resultID = ?");
            getStatement.setInt(1, resultID);
            getStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
    }

    public List<Result> getByCardID(int cardIDQuery){
        open();
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
                Result result = new Result(userID, cardID, at, correct);
                result.setResultID(resultID);
                results.add(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        close();
        return results;
    }

    public List<Result> getAll(){
        open();
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
                Result result = new Result(userID, cardID, at, correct);
                result.setResultID(resultID);
                results.add(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        close();
        return results;
    }

    public List<Result> getByUserID(int userIDQuery){
        open();
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
                Result result = new Result(userID, cardID, at, correct);
                result.setResultID(resultID);
                results.add(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        close();
        return results;

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
