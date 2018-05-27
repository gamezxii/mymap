package com.example.nitsarut.mymap.prefs;

/**
 * Created by Nitsarut on 2/19/2018.
 */

public class Contact {
    private int id;
    private String image;
    private String status;
    private String namesent;
    private String des;
    private String current;
    private String datetime;

    public Contact(int id, String image , String status ,  String namesent , String des , String current,String datetime) {
        this.id = id;
        this.image = image;
        this.status = status;
        this.namesent = namesent;
        this.des = des;
        this.current = current;
        this.datetime = datetime;

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
    public String getDes() {
        return des;
    }

    public String getCurrent() {
        return current;
    }

    public String getDatetime() {
        return datetime;
    }
}
