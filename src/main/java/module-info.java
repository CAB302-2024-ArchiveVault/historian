module com.example.historian {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.sql;
  requires java.desktop;
  requires org.xerial.sqlitejdbc;


  opens com.example.historian to javafx.fxml;
  exports com.example.historian;

  exports com.example.historian.auth;

  exports com.example.historian.models.account;
  exports com.example.historian.models.gallery;
  exports com.example.historian.models.location;
  exports com.example.historian.models.person;
  exports com.example.historian.models.photo;
  exports com.example.historian.models.tag;

  exports com.example.historian.utils;
}