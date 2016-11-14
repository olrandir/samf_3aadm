package com.olrandir.samf.models;

/**
 * Created by olrandir on 13/11/2016.
 */

public class SamfPerson {
    String name;
    String location;

    public SamfPerson(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
