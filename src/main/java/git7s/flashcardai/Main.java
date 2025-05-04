package git7s.flashcardai;

import git7s.flashcardai.dao.CardDAO;
import git7s.flashcardai.dao.ResultDAO;
import git7s.flashcardai.dao.UserDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;

public class Main extends Application {
    //Constants
    public static final String TITLE = "Flashcard AI";
    public static final int WIDTH = 640;
    public static final int HEIGHT = 360;
    public static UserDAO userDAO = new UserDAO();
    public static ResultDAO resultDAO = new ResultDAO();
    public static CardDAO cardDAO = new CardDAO();
    public static User loggedInUser = null;

    //Start
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        ///Connect to DB
            userDAO.createTable();
            ///userDAO.insert(new User(123456789, "password", "Antonio", "miguel", true));
            resultDAO.createTable();
            ///resultDAO.insert(new Result(1, 123456789, 1, new Timestamp(System.currentTimeMillis()), true));
            cardDAO.createTable();
            ///cardDAO.insert(new Card(5, 123456789, "Week 8", "CAB202", "Java", "This is a programming language"));
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


