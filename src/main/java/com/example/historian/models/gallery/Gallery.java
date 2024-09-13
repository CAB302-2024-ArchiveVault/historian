package com.example.historian.models.gallery;

import com.example.historian.models.location.Location;
import com.example.historian.models.person.Person;
import com.example.historian.models.photo.Photo;
import com.example.historian.models.tag.Tag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Gallery {
  private int id;
  private String title;
  private List<Photo> photos;

  public Gallery(String title, List<Photo> photos) {
    this.title = title;
    this.photos = photos;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<Photo> getPhotos() {
    return this.photos;
  }

  public void addPhotos(Photo photo) {
    this.photos.add(photo);
  }

  public void addPhotos(List<Photo> photos) {
    this.photos.addAll(photos);
  }

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

  public List<Location> getAllLocations() {
    List<Location> locations = new ArrayList<>();
    for (Photo photo : photos) {
      if (photo.getLocation() != null && !locations.contains(photo.getLocation())) {
        locations.add(photo.getLocation());
      }
    }
    return locations;
  }

  public Date[] getDateRange() {
    Date fromDate = null;
    Date toDate = null;

    for (Photo photo : photos) {
      if (photo.getDate() != null) {
        if (fromDate == null || toDate == null) {
          fromDate = photo.getDate();
          toDate = photo.getDate();
        } else {
          if (photo.getDate().before(fromDate)) {
            fromDate = photo.getDate();
          } else if (photo.getDate().after(toDate)) {
            toDate = photo.getDate();
          }
        }
      }
    }
    return (fromDate == null || toDate == null) ? null : new Date[]{fromDate, toDate};
  }
}
