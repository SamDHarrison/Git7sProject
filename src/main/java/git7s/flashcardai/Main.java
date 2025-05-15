package git7s.flashcardai;

import git7s.flashcardai.dao.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

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
        //Setup
        loggedInUserID = -1; //No user logged in
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
    /**
     * Tacky message we have been using - getting rid of it, here for debugging only or critical messages
     * @param message
     */
    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}


