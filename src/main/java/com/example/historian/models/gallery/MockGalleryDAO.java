package com.example.historian.models.gallery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * The MockGalleryDAO class is a mock implementation of the IGalleryDAO interface.
 * It provides methods for performing CRUD operations on Gallery objects using an in-memory list.
 */
public class MockGalleryDAO implements IGalleryDAO {
  public static ArrayList<Gallery> galleries = new ArrayList<>();
  private static int autoIncrementId = 0;

  /**
   * Constructs a MockGalleryDAO object.
   */
  public MockGalleryDAO() {
  }

  /**
   * Adds a new gallery to the in-memory list.
   *
   * @param gallery the Gallery object to be added
   */
  @Override
  public String addGallery(String title, Date fromDate, Date toDate, int location, int person) {
    autoIncrementId++;
    return "HASH!@";
  }

  @Override
  public Gallery getGalleryByKey(String galleryKey) {
    return null;
  }

  @Override
  public Boolean checkIfGalleryExistsByKey(String galleryKey) {
    return null;
  }
}
