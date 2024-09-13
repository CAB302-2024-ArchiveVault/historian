package com.example.historian.models.tag;

import com.example.historian.models.person.Person;

public class Tag {
  private int id;
  private int photoId;
  private Person person;
  private int xCoord;
  private int yCoord;

  public Tag(int photoId, Person person, int xCoord, int yCoord) {
    this.photoId = photoId;
    this.person = person;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getPhotoId() {
    return this.photoId;
  }

  public int[] getCoordinates() {
    return new int[]{xCoord, yCoord};
  }

  public Person getPerson() {
    return person;
  }
}
