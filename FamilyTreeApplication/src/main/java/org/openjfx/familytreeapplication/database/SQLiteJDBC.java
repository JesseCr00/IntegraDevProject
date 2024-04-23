package org.openjfx.familytreeapplication.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import org.openjfx.familytreeapplication.Person;

public class SQLiteJDBC {

  private static final int USER_ID = 0;
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

  public void insertPerson(String firstName, String lastName, Date dateOfBirth,
      String gender,
      String personRelatedToID, String relation) {

    try {
      openConnection();
      String uniqueID = UUID.randomUUID().toString();
      String dateOfBirthParsed = dateFormat.format(dateOfBirth);
      String sql = "INSERT INTO PERSON (PERSON_ID, FIRST_NAME, LAST_NAME, GENDER, DATE_OF_BIRTH) " +
          "VALUES (" + uniqueID + ", '" + firstName + "', '" + lastName + "', '" + gender + "', '"
          + dateOfBirthParsed + "' );";
      stmt.executeUpdate(sql);
      if (relation == "Parent") {
        sql = "INSERT INTO PARENT (PARENT_ID, PERSON_ID, PARENT_PERSON_ID) " +
            "VALUES ('" + UUID.randomUUID().toString() + "', '" + uniqueID + "', '" + personRelatedToID
            +  "');";
        stmt.executeUpdate(sql);
      } else if (relation == "Spouse") {
        sql = "INSERT INTO MARRIAGE (MARRIAGE_ID, PERSON_ID, MARRIAGE_PERSON_ID) " +
            "VALUES ('" + UUID.randomUUID().toString() + "', '" + uniqueID + "', '" + personRelatedToID
            + "');";
        stmt.executeUpdate(sql);
      } else if (relation == "Child") {
        sql = "INSERT INTO PARENT (PARENT_ID, PERSON_ID, PARENT_PERSON_ID) " +
            "VALUES ('" + UUID.randomUUID().toString() + "', '" + personRelatedToID + "', '" + uniqueID
            +  "');";
        stmt.executeUpdate(sql);
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

  public void updateUser(String firstName, String lastName, Date dateOfBirth,
      String gender) {
    try {
      openConnection();
      String dateOfBirthParsed = dateFormat.format(dateOfBirth);
      String sql = "UPDATE PERSON "
          + "SET FIRST_NAME = '" + firstName + "', LAST_NAME = '" + lastName
          + "', DATE_OF_BIRTH = '"
          + dateOfBirthParsed + "', GENDER = '" + gender + "' WHERE PERSON_ID = " + USER_ID + "; ";
      stmt.executeUpdate(sql);
    } catch (Exception e) {
      try {
        String dateOfBirthParsed = dateFormat.format(dateOfBirth);
        String sql =
            "INSERT INTO PERSON (PERSON_ID, FIRST_NAME, LAST_NAME, GENDER, DATE_OF_BIRTH) " +
                "VALUES ('0', '" + firstName + "', '" + lastName + "', '" + gender + "', '"
                + dateOfBirthParsed + "' );";
        stmt.executeUpdate(sql);
      } catch (Exception f) {
        logger.info(e.getMessage());
        System.exit(0);
      } finally {
        closeConnection();
      }
    } finally {
      closeConnection();
    }
  }

  public Person getPerson(String personID) {
    Person person = null;
    try {
      openConnection();
      String sql = "SELECT * FROM PERSON WHERE PERSON_ID = " + personID + ";";
      assert stmt != null;
      ResultSet userInfo = stmt.executeQuery(sql);
      person = new Person(userInfo.getString("FIRST_NAME"), userInfo.getString("LAST_NAME"),
          userInfo.getString("GENDER"), userInfo.getString("DATE_OF_BIRTH"), userInfo.getString("PERSON_ID"));
    } catch (Exception e) {
      logger.info(e.getMessage());
      System.exit(0);
    } finally {
      closeConnection();
    }
    return person;
  }

  public ArrayList<Person> getParents(String personID) {
    Person parent1 = null;
    Person parent2 = null;
    ArrayList<Person> parentList = new ArrayList<>();
    try {
      openConnection();
      // Get Parent IDs
      String sql = "SELECT PARENT_PERSON_ID FROM PARENT WHERE PERSON_ID = " + personID + ";";
      ResultSet parentInfo = stmt.executeQuery(sql);
      // Get Parents as Person Objects
      String parentID1 = parentInfo.getString("PARENT_PERSON_ID");
      parentInfo.next();
      String parentID2= parentInfo.getString("PARENT_PERSON_ID");
      parent1 = getPerson(parentID1);
      parent2 = getPerson(parentID2);
      parentList.add(parent1);
      parentList.add(parent2);
    } catch (Exception e) {
      logger.info(e.getMessage());
      System.exit(0);
    } finally {
      closeConnection();
    }
    return parentList;
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
    } catch (Exception e) {
      logger.info(e.getMessage());
      System.exit(0);
    }
  }
}
