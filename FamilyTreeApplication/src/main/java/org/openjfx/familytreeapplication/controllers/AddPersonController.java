package org.openjfx.familytreeapplication.controllers;

import static java.lang.Thread.sleep;

import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.util.Callback;
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
  public ComboBox<Person> personRelated;
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
  public Label spouseMessage;

  @FXML
  public void onAddPersonToTreeButtonClick()
      throws IOException, InterruptedException {
    // TODO add logic so you cannot submit untill all categories filled in
    // TODO input validation
    RadioButton selectedRadio = (RadioButton) relationToggle.getSelectedToggle();
    Person relatedPersonFromBox = personRelated.getValue();
    if (selectedRadio.getText().equals("Spouse") && !FamilyTreeApplication.getDatabase().getSpouse(relatedPersonFromBox.getPersonID()).isEmpty()) {
      spouseMessage.setText("");
      sleep(200);
      spouseMessage.setText("This person already has a spouse");
    } else {
      FamilyTreeApplication.getDatabase()
          .insertPerson(firstNameText.getText(), lastNameText.getText(), dateOfBirthText.getText(),
              genderText.getText(),
              relatedPersonFromBox.getPersonID(), selectedRadio.getText());
      onCancelButtonClick();
    }
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
    Callback<ListView<Person>, ListCell<Person>> cellFactory = new Callback<ListView<Person>, ListCell<Person>>() {
      @Override
      public ListCell<Person> call(ListView<Person> l) {
        return new ListCell<Person>() {
          @Override
          protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);
            if (person == null || empty) {
              setText("");
            } else {
              setText(person.getFirstName());
            }
          }
        };
      }
    };
    personRelated.setButtonCell(cellFactory.call(null));
    personRelated.setCellFactory(cellFactory);
    ArrayList<Person> allPeople = FamilyTreeApplication.getDatabase().getAllPeople();
    for (Person person : allPeople) {
      personRelated.getItems().add(person);
    }
  }
}
