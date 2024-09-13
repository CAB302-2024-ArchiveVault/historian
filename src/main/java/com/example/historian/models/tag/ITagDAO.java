package com.example.historian.models.tag;

import java.util.List;

/**
 * The ITagDAO interface provides methods for performing CRUD operations on Tag objects.
 */
public interface ITagDAO {

  /**
   * Adds a new tag to the data store.
   *
   * @param tag the Tag object to be added
   */
  public void addTag(Tag tag);

  /**
   * Removes a tag from the data store.
   *
   * @param tag the Tag object to be removed
   */
  public void removeTag(Tag tag);

  /**
   * Retrieves a tag by its ID.
   *
   * @param id the ID of the tag to be retrieved
   * @return the Tag object with the specified ID, or null if not found
   */
  public Tag getTag(int id);

  /**
   * Retrieves all tags from the data store.
   *
   * @return a list of all Tag objects
   */
  public List<Tag> getAllTags();
}