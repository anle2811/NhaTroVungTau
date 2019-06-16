package com.example.anle.nhatrovungtau.Models;

public class ThanhPho {
    private String Tenhienthi;
    private String Tentp;
    public ThanhPho(){

    }

    public ThanhPho(String tenhienthi, String tentp) {
        Tenhienthi = tenhienthi;
        Tentp = tentp;
    }

    public String getTenhienthi() {
        return Tenhienthi;
    }

    public void setTenhienthi(String tenhienthi) {
        Tenhienthi = tenhienthi;
    }

    public String getTentp() {
        return Tentp;
    }

    public void setTentp(String tentp) {
        Tentp = tentp;
    }
}
