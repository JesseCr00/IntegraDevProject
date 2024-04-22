package org.openjfx.familytreeapplication;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import org.openjfx.familytreeapplication.database.SQLiteJDBC;
import org.openjfx.familytreeapplication.pages.FamilyTreeProfile;

public class FamilyTreeApplication extends Application {

  private static final SQLiteJDBC database = new SQLiteJDBC();

  public static SQLiteJDBC getDatabase() {
    return database;
  }

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage stage) throws IOException {
    FamilyTreeProfile familyTreeProfile = new FamilyTreeProfile();
    familyTreeProfile.showProfile(stage);
    stage.setTitle("Family Tree Application");
    stage.show();
  }
}