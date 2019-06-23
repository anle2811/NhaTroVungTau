package com.example.anle.nhatrovungtau.Models;

public class PhongTro {
    private String Idphong;
    private String Giaphong;
    private String Dientich;
    private int Trangthai;
    private int Ghep;
    private String Img;
    private String Mota;

    public PhongTro() {
    }

    public PhongTro(String idphong, String giaphong, String dientich, int trangthai, int ghep, String img, String mota) {
        Idphong = idphong;
        Giaphong = giaphong;
        Dientich = dientich;
        Trangthai = trangthai;
        Ghep = ghep;
        Img = img;
        Mota = mota;
    }

    public String getIdphong() {
        return Idphong;
    }

    public void setIdphong(String idphong) {
        Idphong = idphong;
    }

    public String getGiaphong() {
        return Giaphong;
    }

    public void setGiaphong(String giaphong) {
        Giaphong = giaphong;
    }

    public String getDientich() {
        return Dientich;
    }

    public void setDientich(String dientich) {
        Dientich = dientich;
    }

    public int getTrangthai() {
        return Trangthai;
    }

    public void setTrangthai(int trangthai) {
        Trangthai = trangthai;
    }

    public int getGhep() {
        return Ghep;
    }

    public void setGhep(int ghep) {
        Ghep = ghep;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }

    public String getMota() {
        return Mota;
    }

    public void setMota(String mota) {
        Mota = mota;
    }
}
