package git7s.flashcardai.controller;
import git7s.flashcardai.AppDefaults;
import git7s.flashcardai.dao.CardDAO;
import git7s.flashcardai.dao.ResultDAO;
import git7s.flashcardai.dao.UserDAO;
import git7s.flashcardai.model.*;
import git7s.flashcardai.service.GameService;
import git7s.flashcardai.service.SessionService;
import git7s.flashcardai.service.StatisticsService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * This class controls the dashboard GUI
 */
public class DashboardController extends AbstractController implements IController{
    /**
     * Public default constructor.
     */
    public DashboardController() {
    }
    /**
     * Buttons
     */
    @FXML public Button testAllButton, statsButton, testStrongestSubject, testWeakestSubject, subjectsButton, logOutButton, editAccountButton;
    /**
     * Labels
     */
    @FXML public Label welcomeLabel;
    /**
     * Result Manager for accessing DB
     */
    private ResultManager resultManager;
    /**
     * Card Manager for accessing DB
     */
    private CardManager cardManager;
    /**
     * For accessing the user DB
     */
    private UserManager userManager;
    /**
     * Initialise is run when the GUI is opened.
     */
    @FXML
    public void initialize() {
        //DB
        resultManager = new ResultManager(new ResultDAO());
        cardManager = new CardManager(new CardDAO());
        userManager = new UserManager(new UserDAO());

        setupUIData();
    }

    /**
     * Sets up the UI data.
     */
    @Override
    public void setupUIData(){
        User user = userManager.getUser(SessionService.getInstance().loggedInID);
        SessionService.getInstance().setUiColour(user.getPrefColour());

        String[] studyData = StatisticsService.getInstance().getBasicStudyData();

        testStrongestSubject.setText(studyData[0]);
        testWeakestSubject.setText(studyData[1]);
        welcomeLabel.setText("Welcome, " + user.getFullName());

        setPrefColours(testAllButton, statsButton, testStrongestSubject, testWeakestSubject, subjectsButton, logOutButton, editAccountButton);

    }

    /**
     * Back button for controller.
     */
    @FXML
    @Override
    public void handleBackButton(){
        handleLogout();
    }
    /**
     * Method to go to the Subjects display
     */
    @FXML
    private void handleMySubjects() {
        changeView(AppDefaults.ViewTitles.SUBJECTS_VIEW, subjectsButton);
    }

    /**
     * Method to log out
     */
    @FXML
    private void handleLogout() {
        SessionService.getInstance().logout();
        changeView(AppDefaults.ViewTitles.LOGIN_VIEW, logOutButton);
    }
    /**
     * This method tests all the users flashcards
     */
    public void handleTestAll() {
        if (!cardManager.searchByUserID(SessionService.getInstance().loggedInID).isEmpty()){
            GameService.getInstance().setCurrentGameMode(4);
            changeView(AppDefaults.ViewTitles.TEST_VIEW, testAllButton);
        }
        else {
            errorDialogue(AppDefaults.ErrorMessages.NO_CARDS_NO_PLAY.get());
        }
    }

    /**
     * This method tests the strongest subjects
     */
    public void handleTestStrongest() {
        if (StatisticsService.getInstance().getBasicStudyData()[0].equals("Unknown")){
            errorDialogue(AppDefaults.ErrorMessages.NO_RESULTS_NO_PLAY.get());
        } else {

            GameService.getInstance().setCurrentGameMode(2);
            GameService.getInstance().setCurrentDeck(testStrongestSubject.getText());
            if (cardManager.searchCardsBySubject(GameService.getInstance().getCurrentDeck()).isEmpty()) {
                errorDialogue(AppDefaults.ErrorMessages.NO_CARDS_NO_PLAY.get());
                return;
            }
            changeView(AppDefaults.ViewTitles.TEST_VIEW, testStrongestSubject);
        }
    }

    /**
     * This method tests the weakest subject
     */
    public void handleTestWeakest() {
        if (StatisticsService.getInstance().getBasicStudyData()[1].equals("Unknown")){
            errorDialogue(AppDefaults.ErrorMessages.NO_RESULTS_NO_PLAY.get());
        } else {
            GameService.getInstance().setCurrentGameMode(2);
            GameService.getInstance().setCurrentDeck(testWeakestSubject.getText());
            if (cardManager.searchCardsBySubject(GameService.getInstance().getCurrentDeck()).isEmpty()) {
                errorDialogue(AppDefaults.ErrorMessages.NO_CARDS_NO_PLAY.get());
                return;
            }
            changeView(AppDefaults.ViewTitles.TEST_VIEW, testWeakestSubject);
        }
    }
    /**
     * This method goes to the statistics page
     */
    public void handleStatistics() {
        if (!resultManager.getByUserID(SessionService.getInstance().loggedInID).isEmpty()){
            changeView(AppDefaults.ViewTitles.STATISTICS_VIEW, statsButton);
        }
        else {
            errorDialogue(AppDefaults.ErrorMessages.NO_RESULTS_NO_PLAY.get());
        }
    }

    /**
     * This method goes to the account edit page
     */
    public void handleEditAccount() {
        changeView(AppDefaults.ViewTitles.ACCOUNT_VIEW, editAccountButton);
    }
}


