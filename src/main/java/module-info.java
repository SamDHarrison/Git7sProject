module git7sproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.net.http;


    opens git7s.flashcardai to javafx.fxml;
    exports git7s.flashcardai;
    exports git7s.flashcardai.dao;
    opens git7s.flashcardai.dao to javafx.fxml;
}