module com.example.addressbook.addressbook {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.addressbook.addressbook to javafx.fxml;
    exports com.example.addressbook.addressbook;
}