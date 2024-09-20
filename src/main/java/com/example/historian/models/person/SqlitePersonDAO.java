package com.example.historian.models.person;

import com.example.historian.utils.SqliteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * The SqlitePersonDAO class implements the IPersonDAO interface and provides
 * methods to interact with the SQLite database for person-related operations.
 */
public class SqlitePersonDAO implements IPersonDAO {
  private final Connection connection;

  /**
   * Constructor for SqlitePersonDAO.
   * Initializes the connection to the SQLite database.
   */
  public SqlitePersonDAO() {
    connection = SqliteConnection.getInstance();
    createTable();
//    insertSampleData();
  }

  /**
   * Creates the people table in the SQLite database if it does not already exist.
   */
  private void createTable() {
    try {
      Statement statement = connection.createStatement();
      String query = "CREATE TABLE IF NOT EXISTS people ("
              + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
              + "firstName VARCHAR NOT NULL,"
              + "lastName VARCHAR NOT NULL"
              + ")";
      statement.execute(query);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Inserts sample data into the people table.
   */
  private void insertSampleData() {
    try {
      Statement clearStatement = connection.createStatement();
      String clearQuery = "DELETE FROM people";
      clearStatement.execute(clearQuery);

      addPerson(new Person("Isaac", "Shea"));
      addPerson(new Person("Nathan", "Turner"));
      addPerson(new Person("Charles", "McVeigh"));
      addPerson(new Person("Samuel", "Reiger"));
      addPerson(new Person("Adam", "Bailey"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Creates a Person object from the specified ResultSet.
   *
   * @param resultSet the ResultSet to create the Person object from
   * @return the Person object created from the ResultSet
   * @throws Exception if an error occurs while creating the Person object
   */
  private Person createFromResultSet(ResultSet resultSet) throws Exception {
    int id = resultSet.getInt("id");
    String firstName = resultSet.getString("firstName");
    String lastName = resultSet.getString("lastName");

    Person person = new Person(firstName, lastName);
    person.setId(id);
    return person;
  }


  @Override
  public int addPerson(Person person) {
    try {
      PreparedStatement statement = connection.prepareStatement("INSERT INTO people (firstName, lastName) VALUES (?, ?)");
      statement.setString(1, person.getFirstName());
      statement.setString(2, person.getLastName());
      statement.executeUpdate();

      // Set the ID of the new person
      ResultSet generatedKeys = statement.getGeneratedKeys();
      if (generatedKeys.next()) {
        person.setId(generatedKeys.getInt(1));
        return person.getId();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return -1;
  }

  @Override
  public void removePerson(Person person) {
    try {
      PreparedStatement statement = connection.prepareStatement("DELETE FROM people WHERE id = ?");
      statement.setInt(1, person.getId());
      statement.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public Person getPerson(int id) {
    try {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM people WHERE id = ?");
      statement.setInt(1, id);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        return createFromResultSet(resultSet);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public List<Person> getAllPersons() {
    List<Person> people = new ArrayList<>();
    try {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM people");
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        people.add(createFromResultSet(resultSet));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return people;
  }
}
