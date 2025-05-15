package git7s.flashcardai.controller;

import git7s.flashcardai.dao.CardDAO;
import git7s.flashcardai.model.Card;
import git7s.flashcardai.Main;
import git7s.flashcardai.model.CardManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.util.List;

/**
 * A class that handles the My Subjects GUI
 */
public class MySubjectsController {
    /**
     * This button tests the user on the specified topic
     */
    @FXML
    public Button testCurrentTopic;
    /**
     * This ComboBox contains all the user's subjects
     */
    @FXML
    private ComboBox<String> subjectComboBox;
    /**
     * All the users topics
     */
    @FXML
    private ListView<String> topicsListView;

    // Subjects for dropdown menu
    /**
     * List that contains all the options for the card dropdown menu
     */
    private List<Card> usersCards;
    /**
     * List that contains all the options for the subject dropdown menu
     */
    private ObservableList<String> subjects;
    /**
     * List that contains all the options for the topic dropdown menu
     */
    private ObservableList<String> selectedTopics;
    /**
     * First subject selection
     */
    private String firstSubjectSelection;
    /**
     * First topic selection
     */
    private String firstTopicSelection;
    /**
     * Card Manager
     */
    private CardManager cardManager;

    /**
     * Initialises the GUI
     * 1. Loads users cards
     * 2. Pulls the menu values
     * 3. Sets comboboxes
     */
    @FXML
    public void initialize() {
        //Load all users cards
        cardManager = new CardManager(new CardDAO());
        usersCards = cardManager.searchByUserID(Main.loggedInUserID);

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
        subjectComboBox.getSelectionModel().selectFirst();
        if (!usersCards.isEmpty()){
            if (firstSubjectSelection == null || firstTopicSelection == null){
                subjectComboBox.getSelectionModel().selectFirst();
                handleSubjectSelection();
            } else {
                subjectComboBox.getSelectionModel().select(firstSubjectSelection);
                topicsListView.getSelectionModel().select(firstTopicSelection);
            }
        }
    }

    /**
     *Handles the subject selection - if the user clicks a subject, load all the topics
     */
    @FXML
    private void handleSubjectSelection() {
        String selectedSubject = subjectComboBox.getValue();

        if (selectedSubject != null) {
            topicsListView.setItems(topicSelection(selectedSubject));
        }
    }

    /**
     * If the user clicks the create button, this method is run
     * 1. Try to load new stage
     * 2. Popup created
     * 3. Current screen frozen
     */
    @FXML
    private void handleCreateFlashcardsPopup() {
        try {
            firstSubjectSelection = "";
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/git7s/flashcardai/create-new-flashcards.fxml"));
            Parent popupRoot = fxmlLoader.load();

            CreateNewFlashcardsController controller = fxmlLoader.getController();
            controller.setParent(this);

            Stage popupStage = new Stage();
            popupStage.setTitle("Create Flashcards");
            popupStage.setScene(new Scene(popupRoot));
            popupStage.initModality(Modality.APPLICATION_MODAL); // Blocks interaction with the My Subject view
            popupStage.showAndWait();
            //Refresh
            initialize();
            if (!firstSubjectSelection.isEmpty()){
                handleUpdateFlashcards();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * This method takes a String s (subject) and returns a list of all the topics of that subject.
     * @param s The input subject query
     * @return The ObservableList of the topics for the specified inputted subject
     */
    private ObservableList<String> topicSelection(String s) {
        selectedTopics.clear();
        for (Card card : usersCards) {
            if (card.getSubject().equals(s) && !selectedTopics.contains(card.getTopic())) {
                selectedTopics.add(card.getTopic());
            }
        }

        return selectedTopics;
    }
    /**
     * This method handles the button that takes the user to the test screen
     * 1. Set selected topic
     * 2. Try to load screen
     * 2a. Pass selection through to the next screen through the constructor
     */
    public void handleTestCurrentTopic() {
        String selection = topicsListView.getSelectionModel().getSelectedItem();

        if (selection != null){
            if (selectedTopics.contains(selection)){
                try {
                    Main.currentDeck = selection;

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
    /**
     * This button takes the user back to the dashboard
     */
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

    /**
     * This method handles the deletion of flashcards based on the selected subject and topic.
     * Ensures UI is refreshed so that the deleted topic and subject are properly removed.
     * @implNote It also handles refreshing the dropdown and list views, and displays a confirmation or error alert.
     */
    @FXML
    private void handleDeleteSubject() {
        // Get selected subject and topic from UI
        String selectedSubject = subjectComboBox.getValue();
        String selectedTopic = topicsListView.getSelectionModel().getSelectedItem();

        if (selectedSubject != null && selectedTopic != null) {
            /// FIX ADD DELETE SUBJECT, DELETE TOPIC
            // Delete the flashcards under chosen subject and topic for the user
            cardManager.deleteSubject(selectedSubject, selectedTopic);
            // Pull updated list of user's cards from the DB
            initialize();
        }
    }

    /**
     * This method opens a popup window for updating flashcards under a selected topic.
     * @implNote It also sets the selected topic as the active deck in Main before loading the new window.
     */
    @FXML
    private void handleUpdateFlashcards() {
        // Get selected subject and topic from UI
        String selectedSubject = subjectComboBox.getValue();
        String selectedTopic = topicsListView.getSelectionModel().getSelectedItem();

        if (selectedSubject != null && selectedTopic != null) {
            // Store current deck for use in the update view
            Main.currentDeck = selectedTopic;
            try {
                // 1. Load the update flashcards view
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/git7s/flashcardai/update-flashcards-view.fxml"));
                Parent root = fxmlLoader.load();

                // 2. Create and configure new popup window
                Stage popupStage = new Stage();
                popupStage.setTitle("Update Flashcards");
                popupStage.setScene(new Scene(root));
                popupStage.initModality(Modality.APPLICATION_MODAL); // Prevent interaction with other windows

                // 3. Show popup and pause background
                popupStage.showAndWait();

                // Refresh the current page once popup is closed
                initialize();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //Error message
        }
    }



    public void setSelected(String subject, String topic){
        this.firstTopicSelection = topic;
        this.firstSubjectSelection = subject;
    }
}