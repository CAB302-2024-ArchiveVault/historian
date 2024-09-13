package com.example.historian.models.tag;

import com.example.historian.models.person.Person;

public class Tag {
  private int id;
  private Person person;
  private int xCoord;
  private int yCoord;

  public Tag(Person person, int xCoord, int yCoord) {
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

  public int[] getCoordinates() {
    return new int[]{xCoord, yCoord};
  }

  public Person getPerson() {
    return person;
  }
}
