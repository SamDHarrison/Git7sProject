package git7s.flashcardai.controller;

/**
 * IController Interface Specifies minimum requirements for a Controller Class
 * This will ensure that code readability and design principles are parallel between developers.
 */
public interface IController {
    /**
     * Specify all controller must implement a initialize method to maintain refresh functions, setup functions etc.
     */
    void initialize();
    /**
     * Specify all controller must implement a setupIUData method that sets up the data filling its buttons / labels etc
     */
    void setupUIData();
    /**
     * Specify all controller must implement a handleBackButton method to ensure that all controllers can move the scene
     */
    void handleBackButton();

}
