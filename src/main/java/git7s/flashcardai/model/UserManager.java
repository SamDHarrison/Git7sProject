//Packages
package git7s.flashcardai.model;
//Imports
import git7s.flashcardai.dao.UserDAO;
import java.util.List;
/**
 * The model object for interacting with the User DAO
 */
public class UserManager {
    /**
     * Local DAO object that is filled by calling screen
     */
    private final UserDAO userDAO;

    /**
     * The Constructor which takes the caller's DAO object and updates local
     * @param userDAO DAO object which enables the manager.
     */
    public UserManager(UserDAO userDAO){
        this.userDAO = userDAO;
    }
    /**
     * Search Function that gets a list of users for handling GUI-side
     * @param userID The USERID
     * @return Users
     */
    public List<User> searchUsers(int userID){
        return userDAO.getAll()
                .stream()
                .filter(user -> (user.getId() == userID))
                .toList();
    }
    /**
     * Search Function that gets a user for handling GUI-side
     * @param userID The USERID
    * @return Users
     */
    public User getUser(int userID){
        return userDAO.getByID(userID);
    }
    /**
     * Inserts a user to the DAO
     * @param user New User for insertion
     */
    public void addUser(User user){
        userDAO.insert(user);
    }
    /**
     * Update a specific user to the DAO
     * @param user The updated user

     */
    public void update(User user){
        userDAO.update(user);
    }
    /**
     * Deletes a specific user from the db
     * @param userID Specified user
     */
    public void delete(int userID){
        userDAO.delete(userID);
    }
    /**
     * Gets a list of all the users in the DB
     * @return List of users
     */
    public List<User> getAll(){
        return userDAO.getAll();
    }

}
