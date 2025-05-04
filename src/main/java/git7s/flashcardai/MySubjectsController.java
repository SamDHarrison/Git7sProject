package git7s.flashcardai;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class MySubjectsController {

    public Button testCurrentTopic;
    @FXML
    private ComboBox<String> subjectComboBox;

    @FXML
    private ListView<String> topicsListView;

    @FXML
    private Button createFlashcardsButton;

    // Subjects for dropdown menu
    private ObservableList<Card> usersCards;
    private ObservableList<String> subjects;
    private ObservableList<String> selectedTopics;

    @FXML
    public void initialize() {
        //Load all users cards
        usersCards = FXCollections.observableArrayList(Main.cardDAO.getByUserID(Main.loggedInUser.getId()));
        subjects = FXCollections.observableArrayList();
        selectedTopics = FXCollections.observableArrayList();
        //Pull menu values
        for (Card card : usersCards) {
            if(subjects != null) {
                if (!subjects.contains(card.getSubject())) {
                    subjects.add(card.getSubject());
                }
            } else {
                subjects.add(card.getSubject());
            }
        }
        subjectComboBox.setItems(subjects);

    }

    @FXML
    private void handleSubjectSelection() {
        String selectedSubject = subjectComboBox.getValue();

        if (selectedSubject != null && subjects.contains(selectedSubject)) {
            topicsListView.setItems(topicSelection(selectedSubject));
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
            //Refresh
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ObservableList<String> topicSelection(String s) {
        selectedTopics.clear();
        for (Card card : usersCards) {
            if (card.getSubject().equals(s) && !selectedTopics.contains(card.getTopic())) {
                selectedTopics.add(card.getTopic());
            }
        }

        return selectedTopics;
    }

    public void handleTestCurrentTopic() {
        String selection = topicsListView.getSelectionModel().getSelectedItem();

        if (selection != null){
            if (selectedTopics.contains(selection)){
                try {
                    Main.currentDeck = selection;
                    Main.currentDeckProgress = 0;

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/git7s/flashcardai/card-deck-view.fxml"));
                    Parent root = fxmlLoader.load();

                    Stage stage = (Stage) testCurrentTopic.getScene().getWindow();
                    stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
                    stage.setTitle("Flashcard AI - Test");
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

            }
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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