package git7s.flashcardai.dao;

import git7s.flashcardai.DatabaseConnection;
import git7s.flashcardai.User;
import javafx.scene.chart.PieChart;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * The DAO object for interacting with the User Table over the specified connection
 */
public class UserDAO {
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
        close();
    }
    /**
     * Inserts a user to the db
     * @param user New User for insertion
     */
    public void insert(User user){
        open();
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
        close();
    }

    /**
     * Update a specific user
     * @param id The existing ID
     * @param newID The new ID
     * @param newPassword The new password
     * @param newFirstName The new firstname
     * @param newLastName The new lastname
     * @param newAdmin The admin level
     */
    public void update(int id, int newID, String newPassword, String newFirstName, String newLastName, boolean newAdmin){
        open();
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
        close();
    }

    /**
     * Deletes a specific user from the db
     * @param id Specified user
     */
    public void delete(int id){
        open();
        try{
            PreparedStatement getStatement = connection.prepareStatement("DELETE FROM users WHERE id = ?");
            getStatement.setInt(1, id);
            getStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
    }

    /**
     * Gets a list of all the users in the DB
     * @return List of users
     */
    public List<User> getAll(){
        open();
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
        close();
        return users;
    }

    /**
     * Gets a user by a specified ID
     * @param id The specified user ID
     * @return The User Specified, if exists
     */
    public User getById(int id) {
        open();
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
                close();
                return getUser;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return null;
    }

    /**
     * Authenticates a user by their ID
     * @param id User ID
     * @param password User Password (Hashed)
     * @return The User - if authenticated
     */
    public User authenticate(int id, String password) {
        User user = getById(id);
        if (user != null && user.authenticate(password)) {
            return user;
        }
        return null;
    }

    /**
     * Open the DB
     */
    public void open(){
        connection = DatabaseConnection.getInstance();
    }

    /**
     * Close the DB
     */
    public void close(){
        try {
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
}
