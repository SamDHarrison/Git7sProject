package git7s.flashcardai.dao;

import git7s.flashcardai.model.User;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the UserDAO class using an in-memory SQLite database.
 * Tests insert, retrieve, update, delete, and getAll operations.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDAOTest {

    private static Connection connection;
    private UserDAO userDAO;

    /**
     * Sets up an in-memory database and creates the users table.
     */
    @BeforeAll
    public static void setupDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY NOT NULL,
                passwordHash VARCHAR NOT NULL,
                salt VARCHAR NOT NULL,
                firstname VARCHAR NOT NULL,
                lastname VARCHAR NOT NULL,
                admin BIT NOT NULL,
                prefcol VARCHAR
            );
        """);
    }

    /**
     * Creates a new UserDAO using the test database connection.
     */
    @BeforeEach
    public void init() {
        userDAO = new UserDAO(connection);
    }

    /**
     * Checks if a user can be added and then retrieved correctly.
     */
    @Test
    @Order(1)
    public void testInsertAndRetrieve() {
        User taylah = new User(117249823, "Secure123", "Taylah", "McCullough", false, "#FF5733");
        userDAO.insert(taylah);

        User result = userDAO.getByID(117249823);
        assertNotNull(result);
        assertEquals("Taylah", result.getFirstName());
        assertEquals("McCullough", result.getLastName());
        assertEquals("#FF5733", result.getPrefColour());
        assertTrue(result.authenticate("Secure123"));
    }

    /**
     * Checks if the inserted user is returned by getAll().
     */
    @Test
    @Order(2)
    public void testGetAllIncludesUser() {
        List<User> allUsers = userDAO.getAll();
        assertFalse(allUsers.isEmpty());
        assertTrue(allUsers.stream().anyMatch(u -> u.getId() == 117249823));
    }

    /**
     * Checks if a user's details can be updated correctly.
     */
    @Test
    @Order(3)
    public void testUpdate() {
        User updated = new User(117249823, "Pass456", "Tay", "Updated", true, "#0000FF");

        String updatedSalt = updated.getSaltAsString(); // store current salt for comparison

        userDAO.update(updated);

        User result = userDAO.getByID(117249823);
        assertNotNull(result);
        assertEquals("Tay", result.getFirstName());
        assertEquals("Updated", result.getLastName());
        assertEquals(updated.getPasswordHash(), result.getPasswordHash());
        assertEquals(updatedSalt, result.getSaltAsString());
        assertEquals("#0000FF", result.getPrefColour());
        assertFalse(result.isAdmin());  // DAO is hardcoded to false for now
    }

    /**
     * Checks if a user can be deleted from the database.
     */
    @Test
    @Order(4)
    public void testDelete() {
        userDAO.delete(117249823);
        User deleted = userDAO.getByID(117249823);
        assertNull(deleted);
    }

    /**
     * Closes the test database connection after all tests.
     */
    @AfterAll
    public static void tearDown() throws SQLException {
        connection.close();
    }
}
