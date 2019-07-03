package com.example.anle.nhatrovungtau.Models;

import android.graphics.Bitmap;

public class AnhPhong {
    private String Idimg;
    private String Url;
    private boolean type;
    private Bitmap bitmap;

    public AnhPhong(){}

    public AnhPhong(Bitmap bitmap,boolean type){
        this.bitmap=bitmap;
        this.type=type;
    }

    public AnhPhong(String idimg, String url,boolean type) {
        Idimg = idimg;
        Url = url;
        this.type=type;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
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
