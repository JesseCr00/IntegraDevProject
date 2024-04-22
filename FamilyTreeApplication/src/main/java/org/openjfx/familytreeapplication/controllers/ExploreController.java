package org.openjfx.familytreeapplication.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.openjfx.familytreeapplication.pages.FamilyTreeList;
import org.openjfx.familytreeapplication.pages.FamilyTreeProfile;

public class ExploreController {

  @FXML
  private Button listViewButton;
  @FXML
  private Button profileButton;

  @FXML
  protected void onListViewButtonClick() throws IOException {
    // Create Family Tree List Object
    FamilyTreeList familyTreeList = new FamilyTreeList();
    // Get Current Stage
    Stage currentStage = (Stage) listViewButton.getScene().getWindow();
    // Call function to show new scene
    familyTreeList.showList(currentStage);
  }

  @FXML
  protected void onProfileButtonClick() throws IOException {
    // Create Family Tree Profile Object
    FamilyTreeProfile familyTreeProfile = new FamilyTreeProfile();
    // Get Current Stage
    Stage currentStage = (Stage) profileButton.getScene().getWindow();
    // Call function to show new scene
    familyTreeProfile.showProfile(currentStage);
  }
}
