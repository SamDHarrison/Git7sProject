package git7s.flashcardai.dao;

import git7s.flashcardai.DatabaseConnection;
import git7s.flashcardai.User;
import javafx.scene.chart.PieChart;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private Connection connection;

    public UserDAO(){
        connection = DatabaseConnection.getInstance();
    }

    public void createTable(){
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS users ("
                            + "id INTEGER PRIMARY KEY NOT NULL, "
                            + "passwordHash VARCHAR NOT NULL, "
                            + "salt VARCHAR NOT NULL, "
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
            "INSERT INTO users (id, passwordHash, salt, firstName, lastName, admin) VALUES (?, ?, ?, ?, ?, ?)"
            );
            insertUser.setInt(1, user.getId());
            insertUser.setString(2, user.getPasswordHash());
            insertUser.setString(3, user.getSaltAsString());
            insertUser.setString(4, user.getFirstName());
            insertUser.setString(5, user.getLastName());
            insertUser.setBoolean(6, user.isAdmin());
            insertUser.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void update(int id, int newID, String newPassword, String newFirstName, String newLastName, boolean newAdmin){
        try{
            User existingUser= getById(id);
            if(existingUser==null) {
                return;
            }

            PreparedStatement updateStatement = connection.prepareStatement("UPDATE users SET id = ?, passwordHash = ?, salt = ?, firstname = ?, lastname = ?, admin = ? WHERE id = ?");
            updateStatement.setInt(7, id);
            updateStatement.setInt(1, newID);
            updateStatement.setString(2, existingUser.getPasswordHash());
            updateStatement.setString(3, existingUser.getSaltAsString());
            updateStatement.setString(4, newFirstName);
            updateStatement.setString(5, newLastName);
            updateStatement.setBoolean(6, newAdmin);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id){
        try{
            PreparedStatement getStatement = connection.prepareStatement("DELETE FROM users WHERE id = ?");
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
                String passwordHash = resultSet.getString("passwordHash");
                String salt = resultSet.getString("salt");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                boolean admin = resultSet.getBoolean("admin");
                User insertUser = new User(id, passwordHash, firstName, lastName, admin);
                insertUser.setPasswordHash(passwordHash);
                insertUser.setSaltFromString(salt);
                users.add(insertUser);
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
                String passwordHash = resultSet.getString("passwordHash");
                String salt = resultSet.getString("salt");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                boolean admin = resultSet.getBoolean("admin");
                User getUser = new User(id, passwordHash, firstName, lastName, admin);
                getUser.setPasswordHash(passwordHash);
                getUser.setSaltFromString(salt);
                return getUser;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User authenticate(int id, String password) {
        User user = getById(id);
        if (user != null && user.authenticate(password)) {
            return user;
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
