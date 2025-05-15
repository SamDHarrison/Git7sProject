package git7s.flashcardai.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class connects the Model objects to the SQL database connection (through the dao objects)
 */
public class DatabaseConnection {
    /**
     * This is the url to the database (constant)
     */
    private static final String url = "jdbc:sqlite:database.db";
    /**
     * This is the Connection which is initially null. The connection is instantiated from getInstance()
     */
    private static Connection instance = null;

    /**
     * Constructor for DatabaseConnection
     * Tries to assign a connection with the db
     */
    public DatabaseConnection() {

        try {
            instance = DriverManager.getConnection(url);
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx);
        }
    }
    /**
     * This method generates a Connection to the database if able to and returns the Connection
     * @return Connection to be used by a DAO object
     */
    public static Connection getInstance() {
        if (instance == null) {
            new DatabaseConnection();
        }
        return instance;
    }


}

