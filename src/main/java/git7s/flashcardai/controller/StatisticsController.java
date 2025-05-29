package git7s.flashcardai.controller;

import git7s.flashcardai.Main;
import git7s.flashcardai.dao.CardDAO;
import git7s.flashcardai.dao.ResultDAO;
import git7s.flashcardai.dao.UserDAO;
import git7s.flashcardai.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

/**
 * This class controls the dashboard GUI
 */
public class StatisticsController {
    /**
     * This label is the progress text on the progress bar
     */
    @FXML public Label progressTextLabel;
    /**
     * Displays the name on entry
     */
    public Label welcomeLabel;

    public PieChart strongestPieChart;
    public PieChart weakestPieChart;
    public Button backButton;
    public Label progressLabel;
    public TabPane targetPane;
    public Label strongestSubjectLabel;
    public Label weakestSubjectLabel;
    public Label lifetimeScoreLabel;
    public ProgressBar lifetimeScoreBar;
    public Label currentStreakDisplay;
    public VBox strongestSubjectPane;
    public VBox weakestSubjectPane;
    public VBox weeklyScoresPane;
    public Label scoresLabel;
    public LineChart<Number, Number> scoresChart;
    public VBox weeklyVolumePane;
    public Label volumeLabel;
    public LineChart<Number, Number> volumeChart;
    public Pane choiceStatsPane;
    public VBox weeklySubjectVolume;
    public ComboBox<String> subjectVolumeComboBox;
    public LineChart<Number, Number> subjectVolumeChart;
    public VBox weeklySubjectScores;
    public ComboBox<String> subjectScoresComboBox;
    public LineChart<Number, Number> subjectScoresChart;
    public Button viewStrongestButton;
    public Button viewWeakestButton;
    public Button viewVolumeButton;
    public Button viewScoresButton;
    public Button viewSubjectVolume;
    public Button viewSubjectScores;
    /**
     * Strongest topic displayed
     */
    @FXML private Label strongestTopicLabel;
    /**
     * Weakest topic displayed
     */
    @FXML private Label weakestTopicLabel;
    /**
     * Result Manager for accessing DB
     */
    private ResultManager resultManager;
    /**
     * Card Manager for accessing DB
     */
    private UserManager userManager;
    /**
     * List that contains all the options for the subject dropdown menu
     */
    private ObservableList<String> subjects;
    /**
     * Initialise is run when the GUI is opened.
     */
    @FXML
    public void initialize() {
        //DB
        resultManager = new ResultManager(new ResultDAO());
        userManager = new UserManager(new UserDAO());
        // Get Study Data
        String[] studyData = resultManager.getBasicStudyData();
        subjects = FXCollections.observableArrayList();
        setPrefColours(Main.prefCol);
        setupInitialUI(studyData);
        setupOverallCharts(studyData);

    }

    public void handleBackToDashboard() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/git7s/flashcardai/dashboard-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root, Main.WIDTH, Main.HEIGHT));
            stage.setTitle("Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setupInitialUI(String[] studyData){
        //Setup the subject, topic panes
        for (Result result : resultManager.getByUserID(12345)) {
            if(subjects != null) {
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
        welcomeLabel.setText("Statistics for: " + userManager.getUser(Main.loggedInUserID).getFullName());
        lifetimeScoreLabel.setText("Your lifetime score is " + studyData[4] + "% correct!");
        lifetimeScoreBar.setProgress(Double.parseDouble(studyData[4])/100);

        strongestTopicLabel.setText("Strongest Subject: " + studyData[0]);
        weakestTopicLabel.setText("Weakest Subject: " + studyData[1]);

        currentStreakDisplay.setText(resultManager.getCurrentStreak() + " Days!");
    }

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


        List<XYChart.Series<Number, Number>> volumeChartData = resultManager.getVolumeChartData();
        for (XYChart.Series<Number, Number> e : volumeChartData) {
            volumeChart.getData().add(e);
        }
        volumeChart.setCreateSymbols(false);
        volumeChart.setTitle("Flashcards per Week");

        List<XYChart.Series<Number, Number>> scoresChartData = resultManager.getScoresChartData();
        for (XYChart.Series<Number, Number> e : scoresChartData) {
            scoresChart.getData().add(e);
        }
        scoresChart.setCreateSymbols(false);
        scoresChart.setTitle("Correct % per Week");

        handleUpdateSubjectScoreChart();
        handleUpdateSubjectVolumeChart();
    }

    public void handleUpdateSubjectVolumeChart() {
        List<XYChart.Series<Number, Number>> subjectVolumeData = resultManager.getSubjectVolumeData(subjectVolumeComboBox.getSelectionModel().getSelectedItem());
        for (XYChart.Series<Number, Number> e : subjectVolumeData) {
            subjectVolumeChart.getData().add(e);
        }
        subjectVolumeChart.setCreateSymbols(false);
        subjectVolumeChart.setTitle("Flashcards per Week");
    }

    public void handleUpdateSubjectScoreChart() {
        List<XYChart.Series<Number, Number>> subjectScoresData = resultManager.getSubjectScoresChartData(subjectScoresComboBox.getSelectionModel().getSelectedItem());
        for (XYChart.Series<Number, Number> e : subjectScoresData) {
            subjectScoresChart.getData().add(e);
        }
        subjectScoresChart.setCreateSymbols(false);
        subjectScoresChart.setTitle("Correct % per Week");
    }

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

    private void setPrefColours(String colour){
        String s = "-fx-background-color: " + colour + "; -fx-text-fill: white; -fx-font-weight: bold;";

        backButton.setStyle(s);
        viewScoresButton.setStyle(s);
        viewStrongestButton.setStyle(s);
        viewWeakestButton.setStyle(s);
        viewVolumeButton.setStyle(s);
        viewSubjectVolume.setStyle(s);
        viewSubjectScores.setStyle(s);

    }


}


