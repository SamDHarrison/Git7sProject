package git7s.flashcardai;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {


    private static final String url = "jdbc:sqlite:database.db";


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
