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
    /**
     * The starting user ID which indicates no logged-in user
     */
    public static final int startingUserID = -1;
    /**
     *  Starting UI Colour
     */
    public static final String startingUIColour = "#60C3D8";
    /**
     * Constant DB URL
     */
    public static final String DB_URL = "jdbc:sqlite:database.db";

    /// Application Default Enums
    /**
     *  DefaultViewTitles - The default titles of FXML Windows (String)
     */
    public enum ViewTitles {
        /** View Object Titles */
        TEST_VIEW("Test"),
        /** View Object Titles */
        CREATE_ACCOUNT_VIEW("Create Account"),
        /** View Object Titles */
        CREATE_FLASHCARDS_VIEW("Create Flashcards"),
        /** View Object Titles */
        DASHBOARD_VIEW("Home"),
        /** View Object Titles */
        LOGIN_VIEW("Login"),
        /** View Object Titles */
        ACCOUNT_VIEW("My Account"),
        /** View Object Titles */
        STATISTICS_VIEW("My Statistics"),
        /** View Object Titles */
        SUBJECTS_VIEW("My Subjects"),
        /** View Object Titles */
        UPDATE_FLASHCARDS_VIEW("Modify Flashcards");

        private final String title;
        /**
         * Setter
         */
        ViewTitles (String title) {
            this.title = title;
        }
        /**
         * Getter
         * @return String
         */
        public String get(){
            return "Flashcards AI | " + title;
        }
    }
    /**
     *  DefaultViewPaths - The default paths of FXML Windows (String)
     *  Used by the utility controller class to handle switching
     */
    public enum ViewPaths {
        /** View Object Paths */
        TEST_VIEW("/git7s/flashcardai/test-view.fxml"),
        /** View Object Paths */
        CREATE_ACCOUNT_VIEW("/git7s/flashcardai/create-account-view.fxml"),
        /** View Object Paths */
        CREATE_FLASHCARDS_VIEW("/git7s/flashcardai/create-flashcards-view.fxml"),
        /** View Object Paths */
        DASHBOARD_VIEW("/git7s/flashcardai/dashboard-view.fxml"),
        /** View Object Paths */
        LOGIN_VIEW("/git7s/flashcardai/login-view.fxml"),
        /** View Object Paths */
        ACCOUNT_VIEW("/git7s/flashcardai/account-view.fxml"),
        /** View Object Paths */
        STATISTICS_VIEW("/git7s/flashcardai/statistics-view.fxml"),
        /** View Object Paths */
        SUBJECTS_VIEW("/git7s/flashcardai/subjects-view.fxml"),
        /** View Object Paths */
        UPDATE_FLASHCARDS_VIEW("update-flashcards-view");

        private final String path;
        /**
         * Setter
         */
        ViewPaths (String path) {
            this.path = path;
        }
        /**
         * Getter
         * @return String
         */
        public String get(){
            return path;
        }
    }
    /**
     *  DefaultViewDimensions - The default dimensions of the window
     */
    public enum ViewDimensions {
        /** Default Width */
        WIDTH(640),
        /** Default Height */
        HEIGHT(360);

        private final int dimension;
        /**
         * Setter
         */
        ViewDimensions (int dimension) {
            this.dimension = dimension;
        }
        /**
         * Getter
         * @return Int
         */
        public int get(){
            return dimension;
        }
    }
    /**
     *  ErrorMessages - The default msgs of UI feedback when error occurs
     */
    public enum ErrorMessages {
        /** Error Message */
        ADMIN_DENIED("Admin Access NOT Granted"),
        /** Error Message */
        ACCOUNT_DELETE_FAIL("Incorrect Password, Account NOT Deleted"),
        /** Error Message */
        NOT_FILLED_FIELDS("Please fill in all fields"),
        /** Error Message */
        NOT_CONFIRMED_PASSWORD("Passwords are not matching"),
        /** Error Message */
        NOT_NUMERICAL_ID("Your Student ID must be all numerical digits"),
        /** Error Message */
        NOT_UNIQUE_USERNAME("The specified Student ID already exists"),
        /** Error Message */
        INCORRECT_PASSWORD("Password Incorrect"),
        /** Error Message */
        UPDATE_FLASHCARD_ERROR("There was an error, try again"),
        /** Error Message */
        NO_CARDS_NO_PLAY("You have no cards! Press My Subjects to create some!"),
        /** Error Message */
        NO_RESULTS_NO_PLAY("You need to play more before you can do that!");

        private final String msg;
        /**
         * Setter
         */
        ErrorMessages (String msg) {
            this.msg = msg;
        }
        /**
         * Getter
         * @return String
         */
        public String get(){
            return msg;
        }
    }

    /**
     *  SuccessMessages - The default msgs of UI feedback when a successful action happens
     */
    public enum SuccessMessages {
        /** Success Message */
        ADMIN_GRANTED("Admin Access Granted"),
        /** Success Message */
        ACCOUNT_CHANGED("User Details Changed"),
        /** Success Message */
        UPDATED_FLASHCARD("Your flashcard has been updated"),
        /** Success Message */
        DELETED_FLASHCARD("Your flashcard was deleted"),
        /** Success Message */
        WAITING_FOR_AI("Waiting for AI to generate"),
        /** Success Message */
        SUCCESSFUL_GENERATION("You may exit this window!");


        private final String msg;
        /**
         * Setter
         */
        SuccessMessages (String msg) {
            this.msg = msg;
        }
        /**
         * Getter
         * @return String
         */
        public String get(){
            return msg;
        }
    }


}
