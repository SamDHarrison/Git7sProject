package git7s.flashcardai.dao;

import git7s.flashcardai.DatabaseConnection;
import git7s.flashcardai.User;

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
                            + "password VARCHAR NOT NULL, "
                            + "firstName VARCHAR NOT NULL, "
                            + "lastName VARCHAR NOT NULL, "
                            + "admin BIT NOT NULL, "
                            + "friends VARCHAR"
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void insert(User user){
        try {
            PreparedStatement insertUser = connection.prepareStatement(
            "INSERT INTO users (id, password, firstname, lastname, admin, friends) VALUES (?, ?, ?, ?, ?, ?)"
            );
            insertUser.setInt(1, user.getId());
            insertUser.setString(2, user.getPassword());
            insertUser.setString(3, user.getFirstName());
            insertUser.setString(4, user.getLastName());
            insertUser.setBoolean(5, user.isAdmin());
            insertUser.setString(6, user.getFriends());
            insertUser.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void update(){

    }

    public void delete(int id){

    }

    public List<User> getAll(){
        List<User> users = new ArrayList<>();

        return users;
    }

    public User getById(int id) {

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
