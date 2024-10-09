package com.example.historian.models.tag;

import com.example.historian.models.person.*;
import com.example.historian.models.photo.*;
import com.example.historian.utils.SqliteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The SqliteTagDAO class provides methods for performing CRUD operations on Tag objects
 * using a SQLite database.
 */
public class SqliteTagDAO implements ITagDAO {
  private final Connection connection;

  /**
   * Constructs a SqliteTagDAO object.
   */
  public SqliteTagDAO() {
    connection = SqliteConnection.getInstance();
    createTable();
  }

  /**
   * Creates the tags table in the database if it does not already exist.
   */
  private void createTable() {
    try {
      Statement statement = connection.createStatement();
      String query = "CREATE TABLE IF NOT EXISTS tags ("
              + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
              + "photoId INTEGER NOT NULL,"
              + "personId INTEGER NOT NULL,"
              + "xCoord INTEGER NOT NULL,"
              + "yCoord INTEGER NOT NULL,"
              + "FOREIGN KEY(photoId) REFERENCES photos(id),"
              + "FOREIGN KEY(personId) REFERENCES people(id)"
              + ")";
      statement.execute(query);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Inserts sample data into the tags table.
   */
  private void insertSampleData() {
    try {
      Statement clearStatement = connection.createStatement();
      String clearQuery = "DELETE FROM accounts";
      clearStatement.execute(clearQuery);

      IPhotoDAO photoDAO = new SqlitePhotoDAO();
      List<Photo> photos = photoDAO.getAllPhotos();

      IPersonDAO personDAO = new SqlitePersonDAO();
      List<Person> people = personDAO.getAllPersons();

      addTag(new Tag(photos.getFirst().getId(), people.getFirst(), 30, 45));
      addTag(new Tag(photos.getFirst().getId(), people.get(1), 24, 89));
      addTag(new Tag(photos.getFirst().getId(), people.get(2), 45, 324));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Creates a Tag object from the specified ResultSet.
   *
   * @param resultSet The ResultSet to create the Tag object from.
   * @return The Tag object created from the ResultSet.
   * @throws Exception If an error occurs while creating the Tag object.
   */
  private Tag createFromResultSet(ResultSet resultSet) throws Exception {
    int id = resultSet.getInt("id");
    int photoId = resultSet.getInt("photoId");
    int personId = resultSet.getInt("personId");
    int xCoord = resultSet.getInt("xCoord");
    int yCoord = resultSet.getInt("yCoord");

    // Retrieve the person
    IPersonDAO personDAO = new SqlitePersonDAO();
    Person person = personDAO.getPerson(personId);

    if (person == null) {
      throw new Exception("Unable to find person with ID: " + personId);
    }

    Tag tag = new Tag(photoId, person, xCoord, yCoord);
    tag.setId(id);
    return tag;
  }

  @Override
  public void addTag(Tag tag) {
    try {
      PreparedStatement statement = connection.prepareStatement("INSERT INTO tags (photoId, personId, xCoord, yCoord) VALUES (?, ?, ?, ?)");
      statement.setInt(1, tag.getPhotoId());
      statement.setInt(2, tag.getPerson().getId());
      statement.setInt(3, tag.getCoordinates()[0]);
      statement.setInt(4, tag.getCoordinates()[1]);
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
  public void updatePhotoTags(List<Tag> tags, int photoId) {
    List<Tag> currentTags = getTagsForPhoto(photoId);
    Set<Tag> tagsSet = new HashSet<Tag>(tags);
    Set<Tag> currentTagsSet = new HashSet<Tag>(currentTags);

    Set<Tag> tagsToBeAdded = new HashSet<Tag>(tagsSet);
    Set<Tag> tagsToBeRemoved = new HashSet<Tag>(currentTagsSet);

    tagsToBeAdded.removeAll(currentTagsSet);
    tagsToBeRemoved.removeAll(tagsSet);

    for (Tag tag : tagsToBeAdded) {
      if (tag.getPerson().getId() < 0) {
        SqlitePersonDAO personDAO = new SqlitePersonDAO();
        int personId = personDAO.addPerson(tag.getPerson());
        tag.getPerson().setId(personId);
      }
      addTag(tag);
    }
    for (Tag tag : tagsToBeRemoved) {
      removeTag(tag);
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

  @Override
  public List<Tag> getTagsForPhoto(int photoId) {
    List<Tag> tags = new ArrayList<>();
    try {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM tags WHERE photoId = ?");
      statement.setInt(1, photoId);
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
