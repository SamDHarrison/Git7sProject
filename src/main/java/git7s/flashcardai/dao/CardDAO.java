package git7s.flashcardai.dao;

import git7s.flashcardai.DatabaseConnection;
import git7s.flashcardai.User;

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
                    "CREATE TABLE IF NOT EXISTS users ("
                            + "id INTEGER PRIMARY KEY NOT NULL, "
                            + "password VARCHAR NOT NULL, "
                            + "firstName VARCHAR NOT NULL, "
                            + "lastName VARCHAR NOT NULL, "
                            + "admin BIT NOT NULL, "
                            + "subjects VARCHAR"
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void insert(User user){
        try {
            PreparedStatement insertUser = connection.prepareStatement(
            "INSERT INTO users (id, password, firstname, lastname, admin) VALUES (?, ?, ?, ?, ?)"
            );
            insertUser.setInt(1, user.getId());
            insertUser.setString(2, user.getPassword());
            insertUser.setString(3, user.getFirstName());
            insertUser.setString(4, user.getLastName());
            insertUser.setBoolean(5, user.isAdmin());
            insertUser.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void update(int id, int newID, String newPassword, String newFirstName, String newLastName, boolean newAdmin){
        try{
            PreparedStatement updateStatement = connection.prepareStatement("UPDATE users SET id = ?, password = ?, firstname = ?, lastname = ?, admin = ? WHERE id = ?");
            updateStatement.setInt(6, id);
            updateStatement.setInt(1, newID);
            updateStatement.setString(2, newPassword);
            updateStatement.setString(3, newFirstName);
            updateStatement.setString(4, newLastName);
            updateStatement.setBoolean(5, newAdmin);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id){
        try{
            PreparedStatement getStatement = connection.prepareStatement("DELETE * FROM users WHERE id = ?");
            getStatement.setInt(1, id);
            getStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAll(){
        List<User> users = new ArrayList<>();
        try {
            Statement insertStatement = connection.createStatement();
            String query = "SELECT * FROM users";
            ResultSet resultSet = insertStatement.executeQuery(query);
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String password = resultSet.getString("password");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                boolean admin = resultSet.getBoolean("admin");
                User insertUser = new User(id, password, firstName, lastName, admin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public User getById(int id) {
        try{
            PreparedStatement getStatement = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
            getStatement.setInt(1, id);
            ResultSet resultSet = getStatement.executeQuery();
            if (resultSet.next()){
                String password = resultSet.getString("password");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                boolean admin = resultSet.getBoolean("admin");
                User getUser = new User(id, password, firstName, lastName, admin);
                return getUser;
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
