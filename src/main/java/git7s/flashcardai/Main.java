package git7s.flashcardai;

import git7s.flashcardai.dao.CardDAO;
import git7s.flashcardai.dao.DatabaseConnection;
import git7s.flashcardai.dao.ResultDAO;
import git7s.flashcardai.model.Card;
import git7s.flashcardai.model.CardManager;
import git7s.flashcardai.model.Result;
import git7s.flashcardai.model.ResultManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * The Main class extends application and is the static base of the program.
 */
public class Main extends Application {
    /**
     * Title of the FXML
     */
    public static final String TITLE = "Flashcard AI";
    /**
     * WIDTH of the FXML
     */
    public static final int WIDTH = 640;
    /**
     * HEIGHT of the FXML
     */
    public static final int HEIGHT = 360;
    /**
     * Static Connection for entire Application
     */
    public static DatabaseConnection DataBaseConnection;
    /**
     * User that is currently logged in.
     */
    public static int loggedInUserID;
    /**
     * Stores the currently selected topic to remediate transition between GUIs
     */
    public static String currentDeck;
    /**
     * Stores the currently selected gamemode 0 - subject, 1 - topic, 2 - subject targeted, 3 - random, 4 - all
     */
    public static int currentGameMode;
    /**
     * For generating test results
     */
    public static String prefCol;

    /**
     * Called to initialise FXML operations
     *
     * @param stage The stage for the current view
     * @throws IOException Exception in-case of error loading stage.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }
    public static void generateFakeResults(int userID) {
        ResultManager resultManager = new ResultManager(new ResultDAO());
        CardManager cardManager = new CardManager(new CardDAO());
        List<Card> cards = cardManager.searchByUserID(userID);
        List<Result> results = resultManager.getByUserID(userID);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Random random = new Random();

        for (int i = 0; i < 150; i++) {
            // Create a calendar instance and set it to the current timestamp
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);

            // Subtract (i + 1) days from the current timestamp
            cal.add(Calendar.DATE, -(i + 1));

            // Convert back to Timestamp
            Timestamp pastTimestamp = new Timestamp(cal.getTimeInMillis());
            int threshold = 10;

            for (Card r : cards) {
                int rand = 30;
                switch (r.getSubject()) {
                    case "Tech": {
                        rand = 20;
                        break;
                    }
                    case "Stuff": {
                        rand = 50;
                        break;

                    }
                    case "Strawberry 101": {
                        rand = 15;
                        break;

                    }
                    case "Physics": {
                        rand = 14;
                        break;

                    }
                    case "History": {
                        rand = 19;
                        break;

                    }
                    case "Health": {
                        rand = 22;
                        break;

                    }
                    case "French": {
                        rand = 13;
                        break;

                    }
                    case "CAB302": {
                        rand = 30;
                        break;

                    }
                    case "CAB202": {
                        rand = 18;
                        break;

                    }
                    case "Animal Theory": {
                        rand = 40;
                        break;

                    }
                }

                boolean res = (random.nextInt(rand) < threshold);
                resultManager.addResult(new Result(r.getUserID(), r.getCardID(), pastTimestamp, res, r.getSubject(), r.getTopic()));

            }
        }
    }
    /**
     * Main method to initialise entire program
     * 1. Creates DB tables
     * 2. Adds shutdown hook to ensure dbs are all safely closed
     * 3. Launch FXML
     *
     * @param args Default Param
     */
    public static void main(String[] args) {
        //Setup
        loggedInUserID = -1; //No user logged in
        currentGameMode = 0;
        currentDeck = "";
        prefCol = "#60C3D8";

        //generateFakeResults(12345);
        //generateFakeResults(12345);
        /// Shutdown hook (manage DB)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                DatabaseConnection.getInstance().close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));
        //Launch FXML App
        launch();

    }



}


