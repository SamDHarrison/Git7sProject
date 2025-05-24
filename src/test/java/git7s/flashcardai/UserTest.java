package git7s.flashcardai;

import git7s.flashcardai.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link User} class.
 * These tests validate password authentication, full name generation,
 * salt handling, and password hashing behavior.
 *
 */
 public class UserTest {

    private User milly;
    private User jacob;
    private User stephannie;

    /**
     * Initializes multiple user instances with different roles and passwords.
     */
    @BeforeEach
    public void setUp() {
        milly = new User(117249823, "Milly123", "Milly", "Smith", false);
        jacob = new User(117249824, "Jacob456", "Jacob", "Wright", false);
        stephannie = new User(117249825, "Steph789", "Stephannie", "Donald", true);
    }

    /// Authentication Test
    /**
     * Verifies that authentication succeeds with the correct password.
     */
    @Test
    void authenticateShouldReturnTrueWhenPasswordIsCorrect() {
        assertTrue(milly.authenticate("Milly123"), "Correct password should authenticate successfully.");
        assertTrue(jacob.authenticate("Jacob456"), "Correct password should authenticate successfully.");
        assertTrue(stephannie.authenticate("Steph789"), "Correct password should authenticate successfully.");
    }

    /**
     * Verifies that authentication fails with an incorrect password.
     */
    @Test
    void authenticateShouldReturnFalseWhenPasswordIsIncorrect() {
        assertFalse(milly.authenticate("WrongPass"), "Incorrect password should not authenticate.");
        assertFalse(jacob.authenticate("Jacob123"), "Incorrect password should not authenticate.");
        assertFalse(stephannie.authenticate("Steph890"), "Incorrect password should not authenticate.");
    }

    /**
     * Verifies that getFullName() returns the correct "First Last" format.
     */
    /// Full Name Test
    @Test
    void getFullNameShouldReturnCorrectFormat() {
        assertEquals("Milly Smith", milly.getFullName());
        assertEquals("Jacob Wright", jacob.getFullName());
        assertEquals("Stephannie Donald", stephannie.getFullName());
    }

    /// Salt Test
    /**
     * Ensures that salt is generated and encoded as a non-empty Base64 string.
     */
    @Test
    void saltShouldBeGeneratedAndEncodedCorrectly() {
        assertNotNull(milly.getSaltAsString());
        assertFalse(milly.getSaltAsString().isEmpty());
    }

    /**
     * Verifies that the raw salt byte array is exactly 16 bytes long.
     */
    @Test
    void getSaltShouldReturn16Bytes() {
        assertEquals(16, milly.getSalt().length, "Salt should be 16 bytes long.");
    }

    /**
     * Verifies that setting salt from an encoded Base64 string preserves the same salt.
     */
    @Test
    void setSaltFromStringShouldDecodeBase64Correctly() {
        String originalSalt = milly.getSaltAsString();
        milly.setSaltFromString(originalSalt);
        assertEquals(originalSalt, milly.getSaltAsString(), "Salt should match after decoding and re-encoding.");
    }

    /// Hash Test
    /**
     * Verifies that changing the password results in a different password hash.
     */
    @Test
    void passwordHashShouldChangeWithNewPassword() {
        String originalHash = jacob.getPasswordHash();
        jacob.setPassword("NewJacobPass123");
        assertNotEquals(originalHash, jacob.getPasswordHash(), "Hash should change after password update");
    }

    /**
     * Verifies that hashing the same password with the same salt is consistent.
     */

    @Test
    void hashPasswordShouldBeConsistentWithSameSaltAndInput() {
        byte[] salt = jacob.getSalt();
        String hash1 = jacob.hashPassword("Jacob456", salt);
        String hash2 = jacob.hashPassword("Jacob456", salt);
        assertEquals(hash1, hash2, "Hash should be consistent with same password and salt.");
    }

}