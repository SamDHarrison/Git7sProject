package git7s.flashcardai.service;

import git7s.flashcardai.AppDefaults;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class connects the Model objects to the SQL database connection (through the dao objects)
 */
public class DatabaseService {
    /**
     * Protected constructor - cannot be instantiated
     */
    protected DatabaseService() {
        try {
            instance = DriverManager.getConnection(url);
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx.getMessage());
        }
    }
    /**
     * Subclass which acts as session container
     */
    private static class Database {
        private static final DatabaseService INSTANCE = new DatabaseService();
    }
    /**
     * getInstance returns the static INSTANCE of DatabaseService
     * @return INSTANCE
     */
    public static DatabaseService getInstance() {
        return DatabaseService.Database.INSTANCE;
    }
    /**
     * This is the url to the database (constant)
     */
    private final String url = AppDefaults.DB_URL;
    /**
     * This is the Connection which is initially null. The connection is instantiated from getInstance()
     */
    private Connection instance = null;
    /**
     * This method generates a Connection to the database if able to and returns the Connection
     * @return Connection to be used by a DAO object
     */
    public Connection getConnection() {
        if (instance == null) {
            new DatabaseService();
        }
        return instance;
    }


}

