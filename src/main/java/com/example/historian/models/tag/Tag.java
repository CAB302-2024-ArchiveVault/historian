package com.example.historian.models.tag;

import com.example.historian.models.person.Person;

import java.util.Objects;

/**
 * The Tag class represents a tag associated with a photo, including the ID, photo ID, person, and coordinates.
 */
public class Tag {
  private int id;
  private int photoId;
  private Person person;
  private int xCoord;
  private int yCoord;

  /**
   * Constructs a Tag object with the specified photo ID, person, and coordinates.
   *
   * @param photoId the ID of the photo associated with the tag
   * @param person the person associated with the tag
   * @param xCoord the x-coordinate of the tag
   * @param yCoord the y-coordinate of the tag
   */
  public Tag(int photoId, Person person, int xCoord, int yCoord) {
    this.photoId = photoId;
    this.person = person;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
  }

  /**
   * Returns the ID of the tag.
   *
   * @return the ID of the tag
   */
  public int getId() {
    return this.id;
  }

  /**
   * Sets the ID of the tag.
   *
   * @param id the ID to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Returns the ID of the photo associated with the tag.
   *
   * @return the ID of the photo
   */
  public int getPhotoId() {
    return this.photoId;
  }

  /**
   * Returns the coordinates of the tag as an array.
   *
   * @return an array containing the x and y coordinates of the tag
   */
  public int[] getCoordinates() {
    return new int[]{xCoord, yCoord};
  }

  /**
   * Returns the person associated with the tag.
   *
   * @return the person associated with the tag
   */
  public Person getPerson() {
    return person;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Tag tag = (Tag) obj;
    return Objects.equals(id, tag.id);  // Compare by relevant field (e.g., id)
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);  // Use the same field as in equals()
  }
}