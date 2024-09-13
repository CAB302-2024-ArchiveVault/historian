package com.example.historian.models.tag;

import com.example.historian.models.person.Person;
import com.example.historian.models.person.SqlitePersonDAO;
import com.example.historian.utils.SqliteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqliteTagDAO implements ITagDAO {
  private final Connection connection;

  public SqliteTagDAO() {
    connection = SqliteConnection.getInstance();
    createTable();
    insertSampleData();
  }

  private void createTable() {
    try {
      Statement statement = connection.createStatement();
      String query = "CREATE TABLE IF NOT EXISTS tags ("
              + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
              + "personId INTEGER,"
              + "xCoord INTEGER,"
              + "yCoord INTEGER,"
              + "FOREIGN KEY(personId) REFERENCES people(id)"
              + ")";
      statement.execute(query);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void insertSampleData() {
    try {
      Statement clearStatement = connection.createStatement();
      String clearQuery = "DELETE FROM accounts";
      clearStatement.execute(clearQuery);

      SqlitePersonDAO sqlitePersonDAO = new SqlitePersonDAO();
      List<Person> people = sqlitePersonDAO.getAllPersons();

      addTag(new Tag(people.getFirst(), 30, 45));
      addTag(new Tag(people.get(1), 24, 89));
      addTag(new Tag(people.get(2), 45, 324));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Tag createFromResultSet(ResultSet resultSet) throws Exception {
    int id = resultSet.getInt("id");
    int personId = resultSet.getInt("personId");
    int xCoord = resultSet.getInt("xCoord");
    int yCoord = resultSet.getInt("yCoord");

    // Retrieve the person
    SqlitePersonDAO sqlitePersonDAO = new SqlitePersonDAO();
    Person person = sqlitePersonDAO.getPerson(personId);

    if (person == null) {
      throw new Exception("Unable to find person with ID: " + personId);
    }

    Tag tag = new Tag(person, xCoord, yCoord);
    tag.setId(id);
    return tag;
  }

  @Override
  public void addTag(Tag tag) {
    try {
      PreparedStatement statement = connection.prepareStatement("INSERT INTO tags (personId, xCoord, yCoord) VALUES (?, ?, ?)");
      statement.setInt(1, tag.getPerson().getId());
      statement.setInt(2, tag.getCoordinates()[0]);
      statement.setInt(3, tag.getCoordinates()[1]);
      statement.executeUpdate();

      // Set the ID of the new tag
      ResultSet generatedKeys = statement.getGeneratedKeys();
      if (generatedKeys.next()) {
        tag.setId(generatedKeys.getInt(1));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void removeTag(Tag tag) {
    try {
      PreparedStatement statement = connection.prepareStatement("DELETE FROM tags WHERE id = ?");
      statement.setInt(1, tag.getId());
      statement.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public Tag getTag(int id) {
    try {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM tags WHERE id = ?");
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
  public List<Tag> getAllTags() {
    List<Tag> tags = new ArrayList<>();
    try {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM tags");
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        tags.add(createFromResultSet(resultSet));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return tags;
  }
}
