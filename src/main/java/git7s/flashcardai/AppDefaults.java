package git7s.flashcardai;


/**
 * ApplicationDefaults holds the constant variables, enums, and default methods that the program uses. All fields are static.
 */
public final class AppDefaults {

    /**
     * Private Constructor - Cannot be instantiated
     */
    private AppDefaults() {
        throw new AssertionError("Cannot Instantiate ApplicationDefaults");
    }

    /// Application Default Variables
    public static final int startingUserID = -1;
    public static final int startingGameMode = 0;
    public static final String startingDeck = "";
    public static final String startingUIColour = "#60C3D8";
    public static final String DB_URL = "jdbc:sqlite:database.db";

    /// Application Default Enums
    /**
     *  DefaultViewTitles - The default titles of FXML Windows (String)
     */
    public enum ViewTitles {
        TEST_VIEW("Test"),
        CREATE_ACCOUNT_VIEW("Create Account"),
        CREATE_FLASHCARDS_VIEW("Create Flashcards"),
        DASHBOARD_VIEW("Home"),
        LOGIN_VIEW("Login"),
        ACCOUNT_VIEW("My Account"),
        STATISTICS_VIEW("My Statistics"),
        SUBJECTS_VIEW("My Subjects"),
        UPDATE_FLASHCARDS_VIEW("Modify Flashcards");

        private final String title;

        ViewTitles (String title) {
            this.title = title;
        }

        public String get(){
            return "Flashcards AI | " + title;
        }
    }
    /**
     *  DefaultViewPaths - The default paths of FXML Windows (String)
     *  Used by the utility controller class to handle switching
     */
    public enum ViewPaths {
        TEST_VIEW("/git7s/flashcardai/test-view.fxml"),
        CREATE_ACCOUNT_VIEW("/git7s/flashcardai/create-account-view.fxml"),
        CREATE_FLASHCARDS_VIEW("/git7s/flashcardai/create-flashcards-view.fxml"),
        DASHBOARD_VIEW("/git7s/flashcardai/dashboard-view.fxml"),
        LOGIN_VIEW("/git7s/flashcardai/login-view.fxml"),
        ACCOUNT_VIEW("/git7s/flashcardai/account-view.fxml"),
        STATISTICS_VIEW("/git7s/flashcardai/statistics-view.fxml"),
        SUBJECTS_VIEW("/git7s/flashcardai/subjects-view.fxml"),
        UPDATE_FLASHCARDS_VIEW("update-flashcards-view");

        private final String path;

        ViewPaths (String path) {
            this.path = path;
        }

        public String get(){
            return path;
        }
    }
    /**
     *  DefaultViewDimensions - The default dimensions of the window
     */
    public enum ViewDimensions {
        WIDTH(640),
        HEIGHT(360);

        private final int dimension;

        ViewDimensions (int dimension) {
            this.dimension = dimension;
        }

        public int get(){
            return dimension;
        }
    }
    /**
     *  ErrorMessages - The default msgs of UI feedback when error occurs
     */
    public enum ErrorMessages {
        ADMIN_DENIED("Admin Access NOT Granted"),
        ACCOUNT_DELETE_FAIL("Incorrect Password, Account NOT Deleted"),
        NOT_FILLED_FIELDS("Please fill in all fields"),
        NOT_CONFIRMED_PASSWORD("Passwords are not matching"),
        NOT_NUMERICAL_ID("Your Student ID must be all numerical digits"),
        NOT_UNIQUE_USERNAME("The specified Student ID already exists"),
        INCORRECT_PASSWORD("Password Incorrect"),
        UPDATE_FLASHCARD_ERROR("There was an error, try again"),
        NO_CARDS_NO_PLAY("You have no cards! Press My Subjects to create some!"),
        NO_RESULTS_NO_PLAY("You need to play more before you can do that!");

        private final String msg;

        ErrorMessages (String msg) {
            this.msg = msg;
        }

        public String get(){
            return msg;
        }
    }

    /**
     *  SuccessMessages - The default msgs of UI feedback when a successful action happens
     */
    public enum SuccessMessages {
    ADMIN_GRANTED("Admin Access Granted"),
    ACCOUNT_CHANGED("User Details Changed"), UPDATED_FLASHCARD("Your flashcard has been updated"),
    DELETED_FLASHCARD("Your flashcard was deleted"),
    WAITING_FOR_AI("Waiting for AI to generate"),
    SUCCESSFUL_GENERATION("You may exit this window!");


        private final String msg;

        SuccessMessages (String msg) {
            this.msg = msg;
        }

        public String get(){
            return msg;
        }
    }


}
