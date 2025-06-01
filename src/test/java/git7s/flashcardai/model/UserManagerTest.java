package git7s.flashcardai.model;

import git7s.flashcardai.dao.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the UserManager class using Mockito to mock the UserDAO.
 */
public class UserManagerTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserManager userManager;

    private AutoCloseable closeable;

    private User user;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        user = new User(117249823, "Milly123", "Milly", "Smith", false, "blue");
    }

    /**
     * Tests that addUser() calls insert() on UserDAO.
     */
    @Test
    public void testAddUser() {
        userManager.addUser(user);
        verify(userDAO).insert(user);
    }

    /**
     * Tests that delete() passes the user ID to UserDAO.
     */
    @Test
    public void testDeleteUser() {
        userManager.delete(user.getId());
        verify(userDAO).delete(user.getId());
    }

    /**
     * Tests that update() passes the correct values to UserDAO.
     */
    @Test
    public void testUpdateUser() {
        User updatedUser = new User(117249823, "NewPass123", "Milly", "Jones", true, "green");
        userManager.update(updatedUser);
        verify(userDAO).update(updatedUser);
    }

    /**
     * Tests that getAll() returns the full list from UserDAO.
     */
    @Test
    public void testGetAllUsers() {
        when(userDAO.getAll()).thenReturn(Collections.singletonList(user));
        List<User> result = userManager.getAll();
        assertEquals(1, result.size());
        assertEquals("Milly", result.get(0).getFirstName());
    }

    /**
     * Tests that getUser() returns the correct user by ID.
     */
    @Test
    public void testGetUserById() {
        when(userDAO.getByID(user.getId())).thenReturn(user);
        User result = userManager.getUser(user.getId());
        assertNotNull(result);
        assertEquals("Smith", result.getLastName());
    }

    /**
     * Tests that searchUsers() filters users by ID.
     */
    @Test
    public void testSearchUsersById() {
        when(userDAO.getAll()).thenReturn(Arrays.asList(
                user,
                new User(117249824, "J123", "James", "Brown", false, "orange")
        ));
        List<User> result = userManager.searchUsers(user.getId());
        assertEquals(1, result.size());
        assertEquals(user.getId(), result.get(0).getId());
    }
}
