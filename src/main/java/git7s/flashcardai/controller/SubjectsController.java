package git7s.flashcardai.controller;

import git7s.flashcardai.AppDefaults;
import git7s.flashcardai.dao.CardDAO;
import git7s.flashcardai.dao.ResultDAO;
import git7s.flashcardai.model.Card;
import git7s.flashcardai.model.CardManager;
import git7s.flashcardai.model.ResultManager;
import git7s.flashcardai.service.GameService;
import git7s.flashcardai.service.SessionService;
import git7s.flashcardai.service.StatisticsService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
public class SubjectsController extends AbstractController implements IController {
    /**
     * Public default constructor.
     */
    public SubjectsController() {
    }
    /**
     * Piecharts
     */
    @FXML
    public PieChart strongestPieChart, weakestPieChart;
    /**
     * Tabpanes
     */
    @FXML
    public TabPane gameModePane;
    /**
     * Toggle Buttons
     */
    @FXML
    public ToggleButton strongestToggleButton, weakestToggleButton;
    /**
     * ListView
     */
    @FXML
    public ListView<String> subjectsListView, topicsListView;
    /**
     * Labels
     */
    @FXML
    public Label randomDescription;
    /**
     * Slider
     */
    @FXML
    public Slider randomSlider;
    /**
     * Buttons
     */
    @FXML
    public Button backButton, createFlashCardsButton, updateFlashcardsButton, deleteFlashcardsButton, testCurrentTopic;
    /**
     * This ComboBox contains all the user's subjects
     */
    @FXML
    private ComboBox<String> subjectComboBox;
    /**
     * List that contains all the options for the card dropdown menu
     */
    private List<Card> usersCards;
    /**
     * List that contains all the options for the subject dropdown menu
     */
    private ObservableList<String> subjects;
    /**
     * Card Manager
     */
    private CardManager cardManager;
    /**
     * Card Manager
     */
    private ResultManager resultManager;

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
        usersCards = cardManager.searchByUserID(SessionService.getInstance().loggedInID);
        subjects = FXCollections.observableArrayList();
        setupUIData();
    }

    /**
     * Setup the current UI
     */
    @Override
    public void setupUIData() {
        String[] studyData = StatisticsService.getInstance().getBasicStudyData();
        setPrefColours(backButton, deleteFlashcardsButton, updateFlashcardsButton, createFlashCardsButton, testCurrentTopic);

        setupSelectionModels();
        setupTargetTab(studyData);
        setupRandomTab();
        setupSubjectTopicTab();


    }
    /**
     * Back Button
     */
    @Override
    public void handleBackButton() {
        handleBackToDashboard();
    }
    /**
     *Handles the subject selection - if the user clicks a subject, load all the topics into the listview
     */
    @FXML
    private void handleSubjectSelection() {
        String selectedSubject = subjectComboBox.getValue();
        if (selectedSubject != null) {
            topicsListView.setItems(cardManager.topicSelection(selectedSubject));
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(AppDefaults.ViewPaths.CREATE_FLASHCARDS_VIEW.get()));
            Parent popupRoot = fxmlLoader.load();
            Stage popupStage = new Stage();
            popupStage.setTitle(AppDefaults.ViewTitles.CREATE_FLASHCARDS_VIEW.get());
            popupStage.setScene(new Scene(popupRoot));
            popupStage.initModality(Modality.APPLICATION_MODAL); // Blocks interaction with the My Subject view
            popupStage.showAndWait();
            //Refresh
            initialize();
        } catch (Exception e) {
            errorDialogue(e.getMessage());
        }
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
            GameService.getInstance().setCurrentGameMode(0);
            GameService.getInstance().setCurrentDeck(selection);
            changeView(AppDefaults.ViewTitles.TEST_VIEW, testCurrentTopic);
        }
        else if (gameModePane.getSelectionModel().getSelectedItem().getText().equals("Topics") && topicsListView.getSelectionModel().getSelectedItem() != null){
            String selection = topicsListView.getSelectionModel().getSelectedItem(); // Default
            GameService.getInstance().setCurrentGameMode(1);
            GameService.getInstance().setCurrentDeck(selection);
            changeView(AppDefaults.ViewTitles.TEST_VIEW, testCurrentTopic);
        }
        else if (gameModePane.getSelectionModel().getSelectedItem().getText().equals("Targeted Testing")){
            String[] studyData = StatisticsService.getInstance().getBasicStudyData();
            GameService.getInstance().setCurrentGameMode(2);

            if (strongestToggleButton.isSelected()){
                GameService.getInstance().setCurrentDeck(studyData[0]);
            } else {
                GameService.getInstance().setCurrentDeck(studyData[1]);
            }

            changeView(AppDefaults.ViewTitles.TEST_VIEW, testCurrentTopic);
        }
        else if (gameModePane.getSelectionModel().getSelectedItem().getText().equals("Random Testing")){
            String currDeck = Integer.toString((int) (randomSlider.getValue()-1));
            GameService.getInstance().setCurrentDeck(currDeck);
            GameService.getInstance().setCurrentGameMode(3);
            changeView(AppDefaults.ViewTitles.TEST_VIEW, testCurrentTopic);
        }
    }
    /**
     * This button takes the user back to the dashboard
     */
    @FXML
    private void handleBackToDashboard() {
        changeView(AppDefaults.ViewTitles.DASHBOARD_VIEW, backButton);
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
        if (gameModePane.getSelectionModel().getSelectedItem().getText().equals("Subjects")){
            // Get selected subject and topic from UI
            String selectedSubject = subjectsListView.getSelectionModel().getSelectedItem();
            if (selectedSubject != null) {
                // Store current deck for use in the update view
                GameService.getInstance().setCurrentGameMode(0);
                GameService.getInstance().setCurrentDeck(selectedSubject);
                try {
                    // 1. Load the update flashcards view
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(AppDefaults.ViewPaths.UPDATE_FLASHCARDS_VIEW.get()));
                    Parent root = fxmlLoader.load();

                    // 2. Create and configure new popup window
                    Stage popupStage = new Stage();
                    popupStage.setTitle(AppDefaults.ViewTitles.UPDATE_FLASHCARDS_VIEW.get());
                    popupStage.setScene(new Scene(root));
                    popupStage.initModality(Modality.APPLICATION_MODAL); // Prevent interaction with other windows
                    // 3. Show popup and pause background
                    popupStage.showAndWait();
                    // Refresh the current page once popup is closed
                    initialize();
                } catch (Exception e) {
                    errorDialogue(e.getMessage());
                }
            }
            errorDialogue(AppDefaults.ErrorMessages.NO_CARDS_NO_PLAY.get());
        }
        else if (gameModePane.getSelectionModel().getSelectedItem().getText().equals("Topics")){
            // Get selected subject and topic from UI
            String selectedSubject = subjectComboBox.getValue();
            String selectedTopic = topicsListView.getSelectionModel().getSelectedItem();

            if (selectedSubject != null && selectedTopic != null) {
                // Store current deck for use in the update view
                GameService.getInstance().setCurrentGameMode(1);
                GameService.getInstance().setCurrentDeck(selectedTopic);
                try {
                    // 1. Load the update flashcards view
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(AppDefaults.ViewPaths.UPDATE_FLASHCARDS_VIEW.get()));
                    Parent root = fxmlLoader.load();
                    // 2. Create and configure new popup window
                    Stage popupStage = new Stage();
                    popupStage.setTitle(AppDefaults.ViewTitles.UPDATE_FLASHCARDS_VIEW.get());
                    popupStage.setScene(new Scene(root));
                    popupStage.initModality(Modality.APPLICATION_MODAL); // Prevent interaction with other windows
                    // 3. Show popup and pause background
                    popupStage.showAndWait();
                    // Refresh the current page once popup is closed
                    initialize();
                } catch (Exception e) {
                    errorDialogue(e.getMessage());
                }
            }
            errorDialogue(AppDefaults.ErrorMessages.NO_CARDS_NO_PLAY.get());
        }

    }
    /**
     * Makes the UI more interactive with colours changing on clicks
     */
    public void setupSelectionModels() {

        if (usersCards.isEmpty()) {
            for (Tab tab : gameModePane.getTabs()) {
                tab.setDisable(true);
            }
        } else {
            gameModePane.getSelectionModel().selectFirst();
        }

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
    }

    private void setupTargetTab(String[] studyData){
        if (!resultManager.getByUserID(SessionService.getInstance().loggedInID).isEmpty()) {
            ToggleGroup targetGroup = new ToggleGroup();
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
        } else {
            weakestPieChart.setVisible(false);
            strongestPieChart.setVisible(false);
            strongestToggleButton.setVisible(false);
            weakestToggleButton.setVisible(false);

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
    }

    /**
     * Set up the Random Gamemode Pane
     */
    private void setupRandomTab(){
        randomSlider.setMax(usersCards.size());
        randomSlider.setMin(1);
        randomSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            randomDescription.setText("You will test " + newVal.intValue() + " random flashcards");
        });
    }

    private void setupSubjectTopicTab(){
        //Setup the subject, topic
        for (Card card : usersCards) {
            if(!subjects.isEmpty()) {
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
            subjectComboBox.getSelectionModel().selectFirst();
            handleSubjectSelection();
            }
    }
}