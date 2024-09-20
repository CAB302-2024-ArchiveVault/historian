package com.example.historian.models.person;

import java.util.List;

/**
 * The IPersonDAO interface provides methods for performing CRUD operations on Person objects.
 */
public interface IPersonDAO {

  /**
   * Adds a new person to the data store.
   *
   * @param person the Person object to be added
   */
  public int addPerson(Person person);

  /**
   * Removes a person from the data store.
   *
   * @param person the Person object to be removed
   */
  public void removePerson(Person person);

  /**
   * Retrieves a person by their ID.
   *
   * @param id the ID of the person to be retrieved
   * @return the Person object with the specified ID, or null if not found
   */
  public Person getPerson(int id);

  /**
   * Retrieves all persons from the data store.
   *
   * @return a list of all Person objects
   */
  public List<Person> getAllPersons();
}