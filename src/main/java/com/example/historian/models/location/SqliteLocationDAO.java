package com.example.historian.models.location;

import com.example.historian.utils.SqliteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqliteLocationDAO implements ILocationDAO {
  private final Connection connection;

  public SqliteLocationDAO() {
    connection = SqliteConnection.getInstance();
    createTable();
    insertSampleData();
  }

  private void createTable() {
    try {
      Statement statement = connection.createStatement();
      String query = "CREATE TABLE IF NOT EXISTS locations ("
              + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
              + "locationName VARCHAR NOT NULL"
              + ")";
      statement.executeQuery(query);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

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

  private Location createFromResultSet(ResultSet resultSet) throws Exception {
    int id = resultSet.getInt("id");
    String locationName = resultSet.getString("locationName");

    Location location = new Location(locationName);
    location.setId(id);
    return location;
  }


  @Override
  public void addLocation(Location location) {
    try {
      PreparedStatement statement = connection.prepareStatement("INSERT INTO locations (locationName) VALUES (?)");
      statement.setString(1, location.getLocationName());
      statement.executeUpdate();

      // Set the ID of the new location
      ResultSet generatedKeys = statement.getGeneratedKeys();
      if (generatedKeys.next()) {
        location.setId(generatedKeys.getInt(1));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void removeLocation(Location location) {
    try {
      PreparedStatement statement = connection.prepareStatement("DELET FROM locations WHERE id = ?");
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
