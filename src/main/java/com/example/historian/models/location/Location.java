package com.example.historian.models.location;

/**
 * The Location class represents a location with an ID and a name.
 */
public class Location {
  private int id;
  private String locationName;

  /**
   * Constructs a Location object with the specified name.
   *
   * @param locationName the name of the location
   */
  public Location(String locationName) {
    this.locationName = locationName;
  }

  /**
   * Returns the ID of the location.
   *
   * @return the ID of the location
   */
  public int getId() {
    return this.id;
  }

  /**
   * Sets the ID of the location.
   *
   * @param id the ID to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Returns the name of the location.
   *
   * @return the name of the location
   */
  public String getLocationName() {
    return this.locationName;
  }
}
