package org.openjfx.familytreeapplication.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
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
  public ComboBox personRelated;
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
    RadioButton selectedRadio = (RadioButton) relationToggle.getSelectedToggle();
    Person relatedPersonFromBox = (Person) personRelated.getValue();
    FamilyTreeApplication.getDatabase()
        .insertPerson(firstNameText.getText(), lastNameText.getText(), dateOfBirthText.getText(), genderText.getText(),
            relatedPersonFromBox.getPersonID(), selectedRadio.getText());
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
