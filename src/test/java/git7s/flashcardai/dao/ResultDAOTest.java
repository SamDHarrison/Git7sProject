package git7s.flashcardai.dao;

import git7s.flashcardai.model.Result;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ResultDAO class using an in-memory SQLite database.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ResultDAOTest {

    private static Connection connection;
    private ResultDAO resultDAO;
    private static Timestamp sampleTimestamp;

    /**
     * Sets up the in-memory database and creates required tables.
     * Runs once before all tests.
     */
    @BeforeAll
    public static void setupDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        sampleTimestamp = new Timestamp(System.currentTimeMillis());

        Statement stmt = connection.createStatement();
        stmt.executeUpdate("CREATE TABLE users (id INTEGER PRIMARY KEY NOT NULL)");
        stmt.executeUpdate("CREATE TABLE cards (id INTEGER PRIMARY KEY NOT NULL)");
        stmt.executeUpdate("""
            CREATE TABLE results (
                resultID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                userID INTEGER NOT NULL,
                cardID INTEGER NOT NULL,
                at TIMESTAMP NOT NULL,
                correct BIT NOT NULL,
                subject VARCHAR NOT NULL,
                topic VARCHAR NOT NULL,
                FOREIGN KEY (userID) REFERENCES users(id),
                FOREIGN KEY (cardID) REFERENCES cards(id)
            );
        """);

        // Insert dummy user and card
        stmt.executeUpdate("INSERT INTO users (id) VALUES (1)");
        stmt.executeUpdate("INSERT INTO cards (id) VALUES (101)");
    }

    /**
     * Creates a fresh DAO instance before each test.
     */
    @BeforeEach
    public void init() {
        resultDAO = new ResultDAO(connection);
    }

    /**
     * Tests inserting a result and retrieving it by user ID.
     */
    @Test
    @Order(1)
    public void testInsertAndGetByUserID() {
        Result result = new Result(1, 101, sampleTimestamp, true, "Math", "Algebra");
        resultDAO.insert(result);

        List<Result> results = resultDAO.getByUserID(1);
        assertEquals(1, results.size());
        assertEquals("Math", results.get(0).getSubject());
    }

    /**
     * Tests retrieving a result by card ID.
     */
    @Test
    @Order(2)
    public void testGetByCardID() {
        List<Result> results = resultDAO.getByCardID(101);
        assertFalse(results.isEmpty());
        assertEquals("Algebra", results.get(0).getTopic());
    }

    /**
     * Tests retrieving all results in the database.
     */
    @Test
    @Order(3)
    public void testGetAll() {
        List<Result> results = resultDAO.getAll();
        assertEquals(1, results.size());
    }

    /**
     * Tests deleting a result and verifying it no longer exists.
     */
    @Test
    @Order(4)
    public void testDelete() {
        List<Result> resultsBefore = resultDAO.getAll();
        assertFalse(resultsBefore.isEmpty());
        int resultIDToDelete = resultsBefore.get(0).getResultID();

        resultDAO.delete(resultIDToDelete);

        List<Result> resultsAfter = resultDAO.getAll();
        assertTrue(resultsAfter.isEmpty());
    }

    /**
     * Closes the database connection after all tests.
     */
    @AfterAll
    public static void tearDown() throws SQLException {
        connection.close();
    }
}
