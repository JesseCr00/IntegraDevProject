package org.openjfx.familytreeapplication.controllers;

import java.io.IOException;
import java.text.ParseException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.openjfx.familytreeapplication.DateFormatterUtil;
import org.openjfx.familytreeapplication.FamilyTreeApplication;
import org.openjfx.familytreeapplication.Person;
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
  protected void onUpdateDetailsButtonClick() {
    FamilyTreeApplication.getDatabase()
        .updateUser(firstNameText.getText(), lastNameText.getText(), dateOfBirthText.getText(), genderText.getText());
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

  @FXML
  protected void initialize() throws ParseException {
    Person user = FamilyTreeApplication.getDatabase().getPerson("0");
    firstNameText.setText(user.getFirstName());
    lastNameText.setText(user.getLastName());
    String formattedDate = DateFormatterUtil.databaseFormToAusForm(user.getDateOfBirth());
    dateOfBirthText.setText(formattedDate);
    genderText.setText(user.getGender());
  }
}
