package org.openjfx.familytreeapplication.pages;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.openjfx.familytreeapplication.FamilyTreeApplication;

public class FamilyTreeProfile {

  private final Scene profileScene;

  public FamilyTreeProfile() throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(
        FamilyTreeApplication.class.getResource("profile-view.fxml"));
    profileScene = new Scene(fxmlLoader.load(), 640, 400);
  }

  public void showProfile(Stage stage) {
    stage.setScene(profileScene);
  }
}
