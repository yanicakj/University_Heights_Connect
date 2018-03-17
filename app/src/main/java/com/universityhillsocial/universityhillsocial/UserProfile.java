package com.universityhillsocial.universityhillsocial;

/**
 * Created by Kubie on 3/16/18.
 */

public class UserProfile {
    public String userEmail;
    public String userFirstName;
    public String userLastName;

    public UserProfile() {

    }

    public UserProfile(String userEmail, String userFirstName, String userLastName) {
        this.userEmail = userEmail;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }
}
