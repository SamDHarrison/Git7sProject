package git7s.flashcardai;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

public class User {

    private int id; /// This is the User's University Number (String "n10528067" -> int 10528067)
    private String passwordHash;
    private byte[] salt; ///This is random salt that is generated for password hashing
    private String firstName;
    private String lastName;
    private boolean admin;

    public User(int id, String password, String firstName, String lastName, boolean admin) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.admin = admin;
        this.salt= generateSalt();
        this.passwordHash = hashPassword(password, this.salt);
    }

    public String toString(){
        return "User: Name: " + firstName + " " + lastName + ". Student ID: " + id + ". Admin: " + admin + ". Subjects: ";
    }

    /// Getters
    public int getId() {
        return id;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public boolean isAdmin() {
        return admin;
    }

    public String getSaltAsString() {
        return Base64.getEncoder().encodeToString(salt);
    }

    /// Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.salt = generateSalt();
        this.passwordHash = hashPassword(password, this.salt);

    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void setSaltFromString(String saltString) {
        this.salt = Base64.getDecoder().decode(saltString);
    }

    //Methods
    public boolean authenticate(String inputPassword) {
        String inputPasswordHashed = hashPassword(inputPassword, this.salt);
        return this.passwordHash.equals(inputPasswordHashed);
    }
    public byte[] generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16]; // 128 bits is a good salt size
        random.nextBytes(salt);
        return salt;
    }


    private String hashPassword(String password, byte[] salt){
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
