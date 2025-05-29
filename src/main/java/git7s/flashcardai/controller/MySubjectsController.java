package git7s.flashcardai.controller;

import git7s.flashcardai.dao.CardDAO;
import git7s.flashcardai.dao.ResultDAO;
import git7s.flashcardai.model.Card;
import git7s.flashcardai.Main;
import git7s.flashcardai.model.CardManager;
import git7s.flashcardai.model.ResultManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    public PieChart strongestPieChart;
    public PieChart weakestPieChart;
    public TabPane gameModePane;
    public ToggleButton strongestToggleButton;
    public ToggleButton weakestToggleButton;
    public ListView<String> subjectsListView;
    public Label randomDescription;
    public Slider randomSlider;
    public Button deleteFlashcardsButton;
    public Button updateFlashcardsButton;
    public Button backButton;
    public Button createFlashCardsButton;
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
     * Card Manager
     */
    private ResultManager resultManager;
    /**
     * The Toggle Group for selecting a targeted test
     */
    private ToggleGroup targetGroup;

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
        resultManager = new ResultManager(new ResultDAO());
        usersCards = cardManager.searchByUserID(Main.loggedInUserID);

        int numCards = cardManager.searchByUserID(Main.loggedInUserID).size();
        String[] studyData = resultManager.getBasicStudyData();

        subjects = FXCollections.observableArrayList();
        selectedTopics = FXCollections.observableArrayList();
        setPrefColours(Main.prefCol);

        gameModePane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (oldTab != null) oldTab.setStyle("-fx-font-size: 16px; -fx-background-color: #FFFFFF;");
            if (newTab != null) newTab.setStyle("-fx-font-size: 16px; -fx-background-color: #60C3D8;");

            assert newTab != null;
            if (newTab.getText().equals("Random Testing") || newTab.getText().equals("Targeted Testing")) {
                deleteFlashcardsButton.setVisible(false);
                updateFlashcardsButton.setVisible(false);
            } else {
                deleteFlashcardsButton.setVisible(true);
                updateFlashcardsButton.setVisible(true);
            }
        });

        if (numCards == 0) {
            for (Tab tab : gameModePane.getTabs()) {
                tab.setDisable(true);
            }
        } else {
            gameModePane.getSelectionModel().selectFirst();
        }




        //Setup the Targeted Pane
        if (resultManager.getByUserID(Main.loggedInUserID).size() > 1) {
            targetGroup = new ToggleGroup();
            strongestToggleButton.setToggleGroup(targetGroup);
            weakestToggleButton.setToggleGroup(targetGroup);
            targetGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                // Reset previous button style
                if (oldToggle != null) {
                    ((ToggleButton) oldToggle).setStyle("-fx-background-color: #FFFFFF;");
                }
                // Highlight the selected button
                if (newToggle != null) {
                    ((ToggleButton) newToggle).setStyle("-fx-font-weight: bold; -fx-background-color: #60C3D8;");
                }
            });
            strongestToggleButton.setSelected(true);
        }

        strongestPieChart.setData(FXCollections.observableArrayList(
                new PieChart.Data("Correct %", Integer.parseInt(studyData[2])),
                new PieChart.Data("Incorrect %", 100-Integer.parseInt(studyData[2]))
        ));

        weakestPieChart.setData(FXCollections.observableArrayList(
                new PieChart.Data("Correct %", Integer.parseInt(studyData[3])),
                new PieChart.Data("Incorrect %", 100-Integer.parseInt(studyData[3]))
        ));
        strongestPieChart.setLabelsVisible(false);
        weakestPieChart.setLabelsVisible(false);
        strongestToggleButton.setText("Strongest: " + studyData[0] + " (" + studyData[2] + "%)");
        weakestToggleButton.setText("Weakest: " + studyData[1] + " (" + studyData[3] + "%)");

        if (resultManager.getByUserID(Main.loggedInUserID).isEmpty()) {
            weakestPieChart.setVisible(false);
            strongestPieChart.setVisible(false);
            strongestToggleButton.setVisible(false);
            weakestToggleButton.setVisible(false);
        }

        //Setup the random pane
        randomSlider.setMax(numCards);
        randomSlider.setMin(0);
        randomSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            randomDescription.setText("You will test " + newVal.intValue() + " random flashcards");
        });


        //Setup the subject, topic panes
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
        subjectsListView.setItems(subjects);

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
        if (gameModePane.getSelectionModel().getSelectedItem().getText().equals("Subjects") && subjectsListView.getSelectionModel().getSelectedItem() != null){
            String selection = subjectsListView.getSelectionModel().getSelectedItem();
            System.out.println(selection);
            try {
                Main.currentGameMode = 0;
                Main.currentDeck = selection;

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/git7s/flashcardai/card-deck-view.fxml"));
                Parent root = fxmlLoader.load();

                Stage stage = (Stage) testCurrentTopic.getScene().getWindow();
                stage.setScene(new Scene(root, Main.WIDTH, Main.HEIGHT));
                stage.setTitle("Flashcard AI - Test");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (gameModePane.getSelectionModel().getSelectedItem().getText().equals("Topics") && topicsListView.getSelectionModel().getSelectedItem() != null){
            String selection = topicsListView.getSelectionModel().getSelectedItem(); // Default
            try {
                Main.currentDeck = selection;
                Main.currentGameMode = 1;

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/git7s/flashcardai/card-deck-view.fxml"));
                Parent root = fxmlLoader.load();

                Stage stage = (Stage) testCurrentTopic.getScene().getWindow();
                stage.setScene(new Scene(root, Main.WIDTH, Main.HEIGHT));
                stage.setTitle("Flashcard AI - Test");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (gameModePane.getSelectionModel().getSelectedItem().getText().equals("Targeted Testing")){
            if (strongestToggleButton.isSelected() || weakestToggleButton.isSelected()){
                try {
                    String[] studyData = resultManager.getBasicStudyData();

                    if (strongestToggleButton.isSelected()){
                        Main.currentDeck = studyData[0];
                        Main.currentGameMode = 2;

                        if (cardManager.searchCardsBySubject(Main.currentDeck).isEmpty()) {
                            return;
                        }
                    } else {
                        Main.currentDeck = studyData[1];
                        Main.currentGameMode = 2;

                        if (cardManager.searchCardsBySubject(Main.currentDeck).isEmpty()) {
                            return;
                        }
                    }
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/git7s/flashcardai/card-deck-view.fxml"));
                    Parent root = fxmlLoader.load();

                    Stage stage = (Stage) testCurrentTopic.getScene().getWindow();
                    stage.setScene(new Scene(root, Main.WIDTH, Main.HEIGHT));
                    stage.setTitle("Flashcard AI - Test");
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        else if (gameModePane.getSelectionModel().getSelectedItem().getText().equals("Random Testing")){
            if ((int) randomSlider.getValue() != 0) {
                try {

                    Main.currentDeck = Integer.toString((int) (randomSlider.getValue()-1));
                    Main.currentGameMode = 3;

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/git7s/flashcardai/card-deck-view.fxml"));
                    Parent root = fxmlLoader.load();

                    Stage stage = (Stage) testCurrentTopic.getScene().getWindow();
                    stage.setScene(new Scene(root, Main.WIDTH, Main.HEIGHT));
                    stage.setTitle("Flashcard AI - Test");
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
            stage.setScene(new Scene(root, Main.WIDTH, Main.HEIGHT));
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
        if (gameModePane.getSelectionModel().getSelectedItem().getText().equals("Subjects")){
            // Get selected subject and topic from UI
            String selectedSubject = subjectsListView.getSelectionModel().getSelectedItem();

            if (selectedSubject != null) {
                // Delete the flashcards under chosen subject
                cardManager.deleteSubject(selectedSubject);
                // Pull updated list of user's cards from the DB
                initialize();
            }
        }
        else if (gameModePane.getSelectionModel().getSelectedItem().getText().equals("Topics")){
            // Get selected subject and topic from UI
            String selectedSubject = subjectComboBox.getValue();
            String selectedTopic = topicsListView.getSelectionModel().getSelectedItem();

            if (selectedSubject != null && selectedTopic != null) {
                /// FIX ADD DELETE SUBJECT, DELETE TOPIC
                // Delete the flashcards under chosen subject and topic for the user
                cardManager.deleteTopic(selectedSubject, selectedTopic);
                // Pull updated list of user's cards from the DB
                initialize();
            }
        }



    }

    /**
     * This method opens a popup window for updating flashcards under a selected topic.
     * @implNote It also sets the selected topic as the active deck in Main before loading the new window.
     */
    @FXML
    private void handleUpdateFlashcards() {
        if (gameModePane.getSelectionModel().getSelectedItem().getText().equals("Subjects") && subjectsListView.getSelectionModel().getSelectedItem() != null){
            // Get selected subject and topic from UI
            String selectedSubject = subjectsListView.getSelectionModel().getSelectedItem();
            if (selectedSubject != null) {
                // Store current deck for use in the update view
                Main.currentDeck = selectedSubject;
                Main.currentGameMode = 0;
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
        else if (gameModePane.getSelectionModel().getSelectedItem().getText().equals("Topics") && topicsListView.getSelectionModel().getSelectedItem() != null){
            // Get selected subject and topic from UI
            String selectedSubject = subjectComboBox.getValue();
            String selectedTopic = topicsListView.getSelectionModel().getSelectedItem();

            if (selectedSubject != null && selectedTopic != null) {
                // Store current deck for use in the update view
                Main.currentDeck = selectedTopic;
                Main.currentGameMode = 1;
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



    }

    private void setPrefColours(String colour){
        String s = "-fx-background-color: " + colour + "; -fx-text-fill: white; -fx-font-weight: bold;";

        strongestToggleButton.setStyle(s);
        weakestToggleButton.setStyle(s);
        testCurrentTopic.setStyle(s);
        backButton.setStyle(s);
        createFlashCardsButton.setStyle(s);
        updateFlashcardsButton.setStyle(s);
        deleteFlashcardsButton.setStyle(s);

    }

    public void setSelected(String subject, String topic){
        this.firstTopicSelection = topic;
        this.firstSubjectSelection = subject;
    }
}