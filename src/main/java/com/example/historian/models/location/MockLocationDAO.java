package com.example.historian.models.location;

import java.util.ArrayList;
import java.util.List;

public class MockLocationDAO implements ILocationDAO {
  private static ArrayList<Location> locations = new ArrayList<>();
  private static int autoIncrementId = 0;

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
