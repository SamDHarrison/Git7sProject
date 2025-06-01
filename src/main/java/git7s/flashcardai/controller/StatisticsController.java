package git7s.flashcardai.controller;

import git7s.flashcardai.AppDefaults;
import git7s.flashcardai.dao.ResultDAO;
import git7s.flashcardai.dao.UserDAO;
import git7s.flashcardai.model.*;
import git7s.flashcardai.service.SessionService;
import git7s.flashcardai.service.StatisticsService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.util.List;

/**
 * This class controls the dashboard GUI
 */
public class StatisticsController extends AbstractController implements IController{
    /**
     * Public default constructor.
     */
    public StatisticsController() {
    }
    /**
     * Labels
     */
    @FXML
    public Label currentStreakDisplay, scoresLabel, volumeLabel, lifetimeScoreLabel, welcomeLabel, strongestTopicLabel, weakestTopicLabel;
    /**
     * Pie Charts
     */
    @FXML
    public PieChart strongestPieChart, weakestPieChart;
    /**
     * Progress Bar
     */
    @FXML
    public ProgressBar lifetimeScoreBar;
    /**
     * Pane
     */
    @FXML
    public Pane choiceStatsPane;
    /**
     * VBOX
     */
    @FXML
    public VBox weeklySubjectVolume, weeklySubjectScores, weeklyVolumePane, weeklyScoresPane, weakestSubjectPane, strongestSubjectPane;
    /**
     * ComboBox
     */
    @FXML
    public ComboBox<String> subjectVolumeComboBox, subjectScoresComboBox;
    /**
     * Line Chart
     */
    @FXML
    public LineChart<Number, Number> subjectScoresChart, subjectVolumeChart, scoresChart, volumeChart;
    /**
     * Buttons
     */
    @FXML
    public Button viewStrongestButton, viewWeakestButton, viewVolumeButton, viewScoresButton, viewSubjectVolume, viewSubjectScores, backButton;
    /**
     * Result Manager for accessing DB
     */
    @FXML
    private ResultManager resultManager;
    /**
     * Card Manager for accessing DB
     */
    @FXML
    private UserManager userManager;
    /**
     * List that contains all the options for the subject dropdown menu
     */
    @FXML
    private ObservableList<String> subjects;
    /**
     * Initialise is run when the GUI is opened.
     */
    @FXML
    public void initialize() {
        //DB
        resultManager = new ResultManager(new ResultDAO());
        userManager = new UserManager(new UserDAO());
        setupUIData();
    }

    /**
     * Fill UI
     */
    @Override
    public void setupUIData() {
        // Get Study Data
        String[] studyData = StatisticsService.getInstance().getBasicStudyData();
        subjects = FXCollections.observableArrayList();
        setPrefColours(viewStrongestButton, viewWeakestButton, viewVolumeButton, viewScoresButton, viewSubjectVolume, viewSubjectScores, backButton);
        setupExtraUI(studyData);
        setupOverallCharts(studyData);
    }

    /**
     * Go back to Dashboard
     */
    @Override
    public void handleBackButton() {
        handleBackToDashboard();
    }

    /**
     * Go back to Dashboard
     */
    public void handleBackToDashboard() {
        changeView(AppDefaults.ViewTitles.DASHBOARD_VIEW, backButton);
    }

    /**
     * Setup other UI
     * @param studyData Stats
     */
    public void setupExtraUI(String[] studyData){
        //Setup the subject, topic panes
        for (Result result : resultManager.getByUserID(SessionService.getInstance().loggedInID)) {
            if(!subjects.isEmpty()) {
                if (!subjects.contains(result.getSubject())) {
                    subjects.add(result.getSubject());
                }
            } else {
                subjects.add(result.getSubject());
            }
        }

        subjectScoresComboBox.setItems(subjects);
        subjectScoresComboBox.getSelectionModel().selectFirst();
        subjectVolumeComboBox.setItems(subjects);
        subjectVolumeComboBox.getSelectionModel().selectFirst();

        // Setting General Data
        welcomeLabel.setText("Statistics for: " + userManager.getUser(SessionService.getInstance().loggedInID).getFullName());
        lifetimeScoreLabel.setText("Your lifetime score is " + studyData[4] + "% correct!");
        lifetimeScoreBar.setProgress(Double.parseDouble(studyData[4])/100);

        strongestTopicLabel.setText("Strongest Subject: " + studyData[0]);
        weakestTopicLabel.setText("Weakest Subject: " + studyData[1]);

        currentStreakDisplay.setText(StatisticsService.getInstance().getCurrentStreak() + " Days!");
    }

