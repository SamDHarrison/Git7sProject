package git7s.flashcardai.service;

import git7s.flashcardai.dao.CardDAO;
import git7s.flashcardai.dao.ResultDAO;
import git7s.flashcardai.model.Card;
import git7s.flashcardai.model.CardManager;
import git7s.flashcardai.model.Result;
import git7s.flashcardai.model.ResultManager;

import java.sql.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class AdminService {
    /**
     * Protected constructor - cannot be instantiated
     */
    protected AdminService() {}
    /**
     * Subclass which acts as container
     */
    private static class Admin {
        private static final AdminService INSTANCE = new AdminService();
    }

    /**
     * getInstance returns the static INSTANCE of AdminService
     * @return INSTANCE
     */
    public static AdminService getInstance() {
        return AdminService.Admin.INSTANCE;
    }
    /**
     * The Admin Password used to authenticate a user
     */
    private static final String adminPassword = "Admin";

    /**
     * Uses the Private Admin Password to authenticate an admin request
     * @param input Request Input
     * @return Boolean
     */
    public boolean handleAdminRequest(String input) {
        return input.equals(adminPassword);
    }

    /**
     * Generates fake results for the user
     * @param userID Requested User for Generation
     * @param weeksAgo How many weeks ago the results span
     */
    public void generateFakeResults(int userID, int weeksAgo) {
        ResultManager resultManager = new ResultManager(new ResultDAO());
        CardManager cardManager = new CardManager(new CardDAO());
        List<Card> cards = cardManager.searchByUserID(userID);

        Timestamp now = new Timestamp(System.currentTimeMillis());
        Random random = new Random();

        //Assign Randomness
        HashMap<String, Integer> strengthRandMap = new HashMap<>();
        for (Card r : cards) {
            if (!strengthRandMap.containsKey(r.getSubject())) {
                strengthRandMap.put(r.getSubject(), random.nextInt(10));
            }
        }

        for (int daysAgo = 0; daysAgo < weeksAgo*7; daysAgo++) {
            // Create a calendar instance and set it to the current timestamp
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);

            // Subtract (i + 1) days from the current timestamp
            cal.add(Calendar.DATE, -(daysAgo + 1));

            // Convert back to Timestamp
            Timestamp pastTimestamp = new Timestamp(cal.getTimeInMillis());

            //Get "weekCount" which shows the increase in trend over time
            long diffInMillis = now.getTime() - pastTimestamp.getTime();
            int weekCount = (int) (diffInMillis / (7 * 24 * 60 * 60 * 1000));
            int strengthFactor = 10 - (int) ((weekCount / (double) weeksAgo) * (weeksAgo / 2.0));

            //Assign Cards Trending over time
            for (Card r : cards) {

                boolean res = (strengthRandMap.get(r.getSubject()) * strengthFactor  > random.nextInt(100));
                resultManager.addResult(new Result(r.getUserID(), r.getCardID(), pastTimestamp, res, r.getSubject(), r.getTopic()));

            }
        }
    }

    /**
     * Admin Direct Access to SQLite & Database
     * @param sql SQL Statement
     * @throws SQLException If statement is poor / incorrect
     */
    public void directDataBase(String sql) throws SQLException {
        try {
            Statement statement = DatabaseService.getInstance().getConnection().createStatement();
            boolean result = statement.execute(sql);

            if (result) {
                try {
                    ResultSet rs = statement.getResultSet();
                    // Get metadata to determine column count and column names.
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    // Print the column headers.
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.print(metaData.getColumnName(i) + "\t");
                    }
                    System.out.println();

                    // Iterate through all rows of the ResultSet.
                    while (rs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            // Get each column value as a String.
                            String value = rs.getString(i);
                            System.out.print(value + "\t");
                        }
                        // End of row.
                        System.out.println();
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

            }
        } catch (SQLException e) {
            System.out.println("SQL Inject Failed: " + e.getMessage());
        }

    }
}
