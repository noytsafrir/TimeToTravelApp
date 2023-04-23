package com.example.miniapppointsofinterest.model;

public class UserIdBoundary {
    private String superapp;
    private String email;

    public UserIdBoundary() {}

    public UserIdBoundary(String superapp, String email) {
        this.superapp = superapp;
        this.email = email;
    }

    public String getSuperapp() {
        return superapp;
    }

    public void setSuperapp(String superapp) {
        this.superapp = superapp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserIdBoundary{" +
                "superapp='" + superapp + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}