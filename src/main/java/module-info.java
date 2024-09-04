module com.example.historian {
    requires javafx.controls;
    requires javafx.fxml;
  requires java.sql;


  opens com.example.historian to javafx.fxml;
    exports com.example.historian;
}