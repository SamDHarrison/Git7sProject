package git7s.flashcardai.model;

import git7s.flashcardai.dao.ResultDAO;

import java.util.*;
/**
 * The DAO object for interacting with the Results Table over the specified connection
 */
public class ResultManager {
    /**
     * Local DAO object that is filled by calling screen
     */
    private final ResultDAO resultDAO;
    /**
     * The Constructor which takes the caller's DAO object and updates local
     *
     * @param resultDAO DAO Object
     */
    public ResultManager(ResultDAO resultDAO) {
        this.resultDAO = resultDAO;
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
}
