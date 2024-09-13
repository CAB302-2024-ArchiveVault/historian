package com.example.historian.models.person;

public class Person {
  private int id;
  private final String firstName;
  private final String lastName;

  public Person(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getFullName() {
    return firstName + " " + lastName;
  }
}
