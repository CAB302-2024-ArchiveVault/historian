package com.example.historian.models.photo;

import java.util.ArrayList;
import java.util.List;

public class MockPhotoDAO implements IPhotoDAO {
    public static ArrayList<Photo> photos = new ArrayList<>();
    private static int autoIncrementId = 0;

    public MockPhotoDAO() {}

    @Override
    public void addPhoto(Photo photo) {
        photo.setId(autoIncrementId);
        autoIncrementId++;
        photos.add(photo);
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
    public void removePhoto(Photo photo) { photos.remove(photo); }

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
    public List<Photo> getAllPhotos() { return new ArrayList<>(photos); }
}
