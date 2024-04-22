module FamilyTreeApplication {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.graphics;
  requires java.sql;
  requires sqlite.jdbc;

  opens org.openjfx.familytreeapplication to javafx.fxml;
  exports org.openjfx.familytreeapplication;
  exports org.openjfx.familytreeapplication.controllers;
  opens org.openjfx.familytreeapplication.controllers to javafx.fxml;
  exports org.openjfx.familytreeapplication.database;
  opens org.openjfx.familytreeapplication.database to javafx.fxml;
  exports org.openjfx.familytreeapplication.pages;
  opens org.openjfx.familytreeapplication.pages to javafx.fxml;
}