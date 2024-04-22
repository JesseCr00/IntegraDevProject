package org.openjfx.familytreeapplication.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.openjfx.familytreeapplication.pages.FamilyTreeAddPerson;
import org.openjfx.familytreeapplication.pages.FamilyTreeExplore;
import org.openjfx.familytreeapplication.pages.FamilyTreeProfile;

public class ListController {

  @FXML
  private Button addPersonButton;
  @FXML
  private Button profileButton;
  @FXML
  private Button exploreViewButton;

  @FXML
  protected void onExploreViewButtonClick() throws IOException {
    // Create Family Tree Explore Object
    FamilyTreeExplore familyTreeExplore = new FamilyTreeExplore();
    // Get Current Stage
    Stage currentStage = (Stage) exploreViewButton.getScene().getWindow();
    // Call function to show new scene
    familyTreeExplore.showExplore(currentStage);
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

  public void onAddPersonButtonClick() throws IOException {
    // Create Family Tree Add Person Object
    FamilyTreeAddPerson familyTreeAddPerson = new FamilyTreeAddPerson();
    // Get Current Stage
    Stage currentStage = (Stage) addPersonButton.getScene().getWindow();
    // Call function to show new scene
    familyTreeAddPerson.showAddPerson(currentStage);
  }
}
