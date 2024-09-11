package com.example.historian.models.photo;

import java.util.List;

public interface IPhotoDAO {
    public void addPhoto(Photo photo);

    public void updatePhoto(Photo photo);

    public void removePhoto(Photo photo);

    public Photo getPhoto(int photoId);

    public List<Photo> getAllPhotos();
}
