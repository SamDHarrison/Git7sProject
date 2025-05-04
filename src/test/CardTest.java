package git7s.flashcardai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    private Card geographyCard;
    private Card mathCard;
    private Card compsciCard;
    private Card chemistryCard;
    private Card politicsCard;
    private Card engineeringCard;

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


    ///Test for the Flip functionality of the card
    @Test
    public void testFlipReturnsBackText() {
        assertEquals("Brisbane", geographyCard.flip());
        assertEquals("15", mathCard.flip());
        assertEquals("Random Access Memory", compsciCard.flip());
        assertEquals("Na", chemistryCard.flip());
        assertEquals("Anthony Albanese", politicsCard.flip());
        assertEquals("Computer-Aided Design", engineeringCard.flip());
    }
    @Test
    public void testGettersForCardID() {
        assertEquals(1, geographyCard.getCardID());
        assertEquals(2, mathCard.getCardID());
        assertEquals(3, compsciCard.getCardID());
        assertEquals(4, chemistryCard.getCardID());
        assertEquals(5, politicsCard.getCardID());
        assertEquals(6, engineeringCard.getCardID());
    }

    @Test
    public void testGettersForUserID() {
        assertEquals(117249823, geographyCard.getUserID());
        assertEquals(117249824, mathCard.getUserID());
        assertEquals(117249825, compsciCard.getUserID());
        assertEquals(117249826, chemistryCard.getUserID());
        assertEquals(117249827, politicsCard.getUserID());
        assertEquals(117249828, engineeringCard.getUserID());
    }

    @Test
    public void testGettersForTopic() {
        assertEquals("Australian Capitals", geographyCard.getTopic());
        assertEquals("Basic Math", mathCard.getTopic());
        assertEquals("Computer Science Basics", compsciCard.getTopic());
        assertEquals("Periodic Table", chemistryCard.getTopic());
        assertEquals("Politics", politicsCard.getTopic());
        assertEquals("Engineering", engineeringCard.getTopic());
    }

    @Test
    public void testGettersForSubject() {
        assertEquals("GEO301", geographyCard.getSubject());
        assertEquals("MATH301", mathCard.getSubject());
        assertEquals("CAB301", compsciCard.getSubject());
        assertEquals("CHEM301", chemistryCard.getSubject());
        assertEquals("POL301", politicsCard.getSubject());
        assertEquals("ENGR301", engineeringCard.getSubject());
    }

    @Test
    public void testGettersForFront() {
        assertEquals("Capital of Queensland?", geographyCard.getFront());
        assertEquals("What is 9 + 6?", mathCard.getFront());
        assertEquals("What does RAM stand for?", compsciCard.getFront());
        assertEquals("What is the symbol for Sodium?", chemistryCard.getFront());
        assertEquals("Who is the current Prime Minister of Australia?", politicsCard.getFront());
        assertEquals("What does CAD stand for?", engineeringCard.getFront());
    }

    @Test
    public void testGettersForBack() {
        assertEquals("Brisbane", geographyCard.getBack());
        assertEquals("15", mathCard.getBack());
        assertEquals("Random Access Memory", compsciCard.getBack());
        assertEquals("Na", chemistryCard.getBack());
        assertEquals("Anthony Albanese", politicsCard.getBack());
        assertEquals("Computer-Aided Design", engineeringCard.getBack());
    }

    @Test
    public void testSetterCardID() {
        geographyCard.setCardID(10);
        assertEquals(10, geographyCard.getCardID());
    }

    @Test
    public void testSetterUserID() {
        mathCard.setUserID(999999);
        assertEquals(999999, mathCard.getUserID());
    }

    @Test
    public void testSetterTopic() {
        compsciCard.setTopic("Advanced Computing");
        assertEquals("Advanced Computing", compsciCard.getTopic());
    }

    @Test
    public void testSetterSubject() {
        chemistryCard.setSubject("ADV_CHEM401");
        assertEquals("ADV_CHEM401", chemistryCard.getSubject());
    }

    @Test
    public void testSetterFront() {
        politicsCard.setFront("Who is the leader of the opposition?");
        assertEquals("Who is the leader of the opposition?", politicsCard.getFront());
    }

    @Test
    public void testSetterBack() {
        engineeringCard.setBack("Computer-Assisted Design");
        assertEquals("Computer-Assisted Design", engineeringCard.getBack());
    }
}





