package com.example.historian.models.gallery;

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
  public void addGallery(Gallery gallery);

  /**
   * Updates an existing gallery in the data store.
   *
   * @param gallery the Gallery object to be updated
   */
  public void updateGallery(Gallery gallery);

  /**
   * Removes a gallery from the data store.
   *
   * @param gallery the Gallery object to be removed
   */
  public void removeGallery(Gallery gallery);

  /**
   * Retrieves a gallery by its ID.
   *
   * @param galleryId the ID of the gallery to be retrieved
   * @return the Gallery object with the specified ID, or null if not found
   */
  public Gallery getGallery(int galleryId);

  /**
   * Retrieves a gallery by its title.
   *
   * @param galleryTitle the title of the gallery to be retrieved
   * @return the Gallery object with the specified title, or null if not found
   */
  public Gallery getGallery(String galleryTitle);

  /**
   * Retrieves all galleries from the data store.
   *
   * @return a list of all Gallery objects
   */
  public List<Gallery> getAllGalleries();
}
