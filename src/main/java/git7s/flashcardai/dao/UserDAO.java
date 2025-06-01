package git7s.flashcardai.dao;

import git7s.flashcardai.model.User;
import git7s.flashcardai.service.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class UserDAO  implements IDAO<User> {
    /**
     * The connection used for connecting to the db
     */
    private final Connection connection;
    /**
     * The Constructor which gets the static connection from the main class
     */
    public UserDAO() {
        this.connection = DatabaseService.getInstance().getConnection();
        createTable();
    }
    /**
     * The Constructor overload for testing - intended for unit testing with an in-memory database.
     */
    public UserDAO(Connection connection) {
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
                    "CREATE TABLE IF NOT EXISTS users ("
                            + "id INTEGER PRIMARY KEY NOT NULL, "
                            + "passwordHash VARCHAR NOT NULL, "
                            + "salt VARCHAR NOT NULL, "
                            + "firstName VARCHAR NOT NULL, "
                            + "lastName VARCHAR NOT NULL, "
                            + "admin BIT NOT NULL, "
                            + "prefcol VARCHAR"
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
/**
     * Inserts a user to the db
     * @param user New User for insertion
     */
    public void insert(User user){
        
        try {
            PreparedStatement insertUser = connection.prepareStatement(
                    "INSERT INTO users (id, passwordHash, salt, firstName, lastName, admin, prefcol) VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            insertUser.setInt(1, user.getId());
            insertUser.setString(2, user.getPasswordHash());
            insertUser.setString(3, user.getSaltAsString());
            insertUser.setString(4, user.getFirstName());
            insertUser.setString(5, user.getLastName());
            insertUser.setBoolean(6, user.isAdmin());
            insertUser.setString(7, user.getPrefColour());
            insertUser.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        
    }
    /**
     * Update a specific user
     * @param user The updated user

     */
    @Override
    public void update(User user) {
        try{
            PreparedStatement updateStatement = connection.prepareStatement("UPDATE users SET passwordHash = ?, salt = ?, firstname = ?, lastname = ?, admin = ?, prefcol = ? WHERE id = ?");
            updateStatement.setInt(7, user.getId());
            updateStatement.setString(1, user.getPasswordHash());
            updateStatement.setString(2, user.getSaltAsString());
            updateStatement.setString(3, user.getFirstName());
            updateStatement.setString(4, user.getLastName());
            updateStatement.setBoolean(5, user.isAdmin());
            updateStatement.setString(6, user.getPrefColour());
            updateStatement.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
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
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        
    }

    /**
     * Gets a user by a specified ID
     * @param ID The specified user ID
     * @return The User Specified, if exists
     */
    @Override
    public User getByID(int ID) {
        try{
            PreparedStatement getStatement = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
            getStatement.setInt(1, ID);
            ResultSet resultSet = getStatement.executeQuery();
            if (resultSet.next()){
                String passwordHash = resultSet.getString("passwordHash");
                String salt = resultSet.getString("salt");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                boolean admin = resultSet.getBoolean("admin");
                String prefCol = resultSet.getString("prefcol");
                User getUser = new User(ID, passwordHash, firstName, lastName, admin, prefCol);
                getUser.setPasswordHash(passwordHash);
                getUser.setSaltFromString(salt);

                return getUser;
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
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
                String prefCol = resultSet.getString("prefcol");
                User insertUser = new User(id, passwordHash, firstName, lastName, admin, prefCol);
                insertUser.setPasswordHash(passwordHash);
                insertUser.setSaltFromString(salt);
                users.add(insertUser);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return users;
    }
}

