package com.example.historian.models.photo;

import com.example.historian.models.location.ILocationDAO;
import com.example.historian.models.location.Location;
import com.example.historian.models.location.SqliteLocationDAO;
import com.example.historian.utils.SqliteConnection;
import com.example.historian.utils.SqliteDate;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SqlitePhotoDAO implements IPhotoDAO {
  private final Connection connection;

  public SqlitePhotoDAO() {
    connection = SqliteConnection.getInstance();
    createTable();
    insertSampleData();
  }

  private void createTable() {
    try {
      Statement statement = connection.createStatement();
      String query = "CREATE TABLE IF NOT EXISTS photos ("
              + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
              + "date VARCHAR NOT NULL,"
              + "description VARCHAR NOT NULL,"
              + "locationId VARCHAR NOT NULL,"
              + "image BLOB NOT NULL,"
              + "FOREIGN KEY(locationId) REFERENCES location(id)"
              + ")";
      statement.execute(query);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

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

  private Photo createFromResultSet(ResultSet resultSet) throws Exception {
    int id = resultSet.getInt("id");
    String dateString = resultSet.getString("date");
    String description = resultSet.getString("description");
    int locationId = resultSet.getInt("locationId");
    byte[] imageStream = resultSet.getBytes("image");

    Date date = null;
    try {
      date = new SqliteDate(dateString).getDate();
    } catch (ParseException e) {
      throw new Exception("Unable to parse date for photo with ID: " + id);
    }

    // Parse the image blob
    Blob imageBlob = connection.createBlob();
    imageBlob.setBytes(1, imageStream);

    // Get the location
    ILocationDAO locationDAO = new SqliteLocationDAO();
    Location location = locationDAO.getLocation(locationId);
    if (location == null) {
      throw new Exception("Unable to find location with ID: " + locationId);
    }

    // TODO: Get tags

    Photo photo = new Photo(imageBlob, description);
    photo.setId(id);
    photo.setLocation(location);
    photo.setDate(date);

    return photo;
  }

  @Override
  public void addPhoto(Photo photo) {
    try {
      PreparedStatement statement = connection.prepareStatement("INSERT INTO photos (date, description, locationId, image) VALUES (?, ?, ?, ?)");
      statement.setString(1, new SqliteDate(photo.getDate()).toSqliteFormat());
      statement.setString(2, photo.getDescription());
      statement.setInt(3, photo.getLocation().getId());
      statement.setBytes(4, photo.getImageAsByteArray());
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
      PreparedStatement statement = connection.prepareStatement("UPDATE photos SET date = ?, description = ?, locationId = ?, image = ? WHERE id = ?");
      statement.setString(1, new SqliteDate(photo.getDate()).toSqliteFormat());
      statement.setString(2, photo.getDescription());
      statement.setInt(3, photo.getLocation().getId());
      statement.setBytes(4, photo.getImageAsByteArray());
      statement.setInt(5, photo.getId());
      statement.executeUpdate();
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
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM photos");
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
