package git7s.flashcardai;

import git7s.flashcardai.dao.UserDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    //Constants
    public static final String TITLE = "Address Book";
    public static final int WIDTH = 640;
    public static final int HEIGHT = 360;

    //Start
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        ///Connect to DB
        UserDAO userDAO = new UserDAO();
        userDAO.createTable();

        userDAO.insert(new User(10528067, "1qaz2wsx","Sam", "Lord", true, ""));
        userDAO.close();
        ///Launch FXML App
        launch();
    }
}