package org.openjfx.familytreeapplication;

public record Person(String firstName, String lastName, String gender, String dateOfBirth,
                     String personID, String relation) {

  // TODO add relationship info, so that we can pass it into the database class, instead of all the separate variables

}
