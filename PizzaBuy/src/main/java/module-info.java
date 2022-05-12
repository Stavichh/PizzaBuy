module com.example.pizzabuy {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.pizzabuy to javafx.fxml;
    exports com.example.pizzabuy;
}