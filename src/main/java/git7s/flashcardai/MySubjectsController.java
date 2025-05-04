package git7s.flashcardai;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class MySubjectsController {

    @FXML
    private ComboBox<String> subjectComboBox;

    @FXML
    private ListView<String> topicsListView;

    @FXML
    private Button createFlashcardsButton;

    // Subjects for dropdown menu are hardcoded for now. Need to work on logic to allow user to create subject.
    private final Map<String, List<String>> subjectTopics = new HashMap<>();

    @FXML
    public void initialize() {
        subjectTopics.put("CAB302 Software Development", List.of("Collaborative Programming", "High Performing Teams", "Graphical User Interface"));
        subjectTopics.put("IFB240 Cyber Security", List.of("Threats and Vulnerabilities", "Risk Management", "User Authentication"));
        subjectTopics.put("CAB222 Networks", List.of("Subnetting and Supernetting", "Routing"));

        ObservableList<String> subjects = FXCollections.observableArrayList(subjectTopics.keySet());
        subjectComboBox.setItems(subjects);
    }

    @FXML
    private void handleSubjectSelection() {
        String selectedSubject = subjectComboBox.getValue();

        if (selectedSubject != null && subjectTopics.containsKey(selectedSubject)) {
            topicsListView.setItems(FXCollections.observableArrayList(subjectTopics.get(selectedSubject)));
        } else {
            topicsListView.setItems(FXCollections.observableArrayList());
        }
    }

    @FXML
    private void handleCreateFlashcardsPopup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/git7s/flashcardai/create-new-flashcards.fxml"));
            Parent popupRoot = fxmlLoader.load();

            Stage popupStage = new Stage();
            popupStage.setTitle("Create Flashcards");
            popupStage.setScene(new Scene(popupRoot));
            popupStage.initModality(Modality.APPLICATION_MODAL); // Blocks interaction with the My Subject view
            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToDashboard() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/git7s/flashcardai/dashboard-view.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) subjectComboBox.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            stage.setTitle("Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}