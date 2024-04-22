package org.openjfx.familytreeapplication.pages;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.openjfx.familytreeapplication.FamilyTreeApplication;

public class FamilyTreeExplore {

  private final Scene exploreScene;

  public FamilyTreeExplore() throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(
        FamilyTreeApplication.class.getResource("explore-view.fxml"));
    exploreScene = new Scene(fxmlLoader.load(), 640, 400);
  }

  public void showExplore(Stage stage) {
    stage.setScene(exploreScene);
  }
}
