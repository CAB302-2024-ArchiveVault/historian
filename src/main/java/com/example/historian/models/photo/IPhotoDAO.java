package com.example.historian.models.photo;

import java.util.Date;
import java.util.List;

/**
 * The IPhotoDAO interface provides methods for performing CRUD operations on Photo objects.
 */
public interface IPhotoDAO {

  /**
   * Adds a new photo to the data store.
   *
   * @param photo the Photo object to be added
   */
  public int addPhoto(Photo photo);

  /**
   * Updates an existing photo in the data store.
   *
   * @param photo the Photo object to be updated
   */
  public void updatePhoto(Photo photo);

  /**
   * Removes a photo from the data store.
   *
   * @param photo the Photo object to be removed
   */
  public void removePhoto(Photo photo);

  /**
   * Retrieves a photo by its ID.
   *
   * @param photoId the ID of the photo to be retrieved
   * @return the Photo object with the specified ID, or null if not found
   */
  public Photo getPhoto(int photoId);

  /**
   * Retrieves all photos from the data store.
   *
   * @return a list of all Photo objects
   */
  public List<Photo> getAllPhotos();

  /**
   * Retrieves all photos from the data store that were taken within a specified date range.
   *
   * @param startDate the start date of the range
   * @param endDate the end date of the range
   * @return a list of all Photo objects taken within the specified date range
   */
  public List<Photo> getPhotosByFilter(Date startDate, Date endDate, int location, int person);

  /**
   * Retrieves all photo IDs from the data store.
   *
   * @return a list of all photo IDs
   */
  public List<Integer> getAllPhotoIDs();
}