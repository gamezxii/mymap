package com.example.nitsarut.mymap.prefs;

/**
 * Created by Nitsarut on 1/9/2018.
 */

public class Product {
    private int id;
    private String title;
    private String shortdesc;
    private String image;
    private String status;
    private String namerecrive;
    private String datesuc;
    private String image_after1;



    public Product(int id, String title, String shortdesc, String image,String status , String namerecrive ,String datesuc
            , String image_after) {
        this.id = id;
        this.title = title;
        this.shortdesc = shortdesc;
        this.image = image;
        this.status = status;
        this.namerecrive = namerecrive;
        this.datesuc = datesuc;
        this.image_after1 = image_after;

    }

    public String getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getShortdesc() {
        return shortdesc;
    }


    public String getImage() {
        return image;
    }

    public String getNamerecrive() {
        return namerecrive;
    }

    public String getDatesuc() {
        return datesuc;
    }

    public String getImage_after1() {
        return image_after1;
    }
}
