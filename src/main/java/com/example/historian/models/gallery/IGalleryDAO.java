package com.example.historian.models.gallery;

import java.util.Date;
import java.util.List;

/**
 * The IGalleryDAO interface provides methods for performing CRUD operations on Gallery objects.
 */
public interface IGalleryDAO {

  /**
   * Adds a new gallery to the data store.
   *
   * @param gallery the Gallery object to be added
   */
  public String addGallery(String title, Date fromDate, Date toDate, int location, int person);

  /**
   * Retrieves a gallery by its ID.
   *
   * @param galleryId the ID of the gallery to be retrieved
   * @return the Gallery object with the specified ID, or null if not found
   */
  public Gallery getGalleryByKey(String galleryKey);

  public Boolean checkIfGalleryExistsByKey(String galleryKey);
}