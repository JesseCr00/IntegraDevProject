package org.openjfx.familytreeapplication.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;
import org.openjfx.familytreeapplication.DateFormatterUtil;
import org.openjfx.familytreeapplication.FamilyTreeApplication;
import org.openjfx.familytreeapplication.Person;

public class SQLiteJDBC {

  private static final String USER_ID = "0";
  private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
  Logger logger = Logger.getLogger(getClass().getName());
  private Statement stmt;
  private Connection c;

  public SQLiteJDBC() {
    initDB();
  }

  public void initDB() {

    try {
      openConnection();
      String sql = "CREATE TABLE IF NOT EXISTS PERSON " +
          "(PERSON_ID STRING PRIMARY KEY     NOT NULL," +
          " FIRST_NAME           TEXT    NOT NULL, " +
          " LAST_NAME            TEXT     NOT NULL, " +
          " GENDER        TEXT NOT NULL, " +
          " DATE_OF_BIRTH         DATE NOT NULL)";
      assert stmt != null;
      stmt.executeUpdate(sql);
      sql = "CREATE TABLE IF NOT EXISTS PARENT " +
          "(PARENT_ID STRING PRIMARY KEY     NOT NULL," +
          " PERSON_ID           STRING    NOT NULL, " +
          " PARENT_PERSON_ID            STRING     NOT NULL, " +
          //" RELATIONSHIP        TEXT NOT NULL," +
          "FOREIGN KEY (PERSON_ID) REFERENCES PERSON (PERSON_ID),"
          + "FOREIGN KEY (PARENT_PERSON_ID) REFERENCES PERSON (PERSON_ID))";
      stmt.executeUpdate(sql);
      sql = "CREATE TABLE IF NOT EXISTS MARRIAGE " +
          "(MARRIAGE_ID STRING PRIMARY KEY     NOT NULL," +
          " PERSON_ID           STRING    NOT NULL, " +
          " MARRIAGE_PERSON_ID            STRING     NOT NULL,"
          + "FOREIGN KEY (PERSON_ID) REFERENCES PERSON (PERSON_ID),"
          + "FOREIGN KEY (MARRIAGE_PERSON_ID) REFERENCES PERSON (PERSON_ID))";
      stmt.executeUpdate(sql);

      // TEST DATABASE STUFF HERE
      /*
      sql = "INSERT INTO PERSON (PERSON_ID, FIRST_NAME, LAST_NAME, GENDER, DATE_OF_BIRTH) " +
          "VALUES (0, 'Jesse', 'Cruickshank', 'Male', '2000-09-22');";
      stmt.executeUpdate(sql);
      */

    } catch (Exception e) {
      logger.info(e.getMessage());
      System.exit(0);
    } finally {
      closeConnection();
    }
  }

  public void insertPerson(String firstName, String lastName, String dateOfBirth,
      String gender,
      String personRelatedToID, String relation) {

    try {
      openConnection();
      String uniqueID = UUID.randomUUID().toString();
      String dateOfBirthParsed = DateFormatterUtil.ausFormToDatabaseForm(dateOfBirth);
      String sql = "INSERT INTO PERSON (PERSON_ID, FIRST_NAME, LAST_NAME, GENDER, DATE_OF_BIRTH) " +
          "VALUES ('" + uniqueID + "', '" + firstName + "', '" + lastName + "', '" + gender + "', '"
          + dateOfBirthParsed + "' );";
      assert stmt != null;
      stmt.executeUpdate(sql);
      if (relation.equals("Parent")) {
        // This is actually adding the new person as Child

        if (isDestinationSpouse(personRelatedToID)) {
          // If we add a child to a "destination" spouse, this child needs to be added to "source" spouse as a child so it will show
          ArrayList<Person> spouseList = getSpouse(personRelatedToID);
          for (Person spouse : spouseList) {
            // TODO in theory there will be only one spouse but this needs to be fixed
            sql = "INSERT INTO PARENT (PARENT_ID, PERSON_ID, PARENT_PERSON_ID) " +
                "VALUES ('" + UUID.randomUUID() + "', '" + uniqueID + "', '"
                + spouse.getPersonID()
                + "');";
            stmt.executeUpdate(sql);
          }
        } else {
          sql = "INSERT INTO PARENT (PARENT_ID, PERSON_ID, PARENT_PERSON_ID) " +
              "VALUES ('" + UUID.randomUUID() + "', '" + uniqueID + "', '"
              + personRelatedToID
              + "');";
          stmt.executeUpdate(sql);
        }

      } else if (relation.equals("Spouse")) {
        // There is code in the controller to stop you from adding multiple spouses.
        if (isDestinationParent(personRelatedToID)) {
          // If we add a spouse to a "destination" parent, the spouse needs to be added to the "source" child as a parent so it will show
          ArrayList<Person> parentList = getParents(personRelatedToID);
          for (Person parent : parentList) {
            // TODO in theory there will be only two parents
            sql = "INSERT INTO PARENT (PARENT_ID, PERSON_ID, PARENT_PERSON_ID) " +
                "VALUES ('" + UUID.randomUUID() + "', '" + parent.getPersonID() + "', '"
                + uniqueID
                + "');";
            stmt.executeUpdate(sql);
          }
        } else {
          sql = "INSERT INTO MARRIAGE (MARRIAGE_ID, PERSON_ID, MARRIAGE_PERSON_ID) " +
              "VALUES ('" + UUID.randomUUID() + "', '" + personRelatedToID + "', '"
              + uniqueID
              + "');";
          stmt.executeUpdate(sql);
        }
      } else if (relation.equals("Child")) {
        // This is actually adding the new person as Parent
        if (isDestinationChild(personRelatedToID)) {
          // If we add a parent to a "destination" child, the parent needs to be added to the "source" parent as a spouse so it will show
          ArrayList<Person> childList = getChildren(personRelatedToID);
          for (Person child : childList) {
            sql = "INSERT INTO MARRIAGE (MARRIAGE_ID, PERSON_ID, MARRIAGE_PERSON_ID) " +
                "VALUES ('" + UUID.randomUUID() + "', '" + uniqueID + "', '"
                + child.getPersonID()
                + "');";
            stmt.executeUpdate(sql);
          }
        } else {
          sql = "INSERT INTO PARENT (PARENT_ID, PERSON_ID, PARENT_PERSON_ID) " +
              "VALUES ('" + UUID.randomUUID() + "', '" + personRelatedToID + "', '"
              + uniqueID
              + "');";
          stmt.executeUpdate(sql);
        }
      } else {
        logger.info("Invalid relation");
      }
    } catch (Exception e) {
      logger.info(e.getMessage());
      System.exit(0);
    } finally {
      closeConnection();
    }
  }

