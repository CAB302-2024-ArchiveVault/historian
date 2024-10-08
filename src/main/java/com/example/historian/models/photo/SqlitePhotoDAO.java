package com.example.historian.models.photo;

import com.example.historian.models.location.ILocationDAO;
import com.example.historian.models.location.Location;
import com.example.historian.models.location.SqliteLocationDAO;
import com.example.historian.models.tag.ITagDAO;
import com.example.historian.models.tag.SqliteTagDAO;
import com.example.historian.models.tag.Tag;
import com.example.historian.utils.SqliteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The SqlitePhotoDAO class provides methods for performing CRUD operations on Photo objects
 * using a SQLite database.
 */
public class SqlitePhotoDAO implements IPhotoDAO {
  private final Connection connection;

  /**
   * Constructs a SqlitePhotoDAO object.
   */
  public SqlitePhotoDAO() {
    connection = SqliteConnection.getInstance();
    createTable();
//    insertSampleData();
  }

  /**
   * Creates the photos table in the database if it does not already exist.
   */
  private void createTable() {
    try {
      Statement statement = connection.createStatement();
      String query = "CREATE TABLE IF NOT EXISTS photos ("
              + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
              + "date INTEGER,"
              + "description VARCHAR NOT NULL,"
              + "locationId VARCHAR,"
              + "image BLOB NOT NULL,"
              + "imageType VARCHAR NOT NULL,"
              + "FOREIGN KEY(locationId) REFERENCES location(id)"
              + ")";
      statement.execute(query);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Inserts sample data into the photos table.
   */
  private void insertSampleData() {
    try {
      Statement clearStatement = connection.createStatement();
      String clearQuery = "DELETE FROM photos";
      clearStatement.execute(clearQuery);

      // TODO
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Creates a Photo object from a ResultSet.
   *
   * @param resultSet the ResultSet containing the photo data
   * @return the Photo object
   * @throws Exception if an error occurs
   */
  private Photo createFromResultSet(ResultSet resultSet) throws Exception {

    // Get non-nullable values
    int id = resultSet.getInt("id");
    String description = resultSet.getString("description");
    byte[] imageStream = resultSet.getBytes("image");
    String imageType = resultSet.getString("imageType");

    // Get nullable values
    long dateLong = resultSet.getObject("date") != null ? resultSet.getLong("date") : -1;
    Date date = dateLong != -1 ? new Date(dateLong) : null;
    Integer locationId = resultSet.getObject("locationId") != null ? resultSet.getInt("locationId") : null;
    Location location = null;
    if (locationId != null) {
      ILocationDAO locationDAO = new SqliteLocationDAO();
      location = locationDAO.getLocation(locationId);
      if (location == null) {
        throw new Exception("Unable to find location with ID: " + locationId);
      }
    }

    ITagDAO tagDAO = new SqliteTagDAO();
    List<Tag> tags = tagDAO.getTagsForPhoto(id);

    Photo photo = new Photo(imageStream, imageType, description);
    photo.setId(id);
    photo.setLocation(location);
    photo.setDate(date);
    photo.setTagged(tags);

    return photo;
  }

  @Override
  public void addPhoto(Photo photo) {
    try {
      PreparedStatement statement = connection.prepareStatement("INSERT INTO photos (date, description, locationId, image, imageType) VALUES (?, ?, ?, ?, ?)");

      if (photo.getDate() != null) {
        statement.setLong(1, photo.getDate().getTime());
      } else {
        statement.setNull(1, Types.INTEGER);
      }
      statement.setString(2, photo.getDescription());

      if (photo.getLocationId() != null) {
        statement.setInt(3, photo.getLocationId());
      } else {
        statement.setNull(3, Types.INTEGER);
      }

      statement.setBytes(4, photo.getImageAsBytes());
      statement.setString(5, photo.getImageType());
      statement.executeUpdate();

      // Set the ID of the new photo
      ResultSet generatedKeys = statement.getGeneratedKeys();
      if (generatedKeys.next()) {
        photo.setId(generatedKeys.getInt(1));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void updatePhoto(Photo photo) {
    try {
      PreparedStatement statement = connection.prepareStatement("UPDATE photos SET date = ?, description = ?, locationId = ?, image = ?, imageType = ? WHERE id = ?");

      if (photo.getDate() != null) {
        statement.setLong(1, photo.getDate().getTime());
      } else {
        statement.setNull(1, Types.INTEGER);
      }
      statement.setString(2, photo.getDescription());

      if (photo.getLocationId() != null) {
        statement.setInt(3, photo.getLocationId());
      } else {
        if (photo.getLocation() != null) {
          SqliteLocationDAO locationDAO = new SqliteLocationDAO();
          int id = locationDAO.addLocation(photo.getLocation());
          statement.setInt(3, id);
        }
        statement.setNull(3, Types.INTEGER);
      }

      statement.setBytes(4, photo.getImageAsBytes());
      statement.setString(5, photo.getImageType());
      statement.setInt(6, photo.getId());
      statement.executeUpdate();

      if (photo.getTagged() != null) {
        ITagDAO tagDAO = new SqliteTagDAO();
        tagDAO.updatePhotoTags(photo.getTagged(), photo.getId());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void removePhoto(Photo photo) {
    try {
      PreparedStatement statement = connection.prepareStatement("DELETE FROM photos WHERE id = ?");
      statement.setInt(1, photo.getId());
      statement.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public Photo getPhoto(int photoId) {
    try {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM photos WHERE id = ?");
      statement.setInt(1, photoId);
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
  public List<Photo> getAllPhotos() {
    List<Photo> photos = new ArrayList<>();
    try {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM photos ORDER BY date ASC");
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        photos.add(createFromResultSet(resultSet));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return photos;
  }

  @Override
  public List<Photo> getPhotosByFilter(Date startDate, Date endDate, int location, int person) {
    List<Photo> photos = new ArrayList<>();
    try {
      StringBuilder queryBuilder = new StringBuilder("SELECT * FROM photos WHERE 1=1 ");
      int paramIndex = 1;

      if (startDate != null) {
        queryBuilder.append("AND date >= ? ");
      }
      if (endDate != null) {
        queryBuilder.append("AND date <= ? ");
      }
      if (location != -1) {
        queryBuilder.append("AND locationId = ? ");
      }
      if (person != -1) {
        queryBuilder.append("AND id IN (SELECT photoId FROM tags WHERE personId = ?) ");
      }
      queryBuilder.append("ORDER BY date ASC;");

      PreparedStatement statement = connection.prepareStatement(queryBuilder.toString());

      if (startDate != null) {
        statement.setLong(paramIndex++, startDate.getTime());
      }
      if (endDate != null) {
        statement.setLong(paramIndex++, endDate.getTime());
      }
      if (location != -1) {
        statement.setInt(paramIndex++, location);
      }
      if (person != -1) {
        statement.setInt(paramIndex, person);
      }

      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        photos.add(createFromResultSet(resultSet));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return photos;
  }
}
