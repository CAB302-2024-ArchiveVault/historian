package com.example.historian.models.gallery;

import com.example.historian.models.location.Location;
import com.example.historian.models.person.Person;
import com.example.historian.models.photo.Photo;
import com.example.historian.models.tag.Tag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a gallery containing a collection of photos.
 */
public class Gallery {
  private int id;
  private String title;
  private List<Photo> photos;

  /**
   * Constructs a Gallery with the specified title and list of photos.
   *
   * @param title the title of the gallery
   * @param photos the list of photos in the gallery
   */
  public Gallery(String title, List<Photo> photos) {
    this.title = title;
    this.photos = photos;
  }

  /**
   * Gets the ID of the gallery.
   *
   * @return the ID of the gallery
   */
  public int getId() {
    return this.id;
  }

  /**
   * Sets the ID of the gallery.
   *
   * @param id the new ID of the gallery
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Gets the title of the gallery.
   *
   * @return the title of the gallery
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * Sets the title of the gallery.
   *
   * @param title the new title of the gallery
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Gets the list of photos in the gallery.
   *
   * @return the list of photos in the gallery
   */
  public List<Photo> getPhotos() {
    return this.photos;
  }

  /**
   * Adds a photo to the gallery.
   *
   * @param photo the photo to be added
   */
  public void addPhotos(Photo photo) {
    this.photos.add(photo);
  }

  /**
   * Adds a list of photos to the gallery.
   *
   * @param photos the list of photos to be added
   */
  public void addPhotos(List<Photo> photos) {
    this.photos.addAll(photos);
  }

  /**
   * Gets a list of all people tagged in the photos of the gallery.
   *
   * @return a list of all tagged people
   */
  public List<Person> getAllTaggedPeople() {
    List<Person> people = new ArrayList<>();
    for (Photo photo : photos) {
      for (Tag tag : photo.getTagged()) {
        if (!people.contains(tag.getPerson())) {
          people.add(tag.getPerson());
        }
      }
    }
    return people;
  }

  /**
   * Gets a list of all locations of the photos in the gallery.
   *
   * @return a list of all locations
   */
  public List<Location> getAllLocations() {
    List<Location> locations = new ArrayList<>();
    for (Photo photo : photos) {
      if (photo.getLocation() != null && !locations.contains(photo.getLocation())) {
        locations.add(photo.getLocation());
      }
    }
    return locations;
  }

  /**
   * Gets the date range of the photos in the gallery.
   *
   * @return an array with two elements: the earliest and latest dates of the photos
   */
  public Date[] getDateRange() {
    Date fromDate = null;
    Date toDate = null;
    for (Photo photo : photos) {
      Date photoDate = photo.getDate();
      if (fromDate == null || photoDate.before(fromDate)) {
        fromDate = photoDate;
      }
      if (toDate == null || photoDate.after(toDate)) {
        toDate = photoDate;
      }
    }
    return new Date[]{fromDate, toDate};
  }
}
