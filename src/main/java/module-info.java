module git7sproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.google.gson;
    requires java.sql;


    opens git7s.flashcardai to javafx.fxml;
    exports git7s.flashcardai;
    exports git7s.flashcardai.dao;
    opens git7s.flashcardai.dao to javafx.fxml;
    exports git7s.flashcardai.controller;
    opens git7s.flashcardai.controller to javafx.fxml;
    exports git7s.flashcardai.model;
    opens git7s.flashcardai.model to javafx.fxml;
    exports git7s.flashcardai.llm;
    opens git7s.flashcardai.llm to javafx.fxml;
}