package git7s.flashcardai.service;
import git7s.flashcardai.dao.ResultDAO;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import git7s.flashcardai.model.Result;
import git7s.flashcardai.model.ResultManager;
import javafx.scene.chart.XYChart;

/**
 * The Manager object for statistics data calculation
 */
public class StatisticsService {
    /**
     * Protected constructor - cannot be instantiated
     */
    protected StatisticsService() {
        resultManager = new ResultManager(new ResultDAO());
    }
    /**
     * Subclass which acts as session container
     */
    private static class Statistics {
        private static final StatisticsService INSTANCE = new StatisticsService();
    }
    /**
     * getInstance returns the static INSTANCE of SessionService
     * @return INSTANCE
     */
    public static StatisticsService getInstance() {
        return StatisticsService.Statistics.INSTANCE;
    }

    /**
     * Local DAO object that is filled by calling screen
     */
    private final ResultManager resultManager;

    /**
     * Generates a String[] that contains basic results data.
     * @return String[] of study data. 0: Strongest Topic, 1: Weakest Topic, 2: Correct/Incorrect Ratio Strongest, 3: Ratio Weakest, 4:Ratio overall
     */
    public String[] getBasicStudyData(){
        String[] studyData = new String[5];

        HashMap<String, Integer> correctCounter = new HashMap<>();
        HashMap<String, Integer> incorrectCounter = new HashMap<>();

        int correctPercent;
        int strongestPercent = 0;
        int weakestPercent = 0;
        String strongestSubject = "Unknown";
        String weakestSubject = "Unknown";
        int strongestSubjectCount = 0;
        int weakestSubjectCount = 0;
        double totalCorrect = 0;
        double totalIncorrect = 0;


        //Get Results
        for (Result result : resultManager.getByUserID(SessionService.getInstance().loggedInID)) {
            /// Correct
            if (result.isCorrect()){
                totalCorrect++;
                if (correctCounter.containsKey(result.getSubject())){
                    Integer count = correctCounter.get(result.getSubject());
                    correctCounter.replace(result.getSubject(), count + 1);
                    if (count > strongestSubjectCount) {
                        strongestSubject = result.getSubject();
                        strongestSubjectCount = count;
                    }
                }
                else {
                    correctCounter.put(result.getSubject(), 1);
                    if (strongestSubjectCount < 1) {
                        strongestSubject = result.getSubject();
                        strongestSubjectCount = 1;
                    }
                }
            }
            /// Incorrect
            else {
                totalIncorrect++;
                if (incorrectCounter.containsKey(result.getSubject())){
                    Integer count = incorrectCounter.get(result.getSubject());
                    incorrectCounter.replace(result.getSubject(), count + 1);
                    if (count > weakestSubjectCount) {
                        weakestSubject = result.getSubject();
                        weakestSubjectCount = count;
                    }

                } else {
                    incorrectCounter.put(result.getSubject(), 1);
                    if (weakestSubjectCount < 1) {
                        weakestSubject = result.getSubject();
                        weakestSubjectCount = 1;
                    }
                }
            }
        }


        try {
            correctPercent = (int) (totalCorrect/(totalCorrect+totalIncorrect)*100);
            if (!correctCounter.isEmpty() && !incorrectCounter.isEmpty()) {
                strongestPercent = (int) ((double)correctCounter.get(strongestSubject) / ((double)correctCounter.get(strongestSubject) + incorrectCounter.get(strongestSubject))*100);
                weakestPercent = (int) ((double)correctCounter.get(weakestSubject) / ((double)correctCounter.get(weakestSubject) + incorrectCounter.get(weakestSubject))*100);
            }


        } catch (ArithmeticException e){
            correctPercent = 0;
            strongestPercent = 0;
        }


        studyData[0] = strongestSubject;
        studyData[1] = weakestSubject;
        studyData[2] = String.valueOf(strongestPercent);
        studyData[3] = String.valueOf(weakestPercent);
        studyData[4] = String.valueOf(correctPercent);

        return studyData;
    }
    /**
     * Returns graph data for overall volume
     * @return Chart Data
     */
    public List<XYChart.Series<Number, Number>> getVolumeChartData() {
        XYChart.Series<Number, Number> overallData = new XYChart.Series<>();
        overallData.setName("Overall Flashcards");

        List<Result> allResults = resultManager.getByUserID(SessionService.getInstance().loggedInID);

        Map<Integer, Integer> weeksAgoCounts = new HashMap<>();

        for (Result result : allResults) {
            Timestamp timestamp = result.getAt();
            long currentTimeMillis = System.currentTimeMillis();
            long diffInMillis = currentTimeMillis - timestamp.getTime();
            int weekCount = (int) (diffInMillis / (7L * 24 * 60 * 60 * 1000)); // L ensures long multiplication

            weeksAgoCounts.put(weekCount, weeksAgoCounts.getOrDefault(weekCount, 0) + 1);
        }

        List<Integer> weeks = new ArrayList<>(weeksAgoCounts.keySet());
        Collections.sort(weeks);

        for (Integer week : weeks) {
            overallData.getData().add(new XYChart.Data<>(-week, weeksAgoCounts.get(week)));
        }

        List<XYChart.Series<Number, Number>> allData = new ArrayList<>();
        allData.add(overallData);
        return allData;
    }
    /**
     * Returns graph data for a subjects volume
     * @param subject String
     * @return Chart Data
     */
    public List<XYChart.Series<Number, Number>> getSubjectVolumeData(String subject) {
        List<XYChart.Series<Number, Number>> allData = new ArrayList<>();
        List<Result> allResults = resultManager.getByUserID(SessionService.getInstance().loggedInID);

        XYChart.Series<Number, Number> singleData = new XYChart.Series<>();
        HashMap<Integer, Integer> weeksAgo = new HashMap<>();

        for (Result result : allResults) {
            if (result.getSubject().equals(subject)) {
                Timestamp timestamp = result.getAt();
                long currentTimeMillis = System.currentTimeMillis();
                long diffInMillis = currentTimeMillis - timestamp.getTime();

                int weekCount = (int) (diffInMillis / (7 * 24 * 60 * 60 * 1000));

                weeksAgo.put(weekCount, weeksAgo.getOrDefault(weekCount, 0) + 1);
            }
        }

        for (Map.Entry<Integer, Integer> entry : weeksAgo.entrySet()) {
            singleData.getData().add(new XYChart.Data<>(-entry.getKey(), entry.getValue()));
        }
        singleData.setName(subject);
        allData.add(singleData);

        return allData;
    }
    /**
     * Returns graph data for an overall scores
     * @return Chart Data
     */
    public List<XYChart.Series<Number, Number>> getScoresChartData() {
        List<XYChart.Series<Number, Number>> allData = new ArrayList<>();
        List<Result> allResults = resultManager.getByUserID(SessionService.getInstance().loggedInID);

        // Collect all unique subjects from the results.
        HashSet<String> subjects = new HashSet<>();
        for (Result result : allResults) {
            subjects.add(result.getSubject());
        }

        // Process each subject separately.
        for (String subject : subjects) {
            // Two maps: one to store the total number of results per week,
            // and another to store the count of correct results.
            HashMap<Integer, Integer> totalPerWeek = new HashMap<>();
            HashMap<Integer, Integer> correctPerWeek = new HashMap<>();

            for (Result result : allResults) {
                if (result.getSubject().equals(subject)) {
                    Timestamp timestamp = result.getAt();
                    long diffInMillis = System.currentTimeMillis() - timestamp.getTime();

                    // Calculate how many weeks ago the result occurred.
                    int weekCount = (int) (diffInMillis / (7 * 24 * 60 * 60 * 1000));

                    // Increment the total count for the week.
                    totalPerWeek.put(weekCount, totalPerWeek.getOrDefault(weekCount, 0) + 1);

                    // If the result is correct, increment the correct count.
                    if (result.isCorrect()) {
                        correctPerWeek.put(weekCount, correctPerWeek.getOrDefault(weekCount, 0) + 1);
                    }
                }
            }

            // Create the chart series for this subject.
            XYChart.Series<Number, Number> series = new XYChart.Series<>();

            // Sorting the week keys ensures that your x-axis data is ordered.
            List<Integer> weeks = new ArrayList<>(totalPerWeek.keySet());
            Collections.sort(weeks);

            // For every week, calculate the percentage of correct responses.
            for (Integer week : weeks) {
                int total = totalPerWeek.get(week);
                int correct = correctPerWeek.getOrDefault(week, 0);
                int percentage = (int) Math.round((correct * 100.0) / total);
                series.getData().add(new XYChart.Data<>(-week, percentage));
            }

            // Label the series with the subject name.
            series.setName(subject);
            allData.add(series);
        }

        return allData;
    }
    /**
     * Returns graph data for a subjects scores
     * @param subject String
     * @return Chart Data
     */
    public List<XYChart.Series<Number, Number>> getSubjectScoresChartData(String subject) {
        List<XYChart.Series<Number, Number>> allData = new ArrayList<>();
        List<Result> allResults = resultManager.getByUserID(SessionService.getInstance().loggedInID);

        // These maps keep track of the total results and correct results per week.
        Map<Integer, Integer> totalPerWeek = new HashMap<>();
        Map<Integer, Integer> correctPerWeek = new HashMap<>();

        // Iterate over all results and only process those matching the specified subject.
        for (Result result : allResults) {
            if (result.getSubject().equals(subject)) {
                Timestamp timestamp = result.getAt();
                long currentTimeMillis = System.currentTimeMillis();
                long diffInMillis = currentTimeMillis - timestamp.getTime();

                // Calculate the week offset (number of weeks ago)
                int weekCount = (int) (diffInMillis / (7 * 24 * 60 * 60 * 1000));

                // Count total results for that week.
                totalPerWeek.put(weekCount, totalPerWeek.getOrDefault(weekCount, 0) + 1);
                // Count only correct results.
                if (result.isCorrect()) {
                    correctPerWeek.put(weekCount, correctPerWeek.getOrDefault(weekCount, 0) + 1);
                }
            }
        }

        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        // Sorting the weeks ensures the data points are in order along the x-axis.
        List<Integer> weeks = new ArrayList<>(totalPerWeek.keySet());
        Collections.sort(weeks);

        // For each week, calculate the percentage of correct answers.
        for (Integer week : weeks) {
            int total = totalPerWeek.get(week);
            int correct = correctPerWeek.getOrDefault(week, 0);
            int percentage = (int) Math.round((correct * 100.0) / total);
            series.getData().add(new XYChart.Data<>(-week, percentage));
        }

        series.setName(subject);
        allData.add(series);

        return allData;
    }
    /**
     * Returns the current users streak
     * @return Int Streak
     */
    public int getCurrentStreak(){
        List<Result> results = resultManager.getByUserID(SessionService.getInstance().loggedInID);
        Set<LocalDate> resultDates = new HashSet<>();
        for (Result r : results) {
            Timestamp timestamp = r.getAt();
            if (timestamp != null) {
                // Convert Timestamp to LocalDate
                LocalDate date = timestamp.toLocalDateTime().toLocalDate();
                resultDates.add(date);
            }
        }

        int streak = 0;
        LocalDate currentDate = LocalDate.now(); // starting point: today

        while (resultDates.contains(currentDate)) {
            streak++;
            currentDate = currentDate.minusDays(1);
        }

        return streak;
    }

}
