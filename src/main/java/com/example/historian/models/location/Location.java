package com.example.historian.models.location;

public class Location {
  private int id;
  private String location;

  public Location(String location) {
    this.location = location;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getLocation() {
    return this.location;
  }
}
