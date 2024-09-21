package com.example.historian.models.location;

import java.util.List;

/**
 * The ILocationDAO interface provides methods for performing CRUD operations on Location objects.
 */
public interface ILocationDAO {

  /**
   * Adds a new location to the data store.
   *
   * @param location the Location object to be added
   */
  public int addLocation(Location location);

  /**
   * Removes a location from the data store.
   *
   * @param location the Location object to be removed
   */
  public void removeLocation(Location location);

  /**
   * Retrieves a location by its ID.
   *
   * @param id the ID of the location to be retrieved
   * @return the Location object with the specified ID, or null if not found
   */
  public Location getLocation(int id);

  /**
   * Retrieves all locations from the data store.
   *
   * @return a list of all Location objects
   */
  public List<Location> getAllLocations();
}
