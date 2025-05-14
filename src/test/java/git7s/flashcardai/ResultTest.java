/*
public class ResultTest {

    private Result correctResult1;
    private Result correctResult2;
    private Result incorrectResult1;
    private Result incorrectResult2;

    @BeforeEach
    public void setUp() {
        correctResult1 = new Result(1, 117249823, 1, Timestamp.valueOf("2025-04-28 10:15:30"), true);
        correctResult2 = new Result(2, 117249824, 2, Timestamp.valueOf("2025-04-28 14:45:00"), true);
        incorrectResult1 = new Result(3, 117249825, 3, Timestamp.valueOf("2025-04-28 17:20:45"), false);
        incorrectResult2 = new Result(4, 117249826, 4, Timestamp.valueOf("2025-04-28 23:59:59"), false);
    }

    /// Test Correctness
    @Test
    public void testIsCorrectTrue() {
        assertTrue(correctResult1.isCorrect());
        assertTrue(correctResult2.isCorrect());
    }

    @Test
    public void testIsCorrectFalse() {
        assertFalse(incorrectResult1.isCorrect());
        assertFalse(incorrectResult2.isCorrect());
    }

    /// Test Getters
    @Test
    public void testGettersForResultID() {
        assertEquals(1, correctResult1.getResultID());
        assertEquals(2, correctResult2.getResultID());
        assertEquals(3, incorrectResult1.getResultID());
        assertEquals(4, incorrectResult2.getResultID());
    }

    @Test
    public void testGettersForUserID() {
        assertEquals(117249823, correctResult1.getUserID());
        assertEquals(117249824, correctResult2.getUserID());
        assertEquals(117249825, incorrectResult1.getUserID());
        assertEquals(117249826, incorrectResult2.getUserID());
    }

    @Test
    public void testGettersForCardID() {
        assertEquals(1, correctResult1.getCardID());
        assertEquals(2, correctResult2.getCardID());
        assertEquals(3, incorrectResult1.getCardID());
        assertEquals(4, incorrectResult2.getCardID());
    }

    @Test
    public void testGettersForTimestamp() {
        assertEquals(Timestamp.valueOf("2025-04-28 10:15:30"), correctResult1.getAt());
        assertEquals(Timestamp.valueOf("2025-04-28 14:45:00"), correctResult2.getAt());
        assertEquals(Timestamp.valueOf("2025-04-28 17:20:45"), incorrectResult1.getAt());
        assertEquals(Timestamp.valueOf("2025-04-28 23:59:59"), incorrectResult2.getAt());
    }

    /// Test Setters
    @Test
    public void testSettersForResultID() {
        correctResult1.setResultID(10);
        assertEquals(10, correctResult1.getResultID());
    }

    @Test
    public void testSettersForUserID() {
        correctResult2.setUserID(5555555);
        assertEquals(5555555, correctResult2.getUserID());
    }

    @Test
    public void testSettersForCardID() {
        incorrectResult1.setCardID(100);
        assertEquals(100, incorrectResult1.getCardID());
    }

    @Test
    public void testSettersForTimestamp() {
        Timestamp newTimestamp = Timestamp.valueOf("2025-05-01 09:00:00");
        incorrectResult2.setAt(newTimestamp);
        assertEquals(newTimestamp, incorrectResult2.getAt());
    }

    @Test
    public void testSettersForCorrect() {
        correctResult1.setCorrect(false);
        assertFalse(correctResult1.isCorrect());
    }
}
*/
