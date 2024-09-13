package com.example.historian.models.tag;

import java.util.ArrayList;
import java.util.List;

public class MockTagDAO implements ITagDAO {
  public static ArrayList<Tag> tags = new ArrayList<>();
  private static int autoIncrementedId = 0;

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
}
