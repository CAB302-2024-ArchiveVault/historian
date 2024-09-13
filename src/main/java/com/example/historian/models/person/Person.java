package com.example.historian.models.person;

/**
 * The Person class represents a person with an ID, first name, and last name.
 */
public class Person {
  private int id;
  private final String firstName;
  private final String lastName;

  /**
   * Constructs a Person object with the specified first name and last name.
   *
   * @param firstName the first name of the person
   * @param lastName the last name of the person
   */
  public Person(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  /**
   * Returns the ID of the person.
   *
   * @return the ID of the person
   */
  public int getId() {
    return this.id;
  }

  /**
   * Sets the ID of the person.
   *
   * @param id the ID to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Returns the full name of the person.
   *
   * @return the full name of the person
   */
  public String getFullName() {
    return firstName + " " + lastName;
  }

  /**
   * Returns the first name of the person.
   *
   * @return the first name of the person
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Returns the last name of the person.
   *
   * @return the last name of the person
   */
  public String getLastName() {
    return lastName;
  }
}