package git7s.flashcardai;

import git7s.flashcardai.model.Card;
import git7s.flashcardai.model.CardManager;
import org.junit.jupiter.api.*;
import git7s.flashcardai.dao.CardDAO;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class CardManagerTest {

    private CardManager cardManager;

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
        CardDAO cardDAO = new CardDAO(); // ✅ instantiate required DAO
        cardManager = new CardManager(cardDAO); // ✅ pass to constructor

        geographyCard = new Card(117249823, "Australian Capitals", "GEO301", "Capital of Queensland?", "Brisbane");
        mathCard = new Card(117249824, "Basic Math", "MATH301", "What is 9 + 6?", "15");
        compsciCard = new Card(117249825, "Computer Science Basics", "CAB301", "What does RAM stand for?", "Random Access Memory");
        chemistryCard = new Card(117249826, "Periodic Table", "CHEM301", "What is the symbol for Sodium?", "Na");
        politicsCard = new Card(117249827, "Politics", "POL301", "Who is the current Prime Minister of Australia?", "Anthony Albanese");
        engineeringCard = new Card(117249828, "Engineering", "ENGR301", "What does CAD stand for?", "Computer-Aided Design");
    }
    @Test
    @Order(1)
    public void testAddCards() {
        cardManager.addCard(geographyCard);
        cardManager.addCard(mathCard);
        cardManager.addCard(compsciCard);
        cardManager.addCard(chemistryCard);
        cardManager.addCard(politicsCard);
        cardManager.addCard(engineeringCard);

        List<Card> result = cardManager.searchCardsBySubject("Computer Science Basics");
        assertFalse(result.isEmpty());
        assertEquals("CAB301", result.get(0).getTopic());
    }

    @Test
    @Order(2)
    public void testSearchCardsByUserID() {
        List<Card> cards = cardManager.searchByUserID(117249825); // compsciCard
        assertTrue(cards.stream().anyMatch(c -> c.getFront().contains("RAM")));
    }

    @Test
    @Order(3)
    public void testUpdateCard() {
        // Ensure the card exists
        cardManager.addCard(geographyCard);

        List<Card> cards = cardManager.searchByUserID(117249823);
        assertFalse(cards.isEmpty()); // ✅ should now pass
        Card card = cards.get(0);

        card.setBack("Correct Answer: Brisbane");
        cardManager.updateCard(card);

        Card updated = cardManager.searchByUserID(117249823).get(0);
        assertEquals("Correct Answer: Brisbane", updated.getBack());
    }


    @Test
    @Order(4)
    public void testDeleteCard() {
        List<Card> cardsBefore = cardManager.searchByUserID(117249824);

        if (cardsBefore.isEmpty()) {
            cardManager.addCard(mathCard);
            cardsBefore = cardManager.searchByUserID(117249824);
        }

        assertFalse(cardsBefore.isEmpty());
        int cardIDToDelete = cardsBefore.get(0).getCardID();
        int initialCount = cardsBefore.size();

        cardManager.delete(cardIDToDelete);

        List<Card> cardsAfter = cardManager.searchByUserID(117249824);
        assertEquals(initialCount - 1, cardsAfter.size());
        assertFalse(cardsAfter.stream().anyMatch(card -> card.getCardID() == cardIDToDelete));
    }

}