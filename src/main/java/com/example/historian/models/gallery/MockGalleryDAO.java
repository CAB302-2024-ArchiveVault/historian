package com.example.historian.models.gallery;

import java.util.ArrayList;
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
  public void addGallery(Gallery gallery) {
    gallery.setId(autoIncrementId);
    autoIncrementId++;
    galleries.add(gallery);
  }

  @Override
  public void updateGallery(Gallery gallery) {
    for (int i = 0; i < galleries.size(); i++) {
      if (galleries.get(i).getId() == gallery.getId()) {
        galleries.set(i, gallery);
        break;
      }
    }
  }

  @Override
  public void removeGallery(Gallery gallery) {
    galleries.remove(gallery);
  }

  @Override
  public Gallery getGallery(int galleryId) {
    for (Gallery gallery : galleries) {
      if (gallery.getId() == galleryId) {
        return gallery;
      }
    }
    return null;
  }

  @Override
  public Gallery getGallery(String galleryTitle) {
    for (Gallery gallery : galleries) {
      if (Objects.equals(gallery.getTitle(), galleryTitle)) {
        return gallery;
      }
    }
    return null;
  }

  @Override
  public List<Gallery> getAllGalleries() {
    return new ArrayList<>(galleries);
  }
}
