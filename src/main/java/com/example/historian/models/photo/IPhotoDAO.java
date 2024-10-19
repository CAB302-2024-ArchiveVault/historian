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

  public List<Photo> getPhotosByFilter(Date startDate, Date endDate, int location, int person);

  public List<Integer> getAllPhotoIDs();
}