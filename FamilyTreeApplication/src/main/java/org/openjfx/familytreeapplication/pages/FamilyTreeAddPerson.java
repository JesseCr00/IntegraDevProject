package org.openjfx.familytreeapplication.pages;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.openjfx.familytreeapplication.FamilyTreeApplication;

public class FamilyTreeAddPerson {

  private final Scene addPersonScene;

  public FamilyTreeAddPerson() throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(
        FamilyTreeApplication.class.getResource("add-person-view.fxml"));
    addPersonScene = new Scene(fxmlLoader.load(), 640, 400);
  }

  public void showAddPerson(Stage stage) {
    stage.setScene(addPersonScene);
  }
}
