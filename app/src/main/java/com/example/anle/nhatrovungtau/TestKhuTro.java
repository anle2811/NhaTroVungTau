package com.example.anle.nhatrovungtau;

public class TestKhuTro {

    private int Idkhutro;

    public int getIdkhutro() {
        return Idkhutro;
    }

    public void setIdkhutro(int idkhutro) {
        Idkhutro = idkhutro;
    }

    private double Lat;
    private double Lng;
    private String TenKhu;
    private int SoPhong;
    private String DiaChi;
    private String Mota;
    private String AnhKhu;
    private String ThanhPho;
    private String Phuong;

    public String getPhuong() {
        return Phuong;
    }

    public void setPhuong(String phuong) {
        Phuong = phuong;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLng() {
        return Lng;
    }

    public void setLng(double lng) {
        Lng = lng;
    }

    public String getMota() {
        return Mota;
    }

    public void setMota(String mota) {
        Mota = mota;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public TestKhuTro() {
    }

    public TestKhuTro(int Idkhutro,double lat, double lng, String tenKhu, int soPhong, String diaChi, String mota, String anhKhu, String thanhPho, String phuong) {
        this.Idkhutro=Idkhutro;
        Lat = lat;
        Lng = lng;
        TenKhu = tenKhu;
        SoPhong = soPhong;
        DiaChi = diaChi;
        Mota = mota;
        AnhKhu = anhKhu;
        ThanhPho = thanhPho;
        Phuong=phuong;
    }

    public String getAnhKhu() {
        return AnhKhu;
    }

    public void setAnhKhu(String anhKhu) {
        AnhKhu = anhKhu;
    }

    public String getTenKhu() {
        return TenKhu;
    }

    public void setTenKhu(String tenKhu) {
        TenKhu = tenKhu;
    }

    public String getThanhPho() {
        return ThanhPho;
    }

    public void setThanhPho(String thanhPho) {
        ThanhPho = thanhPho;
    }

    public int getSoPhong() {
        return SoPhong;
    }

    public void setSoPhong(int soPhong) {
        SoPhong = soPhong;
    }
}
