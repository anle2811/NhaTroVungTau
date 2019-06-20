package com.example.anle.nhatrovungtau.PhpDB;

public class Api {
    private static final String ROOT_URL="https://dbnhatrovungtau.000webhostapp.com/PHPNhaTro/API.php?apicall=";

    public static final String actionTaoTK="taoTaiKhoan";
    public static final String actionExistCheck="AccExistCheck";
    public static final String actionDN="kiemtraDN";
    public static final String actionLayTTchutro="layTTchutro";
    public static final String actionUpdateTTchutro="updateTTchutro";
    public static final String actionThemKhuTro="themKhuTro";

    public static final String URL_TAO_TAIKHOAN=ROOT_URL+actionTaoTK;
    public static final String URL_ACC_EXIST_CHECK=ROOT_URL+actionExistCheck;
    public static final String URL_KIEMTRA_DN=ROOT_URL+actionDN;
    public static final String URL_LAY_TT_CHUTRO=ROOT_URL+actionLayTTchutro;
    public static final String URL_UPDATE_TT_CHUTRO=ROOT_URL+actionUpdateTTchutro;
    public static final String URL_THEM_KHUTRO=ROOT_URL+actionThemKhuTro;
}
