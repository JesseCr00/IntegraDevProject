package org.openjfx.familytreeapplication.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.openjfx.familytreeapplication.FamilyTreeApplication;
import org.openjfx.familytreeapplication.pages.FamilyTreeExplore;
import org.openjfx.familytreeapplication.pages.FamilyTreeList;

public class ProfileController {

  @FXML
  public TextField dateOfBirthText;
  @FXML
  public TextField lastNameText;
  @FXML
  public TextField genderText;
  @FXML
  public TextField firstNameText;
  @FXML
  private Button listViewButton;
  @FXML
  private Button exploreViewButton;
  @FXML
  private Button updateDetailsButton;
  @FXML
  private Label successLabel;

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
  protected void onExploreViewButtonClick() throws IOException {
    // Create Family Tree Explore Object
    FamilyTreeExplore familyTreeExplore = new FamilyTreeExplore();
    // Get Current Stage
    Stage currentStage = (Stage) exploreViewButton.getScene().getWindow();
    // Call function to show new scene
    familyTreeExplore.showExplore(currentStage);
  }

  @FXML
  protected void onUpdateDetailsButtonClick() throws ParseException {
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    Date date = formatter.parse(dateOfBirthText.getText());
    FamilyTreeApplication.getDatabase()
        .updateUser(firstNameText.getText(), lastNameText.getText(), date, genderText.getText());
    successLabel.setTextFill(Color.GREEN);
    successLabel.setText("Changes Saved Successfully!");
    updateDetailsButton.setDisable(true);
  }

  @FXML
  protected void onTextChanged() {
    successLabel.setTextFill(Color.RED);
    successLabel.setText("Changes not saved!");
    updateDetailsButton.setDisable(false);
  }

}
