package org.openjfx.familytreeapplication.pages;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.openjfx.familytreeapplication.FamilyTreeApplication;

public class FamilyTreeList {

  private final Scene listScene;

  public FamilyTreeList() throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(
        FamilyTreeApplication.class.getResource("list-view.fxml"));
    listScene = new Scene(fxmlLoader.load(), 640, 400);
  }

  public void showList(Stage stage) {
    stage.setScene(listScene);
  }

}
