package org.openjfx.familytreeapplication.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import org.openjfx.familytreeapplication.FamilyTreeApplication;
import org.openjfx.familytreeapplication.Person;
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
  public ChoiceBox personRelated;
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

  @FXML
  public void onAddPersonToTreeButtonClick() throws ParseException, IOException {
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    Date date = formatter.parse(dateOfBirthText.getText());
    RadioButton selectedRadio = (RadioButton) relationToggle.getSelectedToggle();
    FamilyTreeApplication.getDatabase()
        .insertPerson(firstNameText.getText(), lastNameText.getText(), date, genderText.getText(),
            personRelated.getId(), selectedRadio.getText());
    onCancelButtonClick();
    // TODO add logic so you cannot submit untill all categories filled in
  }

  @FXML
  public void onCancelButtonClick() throws IOException {
    // Create Family Tree List Object
    FamilyTreeList familyTreeList = new FamilyTreeList();
    // Get Current Stage
    Stage currentStage = (Stage) cancelButton.getScene().getWindow();
    // Call function to show new scene
    familyTreeList.showList(currentStage);
  }

  public void initialize() {
    // TODO add code to populate the dropdown
    personRelated.getItems().add(new Person("Person 1", "Person 1", "Person 1", "Person 1", "Person 1"));
  }
}
