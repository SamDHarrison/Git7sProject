package git7s.flashcardai.model;

import git7s.flashcardai.dao.ResultDAO;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ResultManager using an in-memory SQLite database.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ResultManagerTest {

    private static Connection connection;
    private static ResultDAO resultDAO;
    private ResultManager resultManager; // ✅ made non-static
    private static Timestamp now;

    /**
     * Set up the in-memory test database before any tests run.
     */
    @BeforeAll
    public static void setupDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        resultDAO = new ResultDAO(connection);
        now = new Timestamp(System.currentTimeMillis());

        Statement stmt = connection.createStatement();

        // Create tables with IF NOT EXISTS to avoid duplicates
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY NOT NULL)");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS cards (id INTEGER PRIMARY KEY NOT NULL)");
        stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS results (
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

        // Insert test data
        stmt.executeUpdate("INSERT INTO users (id) VALUES (1234)");
        stmt.executeUpdate("INSERT INTO cards (id) VALUES (5678)");
    }

    /**
     * Set up a new ResultManager instance before each test.
     */
    @BeforeEach
    public void setup() {
        resultManager = new ResultManager(resultDAO);
    }

    /**
     * Test adding a result and checking it exists in the database.
     */
    @Test
    @Order(1)
    public void testAddAndGetAllResults() {
        Result result = new Result(1234, 5678, now, true, "Math", "Algebra");
        resultManager.addResult(result);
        List<Result> results = resultManager.getAll();
        assertEquals(1, results.size());
    }

    /**
     * Test searching by subject and topic.
     */
    @Test
    @Order(2)
    public void testSearchBySubjectAndTopic() {
        List<Result> bySubject = resultManager.searchResultsBySubject("Math");
        List<Result> byTopic = resultManager.searchResultsByTopic("Algebra");
        assertFalse(bySubject.isEmpty());
        assertFalse(byTopic.isEmpty());
    }

    /**
     * Test retrieving results by user ID.
     */
    @Test
    @Order(3)
    public void testGetByUserID() {
        List<Result> userResults = resultManager.getByUserID(1234);
        assertEquals(1, userResults.size());
    }

    /**
     * Test retrieving results by card ID.
     */
    @Test
    @Order(4)
    public void testGetByCardID() {
        List<Result> cardResults = resultManager.getByCardID(5678);
        assertEquals(1, cardResults.size());
    }

    /**
     * Test retrieving results by time range.
     */
    @Test
    @Order(5)
    public void testGetByTimeFrame() {
        Timestamp start = new Timestamp(now.getTime() - 1000);
        Timestamp end = new Timestamp(now.getTime() + 1000);
        List<Result> timeResults = resultManager.getByTimeFrame(start, end);
        assertEquals(1, timeResults.size());
    }

    /**
     * Test deleting a result by ID.
     */
    @Test
    @Order(6)
    public void testDeleteResult() {
        List<Result> allResults = resultManager.getAll();
        int resultID = allResults.get(0).getResultID();
        resultManager.delete(resultID);
        assertTrue(resultManager.getAll().isEmpty());
    }

    /**
     * Close the in-memory database after all tests.
     */
    @AfterAll
    public static void tearDown() throws SQLException {
        connection.close();
    }
}
