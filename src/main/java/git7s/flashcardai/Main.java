package git7s.flashcardai;

import git7s.flashcardai.dao.CardDAO;
import git7s.flashcardai.dao.ResultDAO;
import git7s.flashcardai.dao.UserDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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
     * User DAO object
     */
    public static UserDAO userDAO = new UserDAO();
    /**
     * Result DAO object
     */
    public static ResultDAO resultDAO = new ResultDAO();
    /**
     * Card DAO object
     */
    public static CardDAO cardDAO = new CardDAO();
    /**
     * User that is currently logged in.
     */
    public static User loggedInUser = null;


    /**
     * Called to initialise FXML operations
     * @param stage The stage for the current view
     * @throws IOException Exception in-case of error loading stage.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main method to initialise entire program
     * 1. Creates DB tables
     * 2. Adds shutdown hook to ensure dbs are all safely closed
     * 3. Launch FXML
     * @param args Default Param
     */
    public static void main(String[] args) {
        ///Connect to DB
            userDAO.createTable();
            resultDAO.createTable();
            cardDAO.createTable();
        /// Shutdown hook to close connections
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (userDAO != null) userDAO.close();
                if (resultDAO != null) resultDAO.close();
                if (cardDAO != null) cardDAO.close();
                System.out.println("DAO connections closed successfully.");
            } catch (Exception e) {
                System.err.println("Error closing DAO connections: " + e.getMessage());
            }
        }));
        ///Launch FXML App
        launch();

    }


}


