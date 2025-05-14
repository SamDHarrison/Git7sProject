package git7s.flashcardai;


// Import necessary JavaFX classes
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {

    // References to the buttons defined in the FXML file
    public Button myAccountButton;
    public Button accessibilityButton;
    public Button shareFlashCardsButton;

    // Method called when the "My Account" button is clicked
    @FXML
    protected void handleMyAccount() {
        try {
            // Load the FXML file for the "My Account" view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("myAcc-view.fxml"));
            Parent root = loader.load();

            // Get the current stage from the button
            Stage stage = (Stage) myAccountButton.getScene().getWindow();

            // Create and set a new scene for the stage
            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);
            stage.setTitle("My Account");  // Set the window title
        } catch (IOException e) {
            // Print stack trace if loading fails
            e.printStackTrace();
        }
    }

    // Method called when the "Accessibility" button is clicked
    @FXML
    protected void handleAccessibility() {
        // Currently just logs to the console
        System.out.println("Accessibility clicked");
    }

    // Method called when the "Share Flash Cards" button is clicked
    @FXML
    protected void handleShareFlashCards() {
        // Currently just logs to the console
        System.out.println("Share Flash Cards clicked");
    }
}
