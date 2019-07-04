package com.example.anle.nhatrovungtau.PhpDB;

public class Api {
    private static final String ROOT_URL="https://dbnhatrovungtau.000webhostapp.com/PHPNhaTro/API.php?apicall=";

    public static final String actionTaoTK="taoTaiKhoan";
    public static final String actionExistCheck="AccExistCheck";
    public static final String actionDN="kiemtraDN";
    public static final String actionLayTTchutro="layTTchutro";
    public static final String actionUpdateTTchutro="updateTTchutro";
    public static final String actionThemKhuTro="themKhuTro";
    public static final String actionLayDSkhutro="layDSkhutro";
    public static final String actionThemPhongTro="themPhongTro";
    public static final String actionLayDSphongtro="layDSphong";
    public static final String actionLayAnhPhong="layAnhPhong";
    public static final String actionXoaAnhPhong="xoaRowAnhPhong";
    public static final String actionThemAnhPhong="themAnhPhong";
    public static final String actionCapNhatGhep="capNhatGhepPhong";
    public static final String actionCapNhatTTPhong="capNhatTTPhong";
    public static final String actionCapNhatNguoi="capNhatSoNPhong";

    public static final String URL_TAO_TAIKHOAN=ROOT_URL+actionTaoTK;
    public static final String URL_ACC_EXIST_CHECK=ROOT_URL+actionExistCheck;
    public static final String URL_KIEMTRA_DN=ROOT_URL+actionDN;
    public static final String URL_LAY_TT_CHUTRO=ROOT_URL+actionLayTTchutro;
    public static final String URL_UPDATE_TT_CHUTRO=ROOT_URL+actionUpdateTTchutro;
    public static final String URL_THEM_KHUTRO=ROOT_URL+actionThemKhuTro;
    public static final String URL_LAY_DS_KHUTRO=ROOT_URL+actionLayDSkhutro;
    public static final String URL_THEM_PHONGTRO=ROOT_URL+actionThemPhongTro;
    public static final String URL_LAY_DS_PHONGTRO=ROOT_URL+actionLayDSphongtro;
    public static final String URL_LAY_ANHPHONG=ROOT_URL+actionLayAnhPhong;
    public static final String URL_XOA_ANHPHONG=ROOT_URL+actionXoaAnhPhong;
    public static final String URL_THEM_ANHPHONG=ROOT_URL+actionThemAnhPhong;
    public static final String URL_CAPNHAT_GHEP=ROOT_URL+actionCapNhatGhep;
    public static final String URL_CAPNHAT_TTPHONG=ROOT_URL+actionCapNhatTTPhong;
    public static final String URL_CAPNHAT_SNGUOI=ROOT_URL+actionCapNhatNguoi;
}
