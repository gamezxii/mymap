package com.example.nitsarut.mymap.prefs;

/**
 * Created by Nitsarut on 2/27/2018.
 */

public class Liststatus {
    private int id;
    private String image;
    private String status;
    private String namesent;
    private String day;
    private String Latti;
    private String Longi;

    public Liststatus(int id, String image, String status, String namesent, String day , String Latti , String Longi) {
        this.id = id;
        this.image = image;
        this.status = status;
        this.namesent = namesent;
        this.day = day;
        this.Latti = Latti;
        this.Longi = Longi;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getStatus() {
        return status;
    }

    public String getNamesent() {
        return namesent;
    }

    public String getDay() {
        return day;
    }

    public String getLatti() {
        return Latti;
    }

    public String getLongi() {
        return Longi;
    }
}
