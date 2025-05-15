package git7s.flashcardai;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

public class MyAccountController {

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void handleEditUsername() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit Username");
        alert.setHeaderText(null);
        alert.setContentText("Edit username functionality coming soon!");
        alert.showAndWait();
    }

    @FXML
    protected void handleDeleteAccount() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Delete Account");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("This will delete all your data and cannot be undone.");
        alert.showAndWait();
    }
}