    /**
     * Set up the charts
     * @param studyData Stats
     */
    public void setupOverallCharts(String[] studyData){
        //Setup Panes
        strongestPieChart.setData(FXCollections.observableArrayList(
                new PieChart.Data("Correct %", Integer.parseInt(studyData[2])),
                new PieChart.Data("Incorrect %", 100-Integer.parseInt(studyData[2]))
        ));

        weakestPieChart.setData(FXCollections.observableArrayList(
                new PieChart.Data("Correct %", Integer.parseInt(studyData[3])),
                new PieChart.Data("Incorrect %", 100-Integer.parseInt(studyData[3]))
        ));

        strongestPieChart.setLabelsVisible(true);
        weakestPieChart.setLabelsVisible(true);


        List<XYChart.Series<Number, Number>> volumeChartData = StatisticsService.getInstance().getVolumeChartData();
        for (XYChart.Series<Number, Number> e : volumeChartData) {
            volumeChart.getData().add(e);
        }
        volumeChart.setCreateSymbols(false);
        volumeChart.setTitle("Flashcards per Week");

        List<XYChart.Series<Number, Number>> scoresChartData = StatisticsService.getInstance().getScoresChartData();
        for (XYChart.Series<Number, Number> e : scoresChartData) {
            scoresChart.getData().add(e);
        }
        scoresChart.setCreateSymbols(false);
        scoresChart.setTitle("Correct % per Week");

        handleUpdateSubjectScoreChart();
        handleUpdateSubjectVolumeChart();
    }

    /**
     * Updates the chart
     */
    public void handleUpdateSubjectVolumeChart() {
        List<XYChart.Series<Number, Number>> subjectVolumeData = StatisticsService.getInstance().getSubjectVolumeData(subjectVolumeComboBox.getSelectionModel().getSelectedItem());
        for (XYChart.Series<Number, Number> e : subjectVolumeData) {
            subjectVolumeChart.getData().add(e);
        }
        subjectVolumeChart.setCreateSymbols(false);
        subjectVolumeChart.setTitle("Flashcards per Week");
    }
    /**
     * Updates the chart
     */
    public void handleUpdateSubjectScoreChart() {
        List<XYChart.Series<Number, Number>> subjectScoresData = StatisticsService.getInstance().getSubjectScoresChartData(subjectScoresComboBox.getSelectionModel().getSelectedItem());
        for (XYChart.Series<Number, Number> e : subjectScoresData) {
            subjectScoresChart.getData().add(e);
        }
        subjectScoresChart.setCreateSymbols(false);
        subjectScoresChart.setTitle("Correct % per Week");
    }
    /**
     * Selects the specific pane
     */
    public void viewStrongestPane() {
        // Hide all child VBoxes
        for (Node node : choiceStatsPane.getChildren()) {
            if (node instanceof VBox) {
                node.setVisible(false);
            }
        }
        // Show only the specific VBox
        strongestSubjectPane.setVisible(true);
    }
    /**
     * Selects the specific pane
     */
    public void viewWeakestPane() {
        // Hide all child VBoxes
        for (Node node : choiceStatsPane.getChildren()) {
            if (node instanceof VBox) {
                node.setVisible(false);
            }
        }
        // Show only the specific VBox
        weakestSubjectPane.setVisible(true);
    }
    /**
     * Selects the specific pane
     */
    public void viewVolumePane() {
        // Hide all child VBoxes
        for (Node node : choiceStatsPane.getChildren()) {
            if (node instanceof VBox) {
                node.setVisible(false);
            }
        }
        // Show only the specific VBox
        weeklyVolumePane.setVisible(true);
    }
    /**
     * Selects the specific pane
     */
    public void viewScoresPane() {
        // Hide all child VBoxes
        for (Node node : choiceStatsPane.getChildren()) {
            if (node instanceof VBox) {
                node.setVisible(false);
            }
        }
        // Show only the specific VBox
        weeklyScoresPane.setVisible(true);
    }
    /**
     * Selects the specific pane
     */
    public void viewSubjectVolumePane() {
        // Hide all child VBoxes
        for (Node node : choiceStatsPane.getChildren()) {
            if (node instanceof VBox) {
                node.setVisible(false);
            }
        }
        // Show only the specific VBox
        weeklySubjectVolume.setVisible(true);
    }
    /**
     * Selects the specific pane
     */
    public void viewSubjectScoresPane() {
        // Hide all child VBoxes
        for (Node node : choiceStatsPane.getChildren()) {
            if (node instanceof VBox) {
                node.setVisible(false);
            }
        }
        // Show only the specific VBox
        weeklySubjectScores.setVisible(true);
    }
}


