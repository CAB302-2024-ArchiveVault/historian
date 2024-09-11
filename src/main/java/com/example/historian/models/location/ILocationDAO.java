package com.example.historian.models.location;

import java.util.List;

public interface ILocationDAO {
    public void addLocation(Location location);

    public void removeLocation(Location location);

    public Location getLocation(int id);

    public List<Location> getAllLocations();
}
