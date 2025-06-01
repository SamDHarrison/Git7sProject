package git7s.flashcardai.model;

import java.sql.Timestamp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Result model class.
 */
public class ResultTest {

    private Result correctResult1;
    private Result correctResult2;
    private Result incorrectResult1;
    private Result incorrectResult2;

    /**
     * Sets up test data before each test.
     */
    @BeforeEach
    public void setUp() {
        correctResult1 = new Result(117249823, 1, Timestamp.valueOf("2025-04-28 10:15:30"), true, "GEO301", "Australian Capitals");
        correctResult1.setResultID(1);
        correctResult2 = new Result(117249824, 2, Timestamp.valueOf("2025-04-28 14:45:00"), true, "MATH301", "Basic Math");
        correctResult2.setResultID(2);
        incorrectResult1 = new Result(117249825, 3, Timestamp.valueOf("2025-04-28 17:20:45"), false, "CHEM301", "Periodic Table");
        incorrectResult1.setResultID(3);
        incorrectResult2 = new Result(117249826, 4, Timestamp.valueOf("2025-04-28 23:59:59"), false, "ENGR301", "CAD");
        incorrectResult2.setResultID(4);
    }

    /**
     * Tests that isCorrect() returns true for correct results.
     */
    @Test
    public void testIsCorrectTrue() {
        assertTrue(correctResult1.isCorrect());
        assertTrue(correctResult2.isCorrect());
    }

    /**
     * Tests that isCorrect() returns false for incorrect results.
     */
    @Test
    public void testIsCorrectFalse() {
        assertFalse(incorrectResult1.isCorrect());
        assertFalse(incorrectResult2.isCorrect());
    }

    /**
     * Tests getResultID() returns the correct ID.
     */
    @Test
    public void testGettersForResultID() {
        assertEquals(1, correctResult1.getResultID());
        assertEquals(2, correctResult2.getResultID());
        assertEquals(3, incorrectResult1.getResultID());
        assertEquals(4, incorrectResult2.getResultID());
    }

    /**
     * Tests getUserID() returns the correct user ID.
     */
    @Test
    public void testGettersForUserID() {
        assertEquals(117249823, correctResult1.getUserID());
        assertEquals(117249824, correctResult2.getUserID());
        assertEquals(117249825, incorrectResult1.getUserID());
        assertEquals(117249826, incorrectResult2.getUserID());
    }

    /**
     * Tests getCardID() returns the correct card ID.
     */
    @Test
    public void testGettersForCardID() {
        assertEquals(1, correctResult1.getCardID());
        assertEquals(2, correctResult2.getCardID());
        assertEquals(3, incorrectResult1.getCardID());
        assertEquals(4, incorrectResult2.getCardID());
    }

    /**
     * Tests getAt() returns the correct timestamp.
     */
    @Test
    public void testGettersForTimestamp() {
        assertEquals(Timestamp.valueOf("2025-04-28 10:15:30"), correctResult1.getAt());
        assertEquals(Timestamp.valueOf("2025-04-28 14:45:00"), correctResult2.getAt());
        assertEquals(Timestamp.valueOf("2025-04-28 17:20:45"), incorrectResult1.getAt());
        assertEquals(Timestamp.valueOf("2025-04-28 23:59:59"), incorrectResult2.getAt());
    }

    /**
     * Tests setResultID() updates the result ID.
     */
    @Test
    public void testSettersForResultID() {
        correctResult1.setResultID(10);
        assertEquals(10, correctResult1.getResultID());
    }

    /**
     * Tests setUserID() updates the user ID.
     */
    @Test
    public void testSettersForUserID() {
        correctResult2.setUserID(5555555);
        assertEquals(5555555, correctResult2.getUserID());
    }

    /**
     * Tests setCardID() updates the card ID.
     */
    @Test
    public void testSettersForCardID() {
        incorrectResult1.setCardID(100);
        assertEquals(100, incorrectResult1.getCardID());
    }

    /**
     * Tests setAt() updates the timestamp.
     */
    @Test
    public void testSettersForTimestamp() {
        Timestamp newTimestamp = Timestamp.valueOf("2025-05-01 09:00:00");
        incorrectResult2.setAt(newTimestamp);
        assertEquals(newTimestamp, incorrectResult2.getAt());
    }

    /**
     * Tests setCorrect() updates the correctness value.
     */
    @Test
    public void testSettersForCorrect() {
        correctResult1.setCorrect(false);
        assertFalse(correctResult1.isCorrect());
    }

}
