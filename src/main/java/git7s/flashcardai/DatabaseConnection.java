package git7s.flashcardai;

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
     * This method generates a Connection to the database if able to and returns the Connection
     * @return Connection to be used by a DAO object
     */
    public static Connection getInstance() {
        try {
            Connection connection = DriverManager.getConnection(url);
            return connection;
        } catch (SQLException e) {
            System.err.print(e);
        }
        return null;
    }
}
