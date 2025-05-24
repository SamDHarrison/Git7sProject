package git7s.flashcardai;

import git7s.flashcardai.model.User;
import git7s.flashcardai.model.UserManager;
import git7s.flashcardai.dao.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagerTest {

    private UserManager userManager;
    private User milly;
    private User jacob;
    private User stephannie;

    @BeforeEach
    public void setUp() {
        UserDAO userDAO = new UserDAO();
        userManager = new UserManager(userDAO);

        milly = new User(117249823, "Milly123", "Milly", "Smith", false);
        jacob = new User(117249824, "Jacob456", "Jacob", "Wright", false);
        stephannie = new User(117249825, "Steph789", "Stephannie", "Donald", true);

        userManager.delete(117249823);
        userManager.delete(117249824);
        userManager.delete(117249825);
    }

    @Test
    public void testAddUsers() {
        userManager.addUser(milly);
        userManager.addUser(jacob);
        userManager.addUser(stephannie);

        assertFalse(userManager.searchUsers(117249823).isEmpty());
        assertFalse(userManager.searchUsers(117249824).isEmpty());
        assertFalse(userManager.searchUsers(117249825).isEmpty());
    }

    @Test
    public void testUpdateUser() {
        userManager.addUser(milly);
        milly.setLastName("Jones");
        userManager.update(117249823, milly);

        List<User> updated = userManager.searchUsers(117249823);
        assertEquals("Jones", updated.get(0).getLastName());
    }

    @Test
    public void testGetAllIncludesInsertedUsers() {
        userManager.addUser(milly);
        userManager.addUser(jacob);
        List<User> allUsers = userManager.getAll();

        assertTrue(allUsers.stream().anyMatch(u -> u.getId() == 117249823));
        assertTrue(allUsers.stream().anyMatch(u -> u.getId() == 117249824));
    }

    @Test
    public void testDeleteUser() {
        userManager.addUser(stephannie);
        userManager.delete(117249825);
        assertTrue(userManager.searchUsers(117249825).isEmpty());
    }
}