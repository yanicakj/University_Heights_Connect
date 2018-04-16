package com.universityhillsocial.universityhillsocial.Home;

import android.widget.ImageView;

/**
 * Created by Kubie on 3/27/18.
 */

public class HomeListViewItem {

    String name;
    String description;
    String location;
    String school;
    String poster;
    String image;

    public HomeListViewItem() {
    }

    public HomeListViewItem(String name, String description, String location, String school, String poster, String image) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.school = school;
        this.poster = poster;
        this.image = image;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
