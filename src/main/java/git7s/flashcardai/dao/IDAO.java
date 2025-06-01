package git7s.flashcardai.dao;
/**
 * IDAO Specifies the Minimum Requirements for ANY DAO class.
 * @param <T> Type of object which is implemented by the DAO class
 */
public interface IDAO<T> {
    /**
     * Default Create Table Method
     */
    void createTable();
    /**
     * Default Insert
     * @param Entity Entity
     */
    void insert(T Entity);
    /**
     * Default Update
     * @param Entity Entity
     */
    void update(T Entity);
    /**
     * Default Delete
     * @param ID Int
     */
    void delete(int ID);
    /**
     * Default Get
     * @param ID Int
     * @return T Entity
     */
    T getByID(int ID);
}
