package git7s.flashcardai;

import git7s.flashcardai.model.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Card} class.
 * Covers the card flipping logic, all getter and setter methods.
 */
public class CardTest {

    private Card geographyCard;
    private Card mathCard;
    private Card compsciCard;
    private Card chemistryCard;
    private Card politicsCard;
    private Card engineeringCard;


    /**
     * Sets up multiple card objects for testing, each representing a different subject and topic.
     * This method is run before each test.
     */

    @BeforeEach
    public void setUp() {
        geographyCard = new Card(117249823, "Australian Capitals", "GEO301", "Capital of Queensland?", "Brisbane");
        geographyCard.setCardID(1);

        mathCard = new Card(117249824, "Basic Math", "MATH301", "What is 9 + 6?", "15");
        mathCard.setCardID(2);

        compsciCard = new Card(117249825, "Computer Science Basics", "CAB301", "What does RAM stand for?", "Random Access Memory");
        compsciCard.setCardID(3);

        chemistryCard = new Card(117249826, "Periodic Table", "CHEM301", "What is the symbol for Sodium?", "Na");
        chemistryCard.setCardID(4);

        politicsCard = new Card(117249827, "Politics", "POL301", "Who is the current Prime Minister of Australia?", "Anthony Albanese");
        politicsCard.setCardID(5);

        engineeringCard = new Card(117249828, "Engineering", "ENGR301", "What does CAD stand for?", "Computer-Aided Design");
        engineeringCard.setCardID(6);
    }


    /**
     * Tests that the flip() method returns the back text of each card.
     */
    @Test
    public void testFlipReturnsBackText() {
        assertEquals("Brisbane", geographyCard.flip());
        assertEquals("15", mathCard.flip());
        assertEquals("Random Access Memory", compsciCard.flip());
        assertEquals("Na", chemistryCard.flip());
        assertEquals("Anthony Albanese", politicsCard.flip());
        assertEquals("Computer-Aided Design", engineeringCard.flip());
    }

    /**
     * Tests that getCardID() returns the correct ID for each card.
     */
    @Test
    public void testGettersForCardID() {
        assertEquals(1, geographyCard.getCardID());
        assertEquals(2, mathCard.getCardID());
        assertEquals(3, compsciCard.getCardID());
        assertEquals(4, chemistryCard.getCardID());
        assertEquals(5, politicsCard.getCardID());
        assertEquals(6, engineeringCard.getCardID());
    }

    /**
     * Tests that getUserID() returns the correct user ID for each card.
     */
    @Test
    public void testGettersForUserID() {
        assertEquals(117249823, geographyCard.getUserID());
        assertEquals(117249824, mathCard.getUserID());
        assertEquals(117249825, compsciCard.getUserID());
        assertEquals(117249826, chemistryCard.getUserID());
        assertEquals(117249827, politicsCard.getUserID());
        assertEquals(117249828, engineeringCard.getUserID());
    }

    /**
     * Tests that getSubject() returns the correct subject string for each card.
     */
    @Test
    public void testGettersForSubject() {
        assertEquals("Australian Capitals", geographyCard.getSubject());
        assertEquals("Basic Math", mathCard.getSubject());
        assertEquals("Computer Science Basics", compsciCard.getSubject());
        assertEquals("Periodic Table", chemistryCard.getSubject());
        assertEquals("Politics", politicsCard.getSubject());
        assertEquals("Engineering", engineeringCard.getSubject());
    }

    /**
     * Tests that getTopic() returns the correct topic string for each card.
     */
    @Test
    public void testGettersForTopic() {
        assertEquals("GEO301", geographyCard.getTopic());
        assertEquals("MATH301", mathCard.getTopic());
        assertEquals("CAB301", compsciCard.getTopic());
        assertEquals("CHEM301", chemistryCard.getTopic());
        assertEquals("POL301", politicsCard.getTopic());
        assertEquals("ENGR301", engineeringCard.getTopic());
    }

    /**
     * Tests that getFront() returns the front text (question) for each card.
     */
    @Test
    public void testGettersForFront() {
        assertEquals("Capital of Queensland?", geographyCard.getFront());
        assertEquals("What is 9 + 6?", mathCard.getFront());
        assertEquals("What does RAM stand for?", compsciCard.getFront());
        assertEquals("What is the symbol for Sodium?", chemistryCard.getFront());
        assertEquals("Who is the current Prime Minister of Australia?", politicsCard.getFront());
        assertEquals("What does CAD stand for?", engineeringCard.getFront());
    }

    /**
     * Tests that getBack() returns the back text (answer) for each card.
     */
    @Test
    public void testGettersForBack() {
        assertEquals("Brisbane", geographyCard.getBack());
        assertEquals("15", mathCard.getBack());
        assertEquals("Random Access Memory", compsciCard.getBack());
        assertEquals("Na", chemistryCard.getBack());
        assertEquals("Anthony Albanese", politicsCard.getBack());
        assertEquals("Computer-Aided Design", engineeringCard.getBack());
    }

    /**
     * Tests that setCardID() correctly updates the card ID.
     */
    @Test
    public void testSetterCardID() {
        geographyCard.setCardID(10);
        assertEquals(10, geographyCard.getCardID());
    }

    /**
     * Tests that setUserID() correctly updates the user ID.
     */
    @Test
    public void testSetterUserID() {
        mathCard.setUserID(999999);
        assertEquals(999999, mathCard.getUserID());
    }

    /**
     * Tests that setTopic() correctly updates the topic string.
     */
    @Test
    public void testSetterTopic() {
        compsciCard.setTopic("Advanced Computing");
        assertEquals("Advanced Computing", compsciCard.getTopic());
    }

    /**
     * Tests that setSubject() correctly updates the subject string.
     */
    @Test
    public void testSetterSubject() {
        chemistryCard.setSubject("ADV_CHEM401");
        assertEquals("ADV_CHEM401", chemistryCard.getSubject());
    }

    /**
     * Tests that setFront() correctly updates the front text.
     */
    @Test
    public void testSetterFront() {
        politicsCard.setFront("Who is the leader of the opposition?");
        assertEquals("Who is the leader of the opposition?", politicsCard.getFront());
    }

    /**
     * Tests that setBack() correctly updates the back text.
     */
    @Test
    public void testSetterBack() {
        engineeringCard.setBack("Computer-Assisted Design");
        assertEquals("Computer-Assisted Design", engineeringCard.getBack());
    }

    /**
     * Testing of edge cases: Null Value
     */

    @Test
    public void testSettersWithNullValues() {
        geographyCard.setSubject(null);
        assertNull(geographyCard.getSubject());

        geographyCard.setFront(null);
        assertNull(geographyCard.getFront());
    }

    /**
     * Testing of edge cases: Empty Value
     */
    @Test
    public void testSettersWithEmptyStrings() {
        mathCard.setSubject("");
        assertEquals("", mathCard.getSubject());

        mathCard.setBack("");
        assertEquals("", mathCard.getBack());
    }

    /**
     * Testing of edge cases: Boundary Value
     */
    @Test
    public void testSettersWithBoundaryValues() {
        compsciCard.setCardID(0);
        assertEquals(0, compsciCard.getCardID());

        compsciCard.setCardID(-1);
        assertEquals(-1, compsciCard.getCardID());
    }
}





