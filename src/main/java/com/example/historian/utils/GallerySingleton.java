package com.example.historian.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A singleton class that manages the photo queue for the gallery.
 */
public class GallerySingleton {
  /**
   * A public record that represents an item in the photo queue.
   * @param photoId The ID of the photo in the queue
   * @param openToEditMode Whether the photo should be opened in edit mode
   */
  public record PhotoQueueItem(int photoId, boolean openToEditMode) {
  }

  /**
   * The singleton instance of the GallerySingleton class.
   */
  private static GallerySingleton instance = new GallerySingleton();

  /**
   * The current page of the gallery.
   */
  private int currentPage = 0;

  /**
   * The list of items in the photo queue.
   */
  private List<PhotoQueueItem> photoQueue = new ArrayList<>();

  /**
   * Constructs a GallerySingleton object.
   */
  private GallerySingleton() {
  }

  /**
   * The method used to get the singleton instance that can be used to manage the photo queue for the gallery.
   * @return An instance of the GallerySingleton singleton
   */
  public static synchronized GallerySingleton getInstance() {
    return instance;
  }

  /**
   * Gets the current page of the gallery.
   * @return The current page of the gallery
   */
  public int getCurrentPage() {
    return currentPage;
  }

  /**
   * Sets the current page of the gallery.
   * @param currentPage The current page of the gallery
   */
  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

  /**
   * Checks if the photo queue is empty.
   * @return True if the photo queue is empty, false otherwise
   */
  public boolean isPhotoQueueEmpty() {
    return this.photoQueue.isEmpty();
  }

  /**
   * Pops the first item from the photo queue.
   * @return The first item in the photo queue
   */
  public PhotoQueueItem popFromPhotoQueue() {
    PhotoQueueItem item = this.photoQueue.getFirst();
    removeFromPhotoQueue(item);
    return item;
  }

  /**
   * Gets the ID of the first photo in the queue.
   * @return The ID of the first photo in the queue
   */
  public int firstPhotoInQueueID(){
    PhotoQueueItem item = this.photoQueue.getFirst();
    return item.photoId;
  }

  /**
   * Adds an item to the photo queue.
   * @param item The item to add to the photo queue
   */
  public void addToPhotoQueue(PhotoQueueItem item) {
    this.photoQueue.add(item);
  }

  /**
   * Removes an item from the photo queue.
   * @param item The item to remove from the photo queue
   */
  public void removeFromPhotoQueue(PhotoQueueItem item) {
    this.photoQueue.remove(item);
  }
}
