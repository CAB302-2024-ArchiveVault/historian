package com.example.historian.utils;

import javafx.beans.property.SimpleBooleanProperty;

/**
 * A class that contains shared properties that can be used across the application.
 */
public class SharedProperties {
    /**
     * A boolean property that represents whether the image has been updated.
     */
    public static final SimpleBooleanProperty imageUpdated = new SimpleBooleanProperty(false);

    /**
     * A boolean property that represents whether the gallery code has been updated.
     */
    public static final SimpleBooleanProperty galleryCodeState = new SimpleBooleanProperty(false);
}
