package com.example.historian.models.photo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The MockPhotoDAO class provides methods for performing CRUD operations on Photo objects
 * using an in-memory list.
 */
public class MockPhotoDAO implements IPhotoDAO {
  public static ArrayList<Photo> photos = new ArrayList<>();
  private static int autoIncrementId = 0;

  /**
   * Constructs a MockPhotoDAO object.
   */
  public MockPhotoDAO() {
  }

  @Override
  public int addPhoto(Photo photo) {
    photo.setId(autoIncrementId);
    autoIncrementId++;
    photos.add(photo);
    return autoIncrementId;
  }

  @Override
  public void updatePhoto(Photo photo) {
    for (int i = 0; i < photos.size(); i++) {
      if (photos.get(i).getId() == photo.getId()) {
        photos.set(i, photo);
        break;
      }
    }
  }

  @Override
  public void removePhoto(Photo photo) {
    photos.remove(photo);
  }

  @Override
  public Photo getPhoto(int photoId) {
    for (Photo photo : photos) {
      if (photo.getId() == photoId) {
        return photo;
      }
    }
    return null;
  }

  @Override
  public List<Photo> getAllPhotos() {
    return new ArrayList<>(photos);
  }

  @Override
  public List<Photo> getPhotosByFilter(Date startDate, Date endDate, int location, int person) {
    return new ArrayList<>(photos);
  }
}
