package com.example.historian.models.location;

public class Location {
  private int id;
  private String locationName;

  public Location(String locationName) {
    this.locationName = locationName;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getLocationName() {
    return this.locationName;
  }
}
