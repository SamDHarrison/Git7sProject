package git7s.flashcardai.service;


import git7s.flashcardai.AppDefaults;

/**
 * SessionManager is designed using the Singleton Pattern and tracks the current user's session
 *
 */
public class SessionService {

    /**
     * Protected constructor - cannot be instantiated
     */
    protected SessionService() {
        loggedInID = AppDefaults.startingUserID;
        uiColour = AppDefaults.startingUIColour;
    }

    /**
     * Subclass which acts as session container
     */
    private static class Session {
        private static final SessionService INSTANCE = new SessionService();
    }

    /**
     * getInstance returns the static INSTANCE of SessionService
     * @return INSTANCE
     */
    public static SessionService getInstance() {
        return Session.INSTANCE;
    }

    /// Session related state variables
    /**
     * Tracks the currently logged-in user
     */
    public int loggedInID;
    /**
     * Tracks the currently logged-in user's preferred UI colour
     */
    public String uiColour;

    /// Session Related Methods
    /**
     * Logs out the user
     */
    public void logout(){
        loggedInID = AppDefaults.startingUserID;
        uiColour = AppDefaults.startingUIColour;
    }

    /**
     * Logs in the user
     */
    public void login(int id){
        loggedInID = id;
    }

    /**
     * Logs in the user
     */
    public void setUiColour(String col){
        uiColour = col;
    }
}
