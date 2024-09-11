package com.example.historian.models.tag;

import java.util.List;

public interface ITagDAO {
    public void addTag(Tag tag);

    public void removeTag(Tag tag);

    public Tag getTag(int id);

    public List<Tag> getAllTags();
}
