package git7s.flashcardai.controller;

import git7s.flashcardai.AppDefaults.ViewTitles;
import git7s.flashcardai.AppDefaults.ViewDimensions;
import git7s.flashcardai.AppDefaults.ViewPaths;

import git7s.flashcardai.service.SessionService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public abstract class AbstractController {

    /**
     * ChangeView is used for all Controllers to switch Scenes
     * @param viewTitle The title of the new scene
     * @param actionNode The action node (usually button) to harness the stage
     */
    public void changeView(ViewTitles viewTitle, Node actionNode) {

        ViewPaths viewPath = ViewPaths.valueOf(viewTitle.name());

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(viewPath.get()));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) actionNode.getScene().getWindow();
            stage.setScene(new Scene(root, ViewDimensions.WIDTH.get(), ViewDimensions.HEIGHT.get()));
            stage.setTitle(viewTitle.get());
            stage.show();
        } catch (IOException e) {
            errorDialogue(e.getMessage());
        }
    }

    /**
     * Templated Error Message which, when an exception happened, can be used to create an error box
     * @param errorMessage The error message, most likely from e.getMessage();
     *
     */
    public void errorDialogue(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Occurred");

        alert.setHeaderText(null);
        alert.setContentText(errorMessage);

        alert.showAndWait();
    }

    /**
     * Templated Input Dialogue which can be used to get basic input
     * @param title The title of the dialogue
     * @param header The header of the dialogue
     */
    public Optional<String> inputDialogue(String title, String header) {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle(title);
        inputDialog.setHeaderText(header);

        /// Show the dialog and capture the result
        return inputDialog.showAndWait();
    }

    /**
     * SetPrefColours is used to change the UI colours of all buttons in the scene
     */
    public void setPrefColours(Node ... nodes) {
        String style = "-fx-background-color: " + SessionService.getInstance().uiColour + "; -fx-text-fill: white; -fx-font-weight: bold;";

        for (Node node : nodes) {
            if (node != null) {
                node.setStyle(style);
            }
        }
    }

    /**
     * Logs out the user and moves to the login screen, handling all internal functionality with SessionService
     */
    public void logoutCurrentUser(Node actionNode) {
        SessionService.getInstance().logout();
        changeView(ViewTitles.LOGIN_VIEW, actionNode);
    }

    /**
     * Checks all fields are filled and if so, returns true or false
     */
    public boolean checkFieldsEmpty(TextField... textFields) {
        for (TextField textField : textFields) {
            if (textField.getText().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
