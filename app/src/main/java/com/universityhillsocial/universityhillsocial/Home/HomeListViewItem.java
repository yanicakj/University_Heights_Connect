package com.universityhillsocial.universityhillsocial.Home;

/**
 * Created by Kubie on 3/27/18.
 */

public class HomeListViewItem {

    String name;
    String description;
    String location;
    String school;

    public HomeListViewItem() {
    }

    public HomeListViewItem(String name, String description, String location, String school) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.school = school;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}
