module git7sproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens git7s.flashcardai to javafx.fxml;
    exports git7s.flashcardai;
}