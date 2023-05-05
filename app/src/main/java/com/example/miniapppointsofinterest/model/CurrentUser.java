package com.example.miniapppointsofinterest.model;

import com.example.miniapppointsofinterest.model.user.UserBoundary;

public class CurrentUser {
    private static CurrentUser instance = null;
    private UserBoundary theUser;

    private CurrentUser(UserBoundary theUser) {
        this.theUser = theUser;
    }

    public static synchronized void init(UserBoundary theUser) {
        instance = new CurrentUser(theUser);
    }

    public static synchronized CurrentUser getInstance() {
        return instance;
    }

    public UserBoundary getTheUser() {
        return theUser;
    }

    public void setTheUser(UserBoundary theUser) {
        this.theUser = theUser;
    }

    @Override
    public String toString() {
        return "CurrentUser{" +
                "theUser=" + theUser +
                '}';
    }
}
