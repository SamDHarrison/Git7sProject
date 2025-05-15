package git7s.flashcardai.model;

import git7s.flashcardai.dao.CardDAO;
import git7s.flashcardai.dao.DatabaseConnection;
import git7s.flashcardai.dao.ResultDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * The DAO object for interacting with the Results Table over the specified connection
 */
public class ResultManager {
    /**
     * Local DAO object that is filled by calling screen
     */
    private ResultDAO resultDAO;
    /**
     * The Constructor which takes the caller's DAO object and updates local
     *
     * @param resultDAO
     */
    public ResultManager(ResultDAO resultDAO) {
        this.resultDAO = resultDAO;
    }
    /**
     * Search Function that gets a list of results for handling GUI-side
     */
    public List<Result> searchResultsBySubject(String subject) {
        return resultDAO.getAll()
                .stream()
                .filter(result -> (result.getSubject().equalsIgnoreCase(subject)))
                .toList();
    }
    /**
     * Search Function that gets a list of results for handling GUI-side
     */
    public List<Result> searchResultsByTopic(String topic) {
        return resultDAO.getAll()
                .stream()
                .filter(result -> (result.getTopic().equalsIgnoreCase(topic)))
                .toList();
    }
    /**
     * Inserts a Result to the db
     * @param result New Result for insertion
     */
    public void addResult(Result result){
        resultDAO.insert(result);
    }

    /**
     * Deletes the specified result
     * @param resultID Specified result
     */
    public void delete(int resultID){
        resultDAO.delete(resultID);
    }

    /**
     * Gets results for a specified Card
     * @param cardIDQuery Specified Card
     * @return List of results
     */
    public List<Result> getByCardID(int cardIDQuery){
        return resultDAO.getAll()
                .stream()
                .filter(result -> result.getCardID() == cardIDQuery).
                toList();
    }
    /**
     * Pulls all db results
     * @return List of results
     */
    public List<Result> getAll(){
        return resultDAO.getAll();
    }
    /**
     * Gets results by the user who got the results
     * @param userIDQuery The user ID
     * @return List of results
     */
    public List<Result> getByUserID(int userIDQuery){
        return resultDAO.getAll()
                .stream()
                .filter(result -> result.getUserID() == userIDQuery).
                toList();
    }
    /**
     * Gets results by the user who got the results
     * @param start Starting Date (Timestamp)
     * @param end ending Date (Timestamp)
     * @return List of results
     */
    public List<Result> getByTimeFrame(Timestamp start, Timestamp end){
        return resultDAO.getAll()
                .stream()
                .filter(result -> (result.getAt().after(start) && result.getAt().before(end))).
                toList();
    }
}