  public void updateUser(String firstName, String lastName, String dateOfBirth,
      String gender) {
    if (this.getPerson("0") == null) {
      try {
        openConnection();
        String dateOfBirthParsed = DateFormatterUtil.ausFormToDatabaseForm(dateOfBirth);
        String sql =
            "INSERT INTO PERSON (PERSON_ID, FIRST_NAME, LAST_NAME, GENDER, DATE_OF_BIRTH) " +
                "VALUES ('0', '" + firstName + "', '" + lastName + "', '" + gender + "', '"
                + dateOfBirthParsed + "' );";
        stmt.executeUpdate(sql);
      } catch (Exception e) {
        logger.info(e.getMessage());
        System.exit(0);
      } finally {
        closeConnection();
      }
    } else {
      try {
        openConnection();
        String dateOfBirthParsed = DateFormatterUtil.ausFormToDatabaseForm(dateOfBirth);
        String sql = "UPDATE PERSON "
            + "SET FIRST_NAME = '" + firstName + "', LAST_NAME = '" + lastName
            + "', DATE_OF_BIRTH = '"
            + dateOfBirthParsed + "', GENDER = '" + gender + "' WHERE PERSON_ID = '" + USER_ID
            + "'; ";
        assert stmt != null;
        stmt.executeUpdate(sql);
      } catch (Exception e) {
        logger.info(e.getMessage());
        System.exit(0);
      } finally {
        closeConnection();
      }
    }
  }

  public Person getPerson(String personID) {
    Person person = null;
    try {
      openConnection();
      String sql = "SELECT * FROM PERSON WHERE PERSON_ID = '" + personID + "';";
      assert stmt != null;
      ResultSet userInfo = stmt.executeQuery(sql);
      if (!userInfo.isBeforeFirst() ) {
        userInfo.close();
        return person;
      }
      person = new Person(userInfo.getString("FIRST_NAME"), userInfo.getString("LAST_NAME"),
          userInfo.getString("GENDER"), userInfo.getString("DATE_OF_BIRTH"),
          userInfo.getString("PERSON_ID"));
      userInfo.close();
    } catch (Exception e) {
      logger.info(e.getMessage());
      System.exit(0);
    } finally {
      closeConnection();
    }
    return person;
  }

  public ArrayList<Person> getParents(String personID) {
    ArrayList<Person> parentList = new ArrayList<>();
    ArrayList<String> parentIDs = new ArrayList<>();
    try {
      openConnection();
      // Get Parent IDs
      String sql = "SELECT * FROM PARENT WHERE PERSON_ID = '" + personID + "';";
      assert stmt != null;
      ResultSet parentInfo = stmt.executeQuery(sql);
      // Get Parents as Person Objects
      while (parentInfo.next()) {
        parentIDs.add(parentInfo.getString("PARENT_PERSON_ID"));
      }
      parentInfo.close();


    } catch (Exception e) {
      logger.info(e.getMessage());
      System.exit(0);
    } finally {
      closeConnection();
    }
    for (String parentIDN : parentIDs) {
      Person parentN = getPerson(parentIDN);
      parentList.add(parentN);
    }
    return parentList;
  }

