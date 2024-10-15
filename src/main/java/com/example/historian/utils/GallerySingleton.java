package com.example.historian.utils;

import java.util.ArrayList;
import java.util.List;

public class GallerySingleton {
  public record PhotoQueueItem(int photoId, boolean openToEditMode) {
  }

  private static GallerySingleton instance = new GallerySingleton();

  private int currentPage = 0;
  private List<PhotoQueueItem> photoQueue = new ArrayList<>();

  private GallerySingleton() {
  }

  public static synchronized GallerySingleton getInstance() {
    return instance;
  }

  public int getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

  public boolean isPhotoQueueEmpty() {
    return this.photoQueue.isEmpty();
  }

  public PhotoQueueItem popFromPhotoQueue() {
    PhotoQueueItem item = this.photoQueue.getFirst();
    removeFromPhotoQueue(item);
    return item;
  }

  public int firstPhotoInQueueID(){
    PhotoQueueItem item = this.photoQueue.getFirst();
    return item.photoId;
  }

  public void addToPhotoQueue(PhotoQueueItem item) {
    this.photoQueue.add(item);
  }

  public void removeFromPhotoQueue(PhotoQueueItem item) {
    this.photoQueue.remove(item);
  }
}
