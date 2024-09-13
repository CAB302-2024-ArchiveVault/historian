package com.example.historian.models.tag;

import com.example.historian.models.person.Person;

public class Tag {
  private int id;
  private Person person;
  private int xCord;
  private int yCord;

  public Tag(Person person, int xCord, int yCord) {
    this.person = person;
    this.xCord = xCord;
    this.yCord = yCord;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int[] getCoordinates() {
    return new int[]{xCord, yCord};
  }

  public Person getPerson() {
    return person;
  }
}
