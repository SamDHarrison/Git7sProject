package git7s.flashcardai;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class DashboardController {
    public Label progressTextLabel;
    @FXML private ProgressBar studyProgressBar;
    @FXML private Label strongestTopicLabel;
    @FXML private Label weakestTopicLabel;
    @FXML private Button mySubjectsButton;

    @FXML private Button createDeckButton;
    @FXML private Button viewFlashcardsButton;
    @FXML private Button testAllFlashcardsButton;
    @FXML private Button settingsButton;

    @FXML
    public void initialize() {
        // Set placeholder data
        studyProgressBar.setProgress(0.75); // 75% completion
        strongestTopicLabel.setText("Programming");
        weakestTopicLabel.setText("Science");
    }
//Test My Subject button function
    @FXML
    private void handleMySubjects() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/git7s/flashcardai/my-subjects-view.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) mySubjectsButton.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            stage.setTitle("My Subjects");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


