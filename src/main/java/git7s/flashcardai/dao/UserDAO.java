package git7s.flashcardai.dao;

import git7s.flashcardai.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class UserDAO {
    /**
     * The connection used for connecting to the db
     */
    private Connection connection;
    /**
     * The Constructor which gets the static connection from the main class
     */
    public UserDAO() {
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
/**
     * Inserts a user to the db
     * @param user New User for insertion
     */
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

    /**
     * Update a specific user
     * @param oldUserID The existing ID
     * @param user The updated user

     */
    public void update(int oldUserID, User user){
        try{
            PreparedStatement updateStatement = connection.prepareStatement("UPDATE users SET id = ?, passwordHash = ?, salt = ?, firstname = ?, lastname = ?, admin = ? WHERE id = ?");
            updateStatement.setInt(7, oldUserID);
            updateStatement.setInt(1, user.getId());
            updateStatement.setString(2, user.getPasswordHash());
            updateStatement.setString(3, user.getSaltAsString());
            updateStatement.setString(4, user.getFirstName());
            updateStatement.setString(5, user.getLastName());
            updateStatement.setBoolean(6, false);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a specific user from the db
     * @param id Specified user
     */
    public void delete(int id){
        
        try{
            PreparedStatement getStatement = connection.prepareStatement("DELETE FROM users WHERE id = ?");
            getStatement.setInt(1, id);
            getStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    /**
     * Gets a list of all the users in the DB
     * @return List of users
     */
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

    /**
     * Gets a user by a specified ID
     * @param id The specified user ID
     * @return The User Specified, if exists
     */
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
}
