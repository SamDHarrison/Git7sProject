package git7s.flashcardai.dao;
/**
 * IDAO Specifies the Minimum Requirements for ANY DAO class.
 */
public interface IDAO<T> {
    /**
     * Default Create Table Method
     */
    void createTable();
    /**
     * Default Insert
     */
    void insert(T Entity);
    /**
     * Default Update
     */
    void update(T Entity);
    /**
     * Default Delete
     */
    void delete(int ID);
    /**
     * Default Get
     */
    T getByID(int ID);
}
