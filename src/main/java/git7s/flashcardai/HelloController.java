package flashcardai.git7s;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    public Button nextButton;
    @FXML
    private Label welcomeText;
    @FXML
    private TextArea termsAndConditions;
    @FXML
    private CheckBox agreeCheckBox;

    @FXML
    public void initialize() {
        termsAndConditions.setText("""
                Blah! Blah blah blah blah blah blah
                Blah blah blah blah blah blah
                Blah blah blah blah blah blah
                Blah blah blah blah blah blah
                Blah blah blah blah blah blah
                Blah blah blah blah blah blah
                Blah blah blah blah blah blah
                Blah blah blah blah blah blah
                Blah blah blah blah blah blah""");
    }
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to Address Book Application!");
    }

    @FXML
    protected void onAgreeCheckBoxClick() {
        boolean accepted = agreeCheckBox.isSelected();
        nextButton.setDisable(!accepted);
    }

    @FXML
    protected void onNextButtonClick() throws IOException {
        Stage stage = (Stage) nextButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

}