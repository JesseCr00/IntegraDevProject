package org.openjfx.familytreeapplication.controllers;

import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;
import org.openjfx.familytreeapplication.FamilyTreeApplication;
import org.openjfx.familytreeapplication.Person;
import org.openjfx.familytreeapplication.pages.FamilyTreeAddPerson;
import org.openjfx.familytreeapplication.pages.FamilyTreeExplore;
import org.openjfx.familytreeapplication.pages.FamilyTreeProfile;

public class ListController {

  @FXML
  public TreeTableView listViewTable;
  @FXML
  public TreeTableColumn nameCol;
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
    // Get user
    String userID = FamilyTreeApplication.getDatabase().getPerson("0").getPersonID();
    TreeItem user = new TreeItem(FamilyTreeApplication.getDatabase().getPerson("0"));
    // Get Parents
    TreeItem parents = getParents(userID);
    user.getChildren().add(parents);
    // Get Spouse
    TreeItem spouse = getSpouse(userID);
    user.getChildren().add(spouse);
    // Get Children
    TreeItem children = getChildren(userID);
    user.getChildren().add(children);
    // Set Table Root
    listViewTable.setRoot(user);
  }

  public TreeItem getParents(String personID) {
    // Get 0, 1 or 2 parents
    // run get parent, get other children
    // Create Header
    TreeItem parents = new TreeItem(new Person("Parents", "...", "...", "...", "..."));
    // Get list of Parent Person Objects
    ArrayList<Person> parentList = FamilyTreeApplication.getDatabase().getParents(personID);
    // For each non-null parent object, find their parents, and other children, and add them too
    if (parentList.get(0) != null) {
      // Create TreeItem for Parent 1
      TreeItem parent1 = new TreeItem(parentList.get(0));
      String parent1ID = parentList.get(0).getPersonID();
      // Call this current function to get parents of parent
      TreeItem parentsSub = getParents(parent1ID);
      // Add parents of parent to hierarchy
      parent1.getChildren().add(parentsSub);
      // TODO Add other Children
      // Add Parent 1 Tree Item
      parents.getChildren().add(parent1);
    }
    if (parentList.get(1) != null) {
      // Create TreeItem for Parent 1
      TreeItem parent2 = new TreeItem(parentList.get(1));
      String parent2ID = parentList.get(1).getPersonID();
      // Call this current function to get parents of parent
      TreeItem parentsSub = getParents(parent2ID);
      // Add parents of parent to hierarchy
      parent2.getChildren().add(parentsSub);
      // TODO Add other Children
      // Add Parent 2 Tree Item
      parents.getChildren().add(parent2);
    }
    return parents;
  }

  public TreeItem getChildren(String personID) {
    // Get however many children
    // Run get spouse, children
    TreeItem children = new TreeItem(new Person("Children", "...", "...", "...", "..."));
   // children.getChildren().add(child1);
    //children.getChildren().add(child2);
    return children;
  }

  public TreeItem getSpouse(String personID) {
    // Get spousechildren
    // Run get parents
    TreeItem spouse = new TreeItem(new Person("Spouse", "...", "...", "...", "..."));
    //spouse.getChildren().add(spouse1);
    return spouse;
  }
}
