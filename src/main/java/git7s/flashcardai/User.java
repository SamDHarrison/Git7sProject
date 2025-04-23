package git7s.flashcardai;

import java.util.ArrayList;
import java.util.Arrays;

public class User {

    private int id; /// This is the User's University Number (String "n10528067" -> int 10528067)
    private String password; ///Look at hashing this later in a seperate table
    private String firstName;
    private String lastName;
    private boolean admin;

    public User(int id, String password, String firstName, String lastName, boolean admin) {
        this.id = id;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.admin = admin;
    }

    public String toString(){
        return "User: Name: " + firstName + " " + lastName + ". Student ID: " + id + ". Admin: " + admin + ". Subjects: ";
    }

    /// Getters
    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isAdmin() {
        return admin;
    }

    /// Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
