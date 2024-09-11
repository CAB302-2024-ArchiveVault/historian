package com.example.historian.models.photo;

import com.example.historian.models.location.Location;
import com.example.historian.models.tag.Tag;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Photo {
    private int id;
    private Date date;
    private String description;
    private List<Tag> tagged;
    private Location location;
    // This is the data type used for storing images and large files.
    private Blob image;

    // If there is no description an empty string can be passed.
    // Because there are so many different permutations of arguments that can
    // be passed into the constructor I have just decided to keep it at requiring
    // an image where other metadata can be added after creation.
    public Photo(Blob image, String description) {
        this.image = image;
        this.description = description;
        date = null;
        tagged = new ArrayList<>();
        location = null;
    }

    public int getId() {return this.id;}
    public void setId(int id) {this.id = id;}

    public Date getDate() {return this.date;}
    public void setDate(Date date) {this.date = date;}

    public Location getLocation() {return this.location;}
    public void setLocation(Location location) {this.location = location;}

    public List<Tag> getTagged() {return this.tagged;}
    public void addTagged(Tag tag) {this.tagged.add(tag);}
    public void addTagged(List<Tag> tags) {this.tagged.addAll(tags);}

    public Blob getImage() {return this.image;}
    public void setImage(Blob image) {this.image = image;}

    public String getDescription() {return this.description;}
    public void setDescription(String description) {this.description = description;}

}
