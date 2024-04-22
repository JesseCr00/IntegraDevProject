package org.openjfx.familytreeapplication.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import org.openjfx.familytreeapplication.pages.FamilyTreeList;

public class AddPersonController {

  @FXML
  public TextField firstNameText;
  @FXML
  public TextField lastNameText;
  @FXML
  public TextField dateOfBirthText;
  @FXML
  public TextField genderText;
  @FXML
  public TextField personRelated;
  @FXML
  public RadioButton fatherToggle;
  @FXML
  public ToggleGroup relationToggle;
  @FXML
  public RadioButton spouseToggle;
  @FXML
  public RadioButton sonToggle;

  @FXML
  public Button addPersonToTreeButton;
  @FXML
  public Button cancelButton;

  public void onAddPersonToTreeButtonClick() {
    // Yet to be implemented
  }

  public void onCancelButtonClick() throws IOException {
    // Create Family Tree List Object
    FamilyTreeList familyTreeList = new FamilyTreeList();
    // Get Current Stage
    Stage currentStage = (Stage) cancelButton.getScene().getWindow();
    // Call function to show new scene
    familyTreeList.showList(currentStage);
  }
}
