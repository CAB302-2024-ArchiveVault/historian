package com.example.historian.models.tag;

import java.util.ArrayList;
import java.util.List;

/**
 * The MockTagDAO class provides methods for performing CRUD operations on Tag objects
 * using an in-memory list.
 */
public class MockTagDAO implements ITagDAO {
  public static ArrayList<Tag> tags = new ArrayList<>();
  private static int autoIncrementedId = 0;

  /**
   * Constructs a MockTagDAO object.
   */
  public MockTagDAO() {
  }

  @Override
  public void addTag(Tag tag) {
    tag.setId(autoIncrementedId);
    autoIncrementedId++;
    tags.add(tag);
  }

  @Override
  public void removeTag(Tag tag) {
    tags.remove(tag);
  }

  @Override
  public Tag getTag(int id) {
    for (Tag tag : tags) {
      if (tag.getId() == id) {
        return tag;
      }
    }
    return null;
  }

  @Override
  public List<Tag> getAllTags() {
    return new ArrayList<>(tags);
  }

  @Override
  public List<Tag> getTagsForPhoto(int photoId) {
    List<Tag> filteredTags = new ArrayList<>();
    for (Tag tag : tags) {
      if (tag.getPhotoId() == photoId) {
        filteredTags.add(tag);
      }
    }
    return filteredTags;
  }
}
