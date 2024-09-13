package com.example.historian.models.gallery;

import java.util.List;

public interface IGalleryDAO {
  public void addGallery(Gallery gallery);

  public void updateGallery(Gallery gallery);

  public void removeGallery(Gallery gallery);

  public Gallery getGallery(int galleryId);

  public Gallery getGallery(String galleryTitle);

  public List<Gallery> getAllGalleries();
}
