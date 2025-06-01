package git7s.flashcardai;

import git7s.flashcardai.service.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * The Main class extends application and is the static base of the program.
 */
public class Main extends Application {
    /**
     * Called to initialise FXML operations
     * @param stage The stage for the current view
     * @throws IOException Exception in-case of error loading stage.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(AppDefaults.ViewPaths.LOGIN_VIEW.get()));
        Scene scene = new Scene(fxmlLoader.load(), AppDefaults.ViewDimensions.WIDTH.get(), AppDefaults.ViewDimensions.HEIGHT.get());
        stage.setTitle(AppDefaults.ViewTitles.LOGIN_VIEW.get());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Main method to initialise entire program
     * Instantiates Services
     * Sets the shutdown hook to protect the database
     * Launches the app
     * @param args Default Param
     */
    public static void main(String[] args) {

        SessionService session = SessionService.getInstance();
        GameService game = GameService.getInstance();
        AdminService admin = AdminService.getInstance();
        StatisticsService stats = StatisticsService.getInstance();
        DatabaseService db = DatabaseService.getInstance();
        setShutdownHook();

        launch();
    }

    /**
     * Sets the Shutdown hook to prevent DB being damaged
     */
    public static void setShutdownHook(){
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                DatabaseService.getInstance().getConnection().close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));
    }
}


