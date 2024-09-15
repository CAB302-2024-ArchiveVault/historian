package com.example.historian.models.location;

import java.util.ArrayList;
import java.util.List;

/**
 * The MockLocationDAO class provides methods for performing CRUD operations on Location objects
 * using an in-memory list.
 */
public class MockLocationDAO implements ILocationDAO {
  private static ArrayList<Location> locations = new ArrayList<>();
  private static int autoIncrementId = 0;

  /**
   * Constructs a MockLocationDAO object.
   */
  public MockLocationDAO() {
  }

  @Override
  public void addLocation(Location location) {
    location.setId(autoIncrementId);
    autoIncrementId++;
    locations.add(location);
  }

  @Override
  public void removeLocation(Location location) {
    locations.remove(location);
  }

  @Override
  public Location getLocation(int id) {
    for (Location location : locations) {
      if (location.getId() == id) {
        return location;
      }
    }
    return null;
  }

  @Override
  public List<Location> getAllLocations() {
    return new ArrayList<>(locations);
  }
}
