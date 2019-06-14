package com.example.anle.nhatrovungtau.PhpDB;

public class Api {
    private static final String ROOT_URL="https://dbnhatrovungtau.000webhostapp.com/PHPNhaTro/API.php?apicall=";
    public static final String actionTaoTK="taoTaiKhoan";
    public static final String actionExistCheck="AccExistCheck";
    public static final String URL_TAO_TAIKHOAN=ROOT_URL+actionTaoTK;
    public static final String URL_ACC_EXIST_CHECK=ROOT_URL+actionExistCheck;
}
