package git7s.flashcardai;

import git7s.flashcardai.dao.CardDAO;
import git7s.flashcardai.llm.FlashCardGenerateManager;
import git7s.flashcardai.model.Card;
import git7s.flashcardai.model.CardManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the FlashCardGenerateManager class.
 */
public class FlashCardGenerateManagerTest {

    private final String mockResponse = "(Q1:A1)(Q2:A2)(Q3:A3)";

    @Mock
    private CardManager mockCardManager;

    @InjectMocks
    private FlashCardGenerateManager flashCardGenerateManager;

    /**
     * Initializes mocks and sets up the test object before each test.
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        flashCardGenerateManager = new FlashCardGenerateManager(mockResponse);
        flashCardGenerateManager.setCardManager(mockCardManager); // Inject the mock manager
    }

    /**
     * Tests that the generateFlashcards() method correctly parses the input.
     */
    @Test
    public void testGenerateFlashcardsParsesCorrectly() {
        Map<String, String> cards = flashCardGenerateManager.getNewFlashcards();

        assertEquals(3, cards.size());
        assertEquals("A1", cards.get("Q1"));
        assertEquals("A2", cards.get("Q2"));
        assertTrue(cards.containsKey("Q3"));
    }

    /**
     * Tests that only the specified number of flashcards are added.
     */
    @Test
    public void testAddFlashcardsInsertsCorrectQuantity() {
        flashCardGenerateManager.setCardManager(mockCardManager);  // Ensure mock is used
        flashCardGenerateManager.addFlashCards("Math", "Algebra", 2);

        verify(mockCardManager, times(2)).addCard(any(Card.class));
    }
}
