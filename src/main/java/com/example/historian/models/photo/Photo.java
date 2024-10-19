package com.example.historian.models.photo;

import com.example.historian.models.location.Location;
import com.example.historian.models.tag.Tag;
import javafx.scene.image.Image;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The Photo class represents a photo with an ID, date, description, tags, location, and image data.
 */
public class Photo {
  private int id;
  private Date date;
  private String description;
  private List<Tag> tagged;
  private Location location;
  private byte[] image;
  private String imageType;
  private int uploaderAccountId;
  private Image thumbNail;

  private static final int defaultHeight = 500;
  private static final int defaultWidth = 500;

  private final int adjustedImageWidth;
  private final int adjustedImageHeight;

  private final int thumbnailWidth = 200;
  private final int thumbnailHeight = 200;

  private final int adjustedThumbnailWidth;
  private final int adjustedThumbnailHeight;


  /**
   * Constructs a Photo object with the specified image data, image type, and description.
   * <p>
   * Because there are so many different permutations of arguments that can
   * be passed into the constructor I have just decided to keep it at requiring
   * an image where other metadata can be added after creation.
   *
   * @param image       the image data as a byte array
   * @param imageType   the type of the image (e.g., "jpg", "png")
   * @param description the description of the photo
   */
  public Photo(byte[] image, String imageType, String description, int uploaderAccountId) {     //
    this.image = image;
    this.imageType = imageType;
    this.description = description;
    this.uploaderAccountId = uploaderAccountId; //
    date = null;
    tagged = new ArrayList<>();
    location = null;

      double imageHeight = this.getImage().getHeight();
      double imageWidth = this.getImage().getWidth();
      double aspectRatio = imageWidth / imageHeight;

    if (aspectRatio > 1)
    {
      adjustedImageWidth = defaultWidth;
      adjustedThumbnailWidth = thumbnailWidth;
      adjustedImageHeight = (int) (defaultWidth/ aspectRatio);
      adjustedThumbnailHeight = (int) (thumbnailWidth/aspectRatio);
    }
    else if (aspectRatio == 1)
    {
      adjustedImageWidth = defaultWidth;
      adjustedThumbnailWidth = thumbnailWidth;
      adjustedImageHeight = defaultHeight;
      adjustedThumbnailHeight = thumbnailHeight;
    }
    else
    {
      adjustedImageWidth = (int) (defaultHeight* aspectRatio);
      adjustedThumbnailWidth = (int) (thumbnailHeight* aspectRatio);
      adjustedImageHeight = defaultHeight;
      adjustedThumbnailHeight = thumbnailHeight;
    }

    thumbNail = createThumbnail(this.getImage(), thumbnailWidth, thumbnailHeight);

  }

  //
  // Constructor with 3 arguments (backward compatibility)
  public Photo(byte[] image, String imageType, String description) {
    this(image, imageType, description, -1);  // Default uploaderAccountId (-1 or other default value)
  }

  public Image createThumbnail(Image originalImage, int width, int height) {

    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(this.getImageAsBytes())) {
      return new Image(inputStream, width, height, true, true);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
    //return new Image(originalImage.get, width, height, true, true);
  }

  public Image getThumbNail()
  {
    return thumbNail;
  }

  // Add getter and setter for uploaderAccountId
  public int getUploaderAccountId() {
    return uploaderAccountId;
  }

  public void setUploaderAccountId(int uploaderAccountId) {
    this.uploaderAccountId = uploaderAccountId;
  }
  //

  /**
   * Returns the ID of the photo.
   *
   * @return the ID of the photo
   */
  public int getId() {
    return this.id;
  }

  /**
   * Sets the ID of the photo.
   *
   * @param id the ID to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Returns the date of the photo.
   *
   * @return the date of the photo
   */
  public Date getDate() {
    return this.date;
  }

  /**
   * Sets the date of the photo.
   *
   * @param date the date to set
   */
  public void setDate(Date date) {
    this.date = date;
  }

  /**
   * Returns the location of the photo.
   *
   * @return the location of the photo
   */
  public Location getLocation() {
    return this.location;
  }

  /**
   * Returns the ID of the location of the photo.
   *
   * @return the ID of the location, or null if the location is not set
   */
  public Integer getLocationId() {
    if (this.location == null) {
      return null;
    }
    return this.location.getId();
  }

  /**
   * Sets the location of the photo.
   *
   * @param location the location to set
   */
  public void setLocation(Location location) {
    this.location = location;
  }

  /**
   * Returns the list of tags associated with the photo.
   *
   * @return the list of tags
   */
  public List<Tag> getTagged() {
    return this.tagged;
  }

  /**
   * Adds a tag to the photo.
   *
   * @param tag the tag to add
   */
  public void addTagged(Tag tag) {
    this.tagged.add(tag);
  }

  public void setTagged(List<Tag> tagged) {
    this.tagged = tagged;
  }

  /**
   * Returns the image data as a byte array.
   *
   * @return the image data as a byte array
   */
  public byte[] getImageAsBytes() {
    return this.image;
  }

  /**
   * Returns the image as a JavaFX Image object.
   *
   * @return the image as a JavaFX Image object
   */
  public javafx.scene.image.Image getImage() {
    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(getImageAsBytes())) {
      return new Image(inputStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }


  /**
   * Returns the adjusted image height.
   *
   * @return the adjusted image height
   */
  public int getAdjustedImageHeight(){
    return adjustedImageHeight;
  }

  /**
   * Returns the adjusted image width.
   *
   * @return the adjusted image width
   */
  public int getAdjustedImageWidth(){
    return adjustedImageWidth;
  }

  /**
   * Returns the default image height.
   * 
   * @return the default image height
   */
  public int getDefaultHeight()
  {
    return defaultHeight;
  }

  /**
   * Returns the default image width.
   * 
   * @return the default image width
   */
  public int getDefaultWidth()
  {
    return defaultWidth;
  }


  /**
   * Sets the image data.
   *
   * @param image the image data to set
   */
  public void setImage(byte[] image) {
    this.image = image;
  }

  /**
   * Returns the type of the image.
   *
   * @return the type of the image
   */
  public String getImageType() {
    return this.imageType;
  }

  /**
   * Returns the description of the photo.
   *
   * @return the description of the photo
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Sets the description of the photo.
   *
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns true if this photo has the minimum fields required to save, which is at least one of the following:
   * - A description
   * - A date
   * - A location
   * - At least one tagged person
   */
  public boolean hasMinimumFields() {
    return !(getDescription() == null || getDescription().isEmpty() || getDescription().isBlank())
            || getLocationId() != null
            || getDate() != null
            || !getTagged().isEmpty();
  }

  /**
   * Creates a Photo object from a file.
   *
   * @param file        the file containing the image data
   * @param description the description of the photo
   * @return a Photo object
   * @throws Exception if an error occurs while reading the file
   */
  public static Photo fromFile(File file, String description) throws Exception {
    // Transform the file into a Blob
    FileInputStream inputStream = new FileInputStream(file);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    byte[] buffer = new byte[4096];
    int bytesRead;

    while ((bytesRead = inputStream.read(buffer)) != -1) {
      outputStream.write(buffer, 0, bytesRead);
    }

    byte[] imageBytes = outputStream.toByteArray();
    String imageType = Files.probeContentType(Path.of(file.getPath()));

    return new Photo(imageBytes, imageType, description);
  }
}