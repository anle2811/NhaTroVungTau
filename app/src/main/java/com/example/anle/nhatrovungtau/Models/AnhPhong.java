package com.example.anle.nhatrovungtau.Models;

public class AnhPhong {
    private String Idimg;
    private String Url;
    public AnhPhong(){}

    public AnhPhong(String idimg, String url) {
        Idimg = idimg;
        Url = url;
    }

    public String getIdimg() {
        return Idimg;
    }

    public void setIdimg(String idimg) {
        Idimg = idimg;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
