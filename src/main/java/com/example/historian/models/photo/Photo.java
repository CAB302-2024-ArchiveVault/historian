package com.example.historian.models.photo;

import com.example.historian.models.location.Location;
import com.example.historian.models.tag.Tag;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Photo {
  private int id;
  private Date date;
  private String description;
  private List<Tag> tagged;
  private Location location;
  // This is the data type used for storing images and large files.
  private byte[] image;
  private String imageType;

  // If there is no description an empty string can be passed.
  // Because there are so many different permutations of arguments that can
  // be passed into the constructor I have just decided to keep it at requiring
  // an image where other metadata can be added after creation.
  public Photo(byte[] image, String imageType, String description) {
    this.image = image;
    this.imageType = imageType;
    this.description = description;
    date = null;
    tagged = new ArrayList<>();
    location = null;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Date getDate() {
    return this.date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Location getLocation() {
    return this.location;
  }

  public Integer getLocationId() {
    if (this.location == null) {
      return null;
    }
    return this.location.getId();
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public List<Tag> getTagged() {
    return this.tagged;
  }

  public void addTagged(Tag tag) {
    this.tagged.add(tag);
  }

  public void addTagged(List<Tag> tags) {
    this.tagged.addAll(tags);
  }

  public byte[] getImageAsBytes() {
    return this.image;
  }

  public javafx.scene.image.Image getImage() {
    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(getImageAsBytes())) {
      return new Image(inputStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void setImage(byte[] image) {
    this.image = image;
  }

  public String getImageType() {
    return this.imageType;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public static Photo fromFile(File file, String description) throws Exception {

    // Transform the file into a Blob
    FileInputStream inputStream = new FileInputStream(file);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    byte[] buffer = new byte[4096];
    int bytesRead;

    while ((bytesRead = inputStream.read(buffer)) != -1) {
      outputStream.write(buffer, 0, bytesRead);
    }

    inputStream.close();
    byte[] fileBytes = outputStream.toByteArray();


    // Get the file type
    Path filePath = file.toPath();
    String fileType = Files.probeContentType(filePath);

    return new Photo(fileBytes, fileType, description);
  }
}
