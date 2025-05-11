package git7s.flashcardai;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * A simple Model Class that represents a user.
 */
public class User {
    /**
     * The userID (student number)
     */
    private int id; // This is the User's University Number (String "n10528067" -> int 10528067)
    /**
     * The password (Hashed)
     */
    private String passwordHash;
    /**
     * The salt for the password hash
     */
    private byte[] salt; //This is random salt that is generated for password hashing
    /**
     * The first name
     */
    private String firstName;
    /**
     * The last name
     */
    private String lastName;
    /**
     * Is the user an admin?
     */
    private boolean admin;

    /**
     * User Constructor - creates a new user with the following params:
     * @param id The userID (studentID) UNIQUE
     * @param password The user's password (unhashed)
     * @param firstName The user's firstname
     * @param lastName The user's lastname
     * @param admin The user's priv level
     * Then the constructor hashes the password
     */
    public User(int id, String password, String firstName, String lastName, boolean admin) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.admin = admin;
        this.salt= generateSalt();
        this.passwordHash = hashPassword(password, this.salt);
    }

    // Getters

    /**
     * Gets the user ID
     * @return userID
     */
    public int getId() {
        return id;
    }
    /**
     * Gets the user's hashed password
     * @return Hashed Password
     */
    public String getPasswordHash() {
        return passwordHash;
    }
    /**
     * Gets the user first name
     * @return First Name
     */
    public String getFirstName() {
        return firstName;
    }
    /**
     * Gets the user last name
     * @return Last Name
     */
    public String getLastName() {
        return lastName;
    }
    /**
     * Gets the user full name
     * @return Full Name (First + Last Name)
     */
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    /**
     * Gets the user's priv level
     * @return boolean priv level (true is an admin)
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * Get's the user's salt as a string
     * @return
     */
    public String getSaltAsString() {
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Get's the user's password salt as a byte[]
     * @return byte[] salt
     */
    public byte[] getSalt() {
        return this.salt;
    }

    // Setters

    /**
     * Sets the user ID
     * @param id UserID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the user password
     * @param password User password (unhashed)
     */
    public void setPassword(String password) {
        this.salt = generateSalt();
        this.passwordHash = hashPassword(password, this.salt);

    }

    /**
     * Sets the password (hashed)
     * @param passwordHash
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Sets the firstname
     * @param firstName First name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the lastname
     * @param lastName Last Name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the priv level
     * @param admin Priv level
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * Sets the salt from a String input
     * @param saltString Salt String input
     */
    public void setSaltFromString(String saltString) {
        this.salt = Base64.getDecoder().decode(saltString);
    }

    //Methods

    /**
     * Authenticates the unhashed and hash password to login the user
     * @param inputPassword Unhashed user password
     * @return Authenticated - true, or not - false
     */
    public boolean authenticate(String inputPassword) {
        String inputPasswordHashed = hashPassword(inputPassword, this.salt);
        return this.passwordHash.equals(inputPasswordHashed);
    }

    /**
     * Generate salt to use for setting password, random input
     * @return byte[] Salt
     */
    public byte[] generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16]; // 128 bits is a good salt size
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Hash a password with an input, and salt
     * @param password Unhashed input
     * @param salt Salt for hashing
     * @return Hashed Password
     */
    public String hashPassword(String password, byte[] salt){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException("Error hashing password", e);

        }

    }


}
