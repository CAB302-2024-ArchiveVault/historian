package com.example.historian.models.location;

import com.example.historian.utils.SqliteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The SqliteLocationDAO class provides methods for performing CRUD operations on Location objects
 * using an SQLite database.
 */
public class SqliteLocationDAO implements ILocationDAO {
  private final Connection connection;

  /**
   * Constructs a SqliteLocationDAO object and initializes the database connection.
   */
  public SqliteLocationDAO() {
    connection = SqliteConnection.getInstance();
    createTable();
    //insertSampleData();
  }

  /**
   * Creates the locations table in the database if it does not already exist.
   */
  private void createTable() {
    try {
      Statement statement = connection.createStatement();
      String query = "CREATE TABLE IF NOT EXISTS locations ("
              + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
              + "locationName VARCHAR NOT NULL"
              + ")";
      statement.execute(query);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Inserts sample data into the locations table.
   */
  private void insertSampleData() {
    try {
      Statement clearStatement = connection.createStatement();
      String clearQuery = "DELETE FROM locations";
      clearStatement.execute(clearQuery);

      addLocation(new Location("Brisbane, QLD, Australia"));
      addLocation(new Location("Kelvin Grove, QLD, Australia"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Creates a Location object from the given ResultSet.
   *
   * @param resultSet the ResultSet containing location data
   * @return a Location object
   * @throws Exception if an error occurs while reading from the ResultSet
   */
  private Location createFromResultSet(ResultSet resultSet) throws Exception {
    int id = resultSet.getInt("id");
    String locationName = resultSet.getString("locationName");

    Location location = new Location(locationName);
    location.setId(id);
    return location;
  }

  @Override
  public int addLocation(Location location) {
    try {
      PreparedStatement statement = connection.prepareStatement("INSERT INTO locations (locationName) VALUES (?)");
      statement.setString(1, location.getLocationName());
      statement.executeUpdate();

      // Set the ID of the new location
      ResultSet generatedKeys = statement.getGeneratedKeys();
      if (generatedKeys.next()) {
        location.setId(generatedKeys.getInt(1));
        return location.getId();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return -1;
  }

  @Override
  public void removeLocation(Location location) {
    try {
      PreparedStatement statement = connection.prepareStatement("DELETE FROM locations WHERE id = ?");
      statement.setInt(1, location.getId());
      statement.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public Location getLocation(int id) {
    try {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM locations WHERE id = ?");
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
  public List<Location> getAllLocations() {
    List<Location> locations = new ArrayList<>();
    try {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM locations");
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        locations.add(createFromResultSet(resultSet));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return locations;
  }
}
