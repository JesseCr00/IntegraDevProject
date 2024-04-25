package org.openjfx.familytreeapplication.controllers;

import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;
import org.openjfx.familytreeapplication.FamilyTreeApplication;
import org.openjfx.familytreeapplication.Person;
import org.openjfx.familytreeapplication.pages.FamilyTreeAddPerson;
import org.openjfx.familytreeapplication.pages.FamilyTreeProfile;

public class ListController {

  @FXML
  public TreeTableView listViewTable;
  @FXML
  public TreeTableColumn nameCol;
  @FXML
  public TreeTableColumn nameCol1;
  @FXML
  public TreeTableColumn relationCol;
  @FXML
  public TreeTableColumn dobCol;
  @FXML
  public TreeTableColumn genderCol;
  @FXML
  private Button addPersonButton;
  @FXML
  private Button profileButton;

  @FXML
  protected void onProfileButtonClick() throws IOException {
    // Create Family Tree Profile Object
    FamilyTreeProfile familyTreeProfile = new FamilyTreeProfile();
    // Get Current Stage
    Stage currentStage = (Stage) profileButton.getScene().getWindow();
    // Call function to show new scene
    familyTreeProfile.showProfile(currentStage);
  }

  @FXML
  public void onAddPersonButtonClick() throws IOException {
    // Create Family Tree Add Person Object
    FamilyTreeAddPerson familyTreeAddPerson = new FamilyTreeAddPerson();
    // Get Current Stage
    Stage currentStage = (Stage) addPersonButton.getScene().getWindow();
    // Call function to show new scene
    familyTreeAddPerson.showAddPerson(currentStage);
  }

  public void initialize() {
    nameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("firstName"));
    nameCol1.setCellValueFactory(new TreeItemPropertyValueFactory<>("lastName"));
    relationCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("relation"));
    dobCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("dateOfBirth"));
    genderCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("gender"));
    // Get user
    String userID = "0";
    TreeItem<Person> user = new TreeItem(FamilyTreeApplication.getDatabase().getPerson("0"));
    // Get Parents
    TreeItem<Person> parents = getParentsFT(userID);
    if (parents.getChildren().size() != 0) {
      user.getChildren().add(parents);
    }
    // Get Spouse
    TreeItem<Person> spouse = getSpouseFT(userID);
    if (spouse.getChildren().size() != 0) {
      user.getChildren().add(spouse);
    }
    // Get Children
    TreeItem<Person> children = getChildrenFT(userID, null);
    if (children.getChildren().size() != 0) {
      user.getChildren().add(children);
    }
    // Set Table Root
    listViewTable.setRoot(user);
  }

  public TreeItem<Person> getParentsFT(String personID) {
    // Get 0, 1 or 2 parents
    // run get parent, get other children
    // Create Header
    // TODO I am not sure if this is improper use of an object.
    TreeItem<Person> parents = new TreeItem(new Person("Parents", "...", "...", "...", "..."));
    // Get list of Parent Person Objects
    ArrayList<Person> parentList = FamilyTreeApplication.getDatabase().getParents(personID);
    // For each non-null parent object, find their parents, and other children, and add them too
    for (Person parent : parentList) {
      parent.setRelation("Parent");
      // Create TreeItem for Parent
      TreeItem<Person> parentN = new TreeItem(parent);
      String parentNID = parent.getPersonID();
      // Call this current function to get parents of parent
      TreeItem<Person> parentsSub = getParentsFT(parentNID);
      // Add parents of parent to hierarchy
      if (!parentsSub.getChildren().isEmpty()) {
        parentN.getChildren().add(parentsSub);
      }
      // Call children function to get children of parent
      TreeItem<Person> childrenSub = getChildrenFT(parentNID, personID);
      // Add children of parent to hierarchy
      if (!childrenSub.getChildren().isEmpty()) {
        parentN.getChildren().add(childrenSub);
      }
      // Add Parent 1 Tree Item
      parents.getChildren().add(parentN);
    }
    return parents;
  }

  /**
   * Get children of a person. Recursively calls itself to get children of children. Calls getSpouse to get spouse of children.
   * @param personID ID of person to get children of
   * @return TreeItem of children
   */
  public TreeItem<Person> getChildrenFT(String personID, String callingChildID) {
    // Get however many children
    // Run get spouse, children
    TreeItem<Person> children = new TreeItem(new Person("Children", "...", "...", "...", "..."));
    ArrayList<Person> childList = FamilyTreeApplication.getDatabase().getChildren(personID);
    // For each non-null parent object, find their parents, and other children, and add them too
    for (Person child : childList) {
      if (child.getPersonID().equals(callingChildID)) {
        // If a child calls this, they will not be included, therefore it is "other children"
        children = new TreeItem(new Person("Other Children", "...", "...", "...", "..."));
        continue;
      }
      child.setRelation("Child");
      // Create TreeItem for Children
      TreeItem<Person> childN = new TreeItem(child);
      String childNID = child.getPersonID();
      // Call this current function to get children of children
      TreeItem<Person> childrenSub = getChildrenFT(childNID, personID);
      // Add children of children to hierarchy
      if (!childrenSub.getChildren().isEmpty()) {
        childN.getChildren().add(childrenSub);
      }
      // Call spouse function to get spouse of children
      TreeItem<Person> spouseSub = getSpouseFT(childNID);
      // Add spouse of children to hierarchy
      childN.getChildren().add(spouseSub);
      // Add Parent 1 Tree Item
      children.getChildren().add(childN);
    }
    return children;
  }
  /**
   * Get spouse of a person. Calls getParents to get parents of spouse.
   * @param personID ID of person to get spouse of
   * @return TreeItem of spouse
   */
  public TreeItem<Person> getSpouseFT(String personID) {
    TreeItem<Person> spouses= new TreeItem(new Person("Spouse", "...", "...", "...", "..."));
    ArrayList<Person> spouseList = FamilyTreeApplication.getDatabase().getSpouse(personID);
    // For each non-null parent object, find their parents, and other children, and add them too
    for (Person spouse : spouseList) {
      spouse.setRelation("Spouse");
      // Create TreeItem for Spouse
      TreeItem<Person> spouseN = new TreeItem(spouse);
      String spouseNID = spouse.getPersonID();
      // Call parent function to get parents of spouse
      TreeItem<Person> parentsSub = getParentsFT(spouseNID);
      // Add parents of spouse to hierarchy
      if (!parentsSub.getChildren().isEmpty()) {
        spouseN.getChildren().add(parentsSub);
      }
      // Add Spouse Tree Item
      spouses.getChildren().add(spouseN);
      // TODO add children note in tree (this persons children are their spouses children)
    }
    return spouses;
  }
}
