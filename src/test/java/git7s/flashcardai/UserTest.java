package git7s.flashcardai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User milly;
    private User jacob;
    private User stephannie;

    @BeforeEach
    public void setUp() {
        milly = new User(117249823, "Milly123", "Milly", "Smith", false);
        jacob = new User(117249824, "Jacob456", "Jacob", "Wright", false);
        stephannie = new User(117249825, "Steph789", "Stephannie", "Donald", true);
    }

    /// Authentication Test
    @Test
    void authenticateShouldReturnTrueWhenPasswordIsCorrect() {
        assertTrue(milly.authenticate("Milly123"), "Correct password should authenticate successfully.");
        assertTrue(jacob.authenticate("Jacob456"), "Correct password should authenticate successfully.");
        assertTrue(stephannie.authenticate("Steph789"), "Correct password should authenticate successfully.");
    }

    @Test
    void authenticateShouldReturnFalseWhenPasswordIsIncorrect() {
        assertFalse(milly.authenticate("WrongPass"), "Incorrect password should not authenticate.");
        assertFalse(jacob.authenticate("Jacob123"), "Incorrect password should not authenticate.");
        assertFalse(stephannie.authenticate("Steph890"), "Incorrect password should not authenticate.");
    }

    /// Full Name Test
    @Test
    void getFullNameShouldReturnCorrectFormat() {
        assertEquals("Milly Smith", milly.getFullName());
        assertEquals("Jacob Wright", jacob.getFullName());
        assertEquals("Stephannie Donald", stephannie.getFullName());
    }

    /// Salt Test
    @Test
    void saltShouldBeGeneratedAndEncodedCorrectly() {
        assertNotNull(milly.getSaltAsString());
        assertFalse(milly.getSaltAsString().isEmpty());
    }

    @Test
    void getSaltShouldReturn16Bytes() {
        assertEquals(16, milly.getSalt().length, "Salt should be 16 bytes long.");
    }

    @Test
    void setSaltFromStringShouldDecodeBase64Correctly() {
        String originalSalt = milly.getSaltAsString();
        milly.setSaltFromString(originalSalt);
        assertEquals(originalSalt, milly.getSaltAsString(), "Salt should match after decoding and re-encoding.");
    }

    /// Hash Test
    @Test
    void passwordHashShouldChangeWithNewPassword() {
        String originalHash = jacob.getPasswordHash();
        jacob.setPassword("NewJacobPass123");
        assertNotEquals(originalHash, jacob.getPasswordHash(), "Hash should change after password update");
    }

    @Test
    void hashPasswordShouldBeConsistentWithSameSaltAndInput() {
        byte[] salt = jacob.getSalt();
        String hash1 = jacob.hashPassword("Jacob456", salt);
        String hash2 = jacob.hashPassword("Jacob456", salt);
        assertEquals(hash1, hash2, "Hash should be consistent with same password and salt.");
    }

}