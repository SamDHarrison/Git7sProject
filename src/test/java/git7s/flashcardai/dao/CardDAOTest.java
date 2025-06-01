package git7s.flashcardai.dao;

import git7s.flashcardai.model.Card;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the CardDAO class using an in-memory SQLite database.
 * Covers insertion, retrieval by various criteria, updating, and deletion.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CardDAOTest {

    private static Connection connection;
    private CardDAO cardDAO;

    /**
     * Initializes the in-memory SQLite database and creates the `cards` table.
     */
    @BeforeAll
    public static void setupDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS cards (
                cardID INTEGER PRIMARY KEY AUTOINCREMENT,
                userID INTEGER NOT NULL,
                topic VARCHAR NOT NULL,
                subject VARCHAR NOT NULL,
                front TEXT NOT NULL,
                back TEXT NOT NULL
            );
        """);
    }

    /**
     * Initializes a fresh instance of CardDAO before each test.
     */
    @BeforeEach
    public void init() {
        cardDAO = new CardDAO(connection);
    }

    /**
     * Tests inserting a card and retrieving it by userID.
     */
    @Test
    @Order(1)
    public void testInsertAndGetByUserID() {
        Card card = new Card(1, "Math", "MATH101", "What is 2+2?", "4");
        cardDAO.insert(card);
        List<Card> cards = cardDAO.getByUserID(1);
        assertEquals(1, cards.size());
    }

    /**
     * Tests retrieving a card by subject.
     */
    @Test
    @Order(2)
    public void testGetBySubject() {
        List<Card> cards = cardDAO.getBySubject("Math");
        assertEquals(1, cards.size());
        assertEquals("What is 2+2?", cards.get(0).getFront());
    }

    /**
     * Tests retrieving a card by topic.
     */
    @Test
    @Order(3)
    public void testGetByTopic() {
        List<Card> cards = cardDAO.getByTopic("MATH101");
        assertEquals(1, cards.size());
    }

    /**
     * Tests retrieving a card by topic and userID.
     */
    @Test
    @Order(4)
    public void testGetByTopicAndUser() {
        List<Card> cards = cardDAO.getByTopicAndUser("MATH101", 1);
        assertEquals(1, cards.size());
    }

    /**
     * Tests retrieving all cards.
     */
    @Test
    @Order(5)
    public void testGetAll() {
        List<Card> allCards = cardDAO.getAll();
        assertFalse(allCards.isEmpty());
        assertEquals(1, allCards.size());
    }

    /**
     * Tests updating a card's back content.
     */
    @Test
    @Order(6)
    public void testUpdate() {
        Card card = cardDAO.getAll().get(0);
        card.setBack("4.0");
        cardDAO.update(card);
        Card updated = cardDAO.getByID(card.getCardID());
        assertEquals("4.0", updated.getBack());
    }

    /**
     * Tests deleting a card and verifying it's removed.
     */
    @Test
    @Order(7)
    public void testDelete() {
        Card card = cardDAO.getAll().get(0);
        cardDAO.delete(card.getCardID());
        assertTrue(cardDAO.getAll().isEmpty());
    }

    /**
     * Closes the database connection after all tests are complete.
     */
    @AfterAll
    public static void tearDown() throws SQLException {
        connection.close();
    }
}