  public ArrayList<Person> getChildren(String personID) {
    ArrayList<Person> childList = new ArrayList<>();
    ArrayList<String> childIDs = new ArrayList<>();
    try {
      openConnection();
      // Get Child IDs
      String sql = "SELECT * FROM PARENT WHERE PARENT_PERSON_ID = '" + personID + "';";
      assert stmt != null;
      ResultSet childInfo = stmt.executeQuery(sql);
      // Get Children as Person Objects
      while (childInfo.next()) {
        childIDs.add(childInfo.getString("PERSON_ID"));
      }
      childInfo.close();

    } catch (Exception e) {
      logger.info(e.getMessage());
      System.exit(0);
    } finally {
      closeConnection();
    }
    for (String childIDN : childIDs) {
      Person childN = getPerson(childIDN);
      childList.add(childN);
    }
    return childList;
  }

  public ArrayList<Person> getSpouse(String personID) {
    ArrayList<Person> spouseList = new ArrayList<>();
    ArrayList<String> spouseIDs = new ArrayList<>();
    try {
      openConnection();
      // Get Spouse IDs
      String sql = "SELECT * FROM MARRIAGE WHERE PERSON_ID = '" + personID + "';";
      assert stmt != null;
      ResultSet spouseInfo = stmt.executeQuery(sql);
      // Get Spouse as Person Objects
      while (spouseInfo.next()) {
        spouseIDs.add(spouseInfo.getString("MARRIAGE_PERSON_ID"));
      }
      spouseInfo.close();
    } catch (Exception e) {
      logger.info(e.getMessage());
      System.exit(0);
    } finally {
      closeConnection();
    }
    for (String spouseIDN : spouseIDs) {
      Person spouseN = getPerson(spouseIDN);
      spouseList.add(spouseN);
    }
    return spouseList;
  }

  public void openConnection() {
    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:familytree.db");
      stmt = c.createStatement();
    } catch (Exception e) {
      logger.info(e.getMessage());
      System.exit(0);
    }
  }

  public void closeConnection() {
    try {
      stmt.close();
      c.close();
      stmt = null;
      c = null;
    } catch (Exception e) {
      logger.info(e.getMessage());
      System.exit(0);
    }
  }

  public ArrayList<Person> getAllPeople() {
    ArrayList<Person> people = new ArrayList<>();
    try {
      openConnection();
      String sql = "SELECT * FROM PERSON;";
      assert stmt != null;
      ResultSet peopleInfo = stmt.executeQuery(sql);
      while (peopleInfo.next()) {
        Person person = new Person(peopleInfo.getString("FIRST_NAME"),
            peopleInfo.getString("LAST_NAME"),
            peopleInfo.getString("DATE_OF_BIRTH"), peopleInfo.getString("GENDER"),
            peopleInfo.getString("PERSON_ID"));
        people.add(person);
      }
      peopleInfo.close();
    } catch (Exception e) {
      logger.info(e.getMessage());
      System.exit(0);
    } finally {
      closeConnection();
    }
    return people;
  }

  public boolean isDestinationParent(String parentID) {
    boolean destinationParentStatus = false;
    try {
      openConnection();
      // Get Parent IDs
      String sql = "SELECT * FROM PARENT WHERE PARENT_PERSON_ID = '" + parentID + "';";
      assert stmt != null;
      ResultSet parentInfo = stmt.executeQuery(sql);
      // Get Parents as Person Objects
      while (parentInfo.next()) {
        destinationParentStatus = true;
        break;
      }
      parentInfo.close();
    } catch (Exception e) {
      logger.info(e.getMessage());
      System.exit(0);
    } finally {
      closeConnection();
    }
    return destinationParentStatus;
  }

  public boolean isDestinationSpouse(String spouseID) {
    boolean destinationSpouseStatus = false;
    try {
      openConnection();
      // Get Spouse IDs
      String sql = "SELECT * FROM MARRIAGE WHERE MARRIAGE_PERSON_ID = '" + spouseID + "';";
      assert stmt != null;
      ResultSet spouseInfo = stmt.executeQuery(sql);
      // Get Spouse as Person Objects
      while (spouseInfo.next()) {
        destinationSpouseStatus = true;
        break;
      }
      spouseInfo.close();
    } catch (Exception e) {
      logger.info(e.getMessage());
      System.exit(0);
    } finally {
      closeConnection();
    }
    return destinationSpouseStatus;
  }

  public boolean isDestinationChild(String childID) {
    boolean destinationChildStatus = false;
    try {
      openConnection();
      // Get Child IDs
      String sql = "SELECT * FROM PARENT WHERE PERSON_ID = '" + childID + "';";
      assert stmt != null;
      ResultSet childInfo = stmt.executeQuery(sql);
      // Get Children as Person Objects
      while (childInfo.next()) {
        destinationChildStatus = true;
        break;
      }
      childInfo.close();
    } catch (Exception e) {
      logger.info(e.getMessage());
      System.exit(0);
    } finally {
      closeConnection();
    }
    return destinationChildStatus;
  }

}
