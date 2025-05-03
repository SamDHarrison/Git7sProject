package git7s.flashcardai;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySubjectsController {

    @FXML
    private ComboBox<String> subjectComboBox;

    @FXML
    private ListView<String> topicsListView;

    // Subjects for dropdown menu are hardcoded for now. Need to work on logic to allow User to create subject.
    private final Map<String, List<String>> subjectTopics = new HashMap<>();

    @FXML
    public void initialize() {
        subjectTopics.put("Math", List.of("Algebra", "Calculus", "Geometry"));
        subjectTopics.put("Science", List.of("Physics", "Chemistry", "Biology"));
        subjectTopics.put("History", List.of("Ancient Rome", "World War II", "Cold War"));

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
}