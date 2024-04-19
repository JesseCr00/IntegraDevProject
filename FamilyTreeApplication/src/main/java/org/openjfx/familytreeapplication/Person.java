package org.openjfx.familytreeapplication;

import java.util.ArrayList;

public abstract class Person {

  private final String firstName;
  private final String lastName;
  private final String gender;
  private final String dateOfBirth;
  private final int personID;
  private String dateOfDeath;
  private ArrayList<Person> parents;
  private ArrayList<Person> partners;
  private ArrayList<Person> children;

  protected Person(String firstName, String lastName, String dateOfBirth, String gender,
      int personID) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
    this.dateOfBirth = dateOfBirth;
    this.personID = personID; // TODO hange this to unique ID
  }
}
