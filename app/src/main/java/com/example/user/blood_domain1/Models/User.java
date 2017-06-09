package com.example.user.blood_domain1.Models;

/**
 * Created by hp on 08-06-2017.
 */

public class User {
    private String username;
    private String password;
    private String address;
    private double lat;
    private double lang;
    private String bloodGroup;

    public User(String username, String password, String address, double lat, double lang, String bloodGroup) {
        this.username = username;
        this.password = password;
        this.address = address;
        this.lat = lat;
        this.lang = lang;
        this.bloodGroup=bloodGroup;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
}
