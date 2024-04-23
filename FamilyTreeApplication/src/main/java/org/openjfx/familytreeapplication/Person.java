package org.openjfx.familytreeapplication;

import java.util.ArrayList;

public class Person {

  private final String firstName;
  private final String lastName;
  private final String gender;
  private final String dateOfBirth;
  private final String personID;
  private ArrayList<Person> parents;
  private ArrayList<Person> partners;
  private ArrayList<Person> children;

  public Person(String firstName, String lastName, String gender, String dateOfBirth,
      String personID) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
    this.dateOfBirth = dateOfBirth;
    this.personID = personID;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getGender() {
    return gender;
  }

  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public String getPersonID() {
    return personID;
  }
}
