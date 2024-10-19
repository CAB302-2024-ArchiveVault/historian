package com.example.historian.models.gallery;

import java.util.Date;

/**
 * The IGalleryDAO interface provides methods for performing CRUD operations on Gallery objects.
 */
public interface IGalleryDAO {

  /**
   * Adds a new gallery to the data store.
   *
   */
  public String addGallery(Date fromDate, Date toDate, int location, int person);

  /**
   * Retrieves a gallery by its ID.
   *
   * @return the Gallery object with the specified ID, or null if not found
   */
  public Gallery getGalleryByKey(String galleryKey);

  /**
   * Checks if a gallery exists in the data store.
   * @param galleryKey the key of the gallery to check
   * @return true if the gallery exists, false otherwise
   */
  public Boolean checkIfGalleryExistsByKey(String galleryKey);
}