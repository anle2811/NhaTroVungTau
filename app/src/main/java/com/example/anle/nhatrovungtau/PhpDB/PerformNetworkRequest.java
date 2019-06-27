package com.example.anle.nhatrovungtau.PhpDB;
import com.example.anle.nhatrovungtau.ChuTroActivity;
import com.example.anle.nhatrovungtau.DangNhapActivity;
import com.example.anle.nhatrovungtau.KhuTroPhongTro.KhuTroActivity;
import com.example.anle.nhatrovungtau.KhuTroPhongTro.PhongTroActivity;
import com.example.anle.nhatrovungtau.XemKhuTroPhongTro.ChiTietKhuTro;
import com.example.anle.nhatrovungtau.XemKhuTroPhongTro.ChiTietPhongTro;
import com.example.anle.nhatrovungtau.dkTKActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class PerformNetworkRequest extends AsyncTask<Void,Void,String> { //AsyncTask<Params, Progress, Result>
    // Những đối số nào không sử dụng trong quá trình thực thi tiến trình thì ta thay bằng Void.
    //Result: Là biến dùng để lưu trữ kết quả trả về sau khi tiến trình thực hiện xong.
    private WeakReference<Object> mCallBack;
    String url; //Chuoi url ket noi toi database thuc hien yeu cau trong code php
    private String action;
    HashMap<String,String> params; //HashMap là 1 kiểu đối tượng lưu giá trị theo cặp key / value.
    // Đây là tham số truyền vào với các tham số đi kèm với giá trị của chúng
    int requestCode; //Mã yêu cầu: để kiểm tra phải sử dụng phương thức nào
    Context context;
    public PerformNetworkRequest(String url,String action,HashMap<String,String> params,int requestCode,Context context,Object callback){ //Phương thức khởi tạo
        this.url=url;
        this.action=action;
        this.params=params;
        this.requestCode=requestCode;
        this.context=context;
        this.mCallBack=new WeakReference<>(callback);
    }
    public interface XulyDK{
        void exeTaotaikhoan();
        void gotoDangNhap();
    }
    public interface KiemtraDN{
        void DangNhap();

    }
    public interface TTChuTro{
        void layTTchutro(JSONObject jsonObject);
        void updateTT();
        void layDSkhutro(JSONArray dskhutro);
    }
    public interface DSPhongTro{
        void layDSphongtro(JSONArray dsphong);
    }
    public interface DSAnhPhong{
        void layDSanhphong(JSONArray dsUrlAnh);
    }
    @Override
    protected void onPreExecute() { //Được gọi đầu tiên khi tiến trình được kích hoạt
        super.onPreExecute();
        switch (this.action){
            case Api.actionExistCheck:
                dkTKActivity.ktTkDialog.show();
                break;
            case Api.actionTaoTK:
                dkTKActivity.taotkDialog.show();
                break;
            case Api.actionDN:
                DangNhapActivity.dialogLoad.show();
                break;
            case Api.actionLayTTchutro:
                ChuTroActivity.LayTTload.show();
                break;
            case Api.actionUpdateTTchutro:
                ChuTroActivity.TTCNload.show();
                break;

            case Api.actionThemKhuTro:
                KhuTroActivity.themKTdialog.show();
                break;
            case Api.actionLayDSkhutro:
                ChuTroActivity.DSkhutroLoad.show();
                break;
            case Api.actionThemPhongTro:
                PhongTroActivity.loadThemPhong.show();
                break;
            case Api.actionLayDSphongtro:
                ChiTietKhuTro.loadDSphong.show();
                break;
            case Api.actionLayAnhPhong:
                ChiTietPhongTro.loadAnhPhong.show();
                break;
        }

    }


    @Override
    protected void onPostExecute(String s) { //Sau khi tiến trình kết thúc thì hàm này sẽ được gọi. Ta có thể lấy được kết quả trả về sau khi tiến trình kết thúc, ở đây.
        super.onPostExecute(s);
        if (s==""){
            switch (this.action){
                case Api.actionExistCheck: dkTKActivity.ktTkDialog.cancel();
                    Toast.makeText(context,"Lỗi kết nối, không kiểm tra được tên tài khoản",Toast.LENGTH_LONG).show();
                    break;
                case Api.actionTaoTK:
                    dkTKActivity.taotkDialog.cancel();
                    Toast.makeText(context,"Lỗi kết nối, vui lòng gửi lại mã xác nhận để thử lại",Toast.LENGTH_LONG).show();
                    break;
                case Api.actionDN:
                    DangNhapActivity.dialogLoad.cancel();
                    Toast.makeText(context,"Lỗi kết nối, vui lòng kiểm tra mạng và thử lại",Toast.LENGTH_LONG).show();
                    break;
                case Api.actionLayTTchutro:
                    ChuTroActivity.LayTTload.cancel();
                    Toast.makeText(context,"Lỗi kết nối, vui lòng kiểm tra mạng và thử lại",Toast.LENGTH_LONG).show();
                    break;
                case Api.actionUpdateTTchutro:
                    ChuTroActivity.TTCNload.cancel();
                    Toast.makeText(context,"Lỗi kết nối, vui lòng kiểm tra mạng và thử lại",Toast.LENGTH_LONG).show();
                    break;
                case Api.actionThemKhuTro:
                    KhuTroActivity.themKTdialog.cancel();
                    Toast.makeText(context,"Lỗi kết nối, vui lòng kiểm tra mạng và thử lại",Toast.LENGTH_LONG).show();
                    break;
                case Api.actionLayDSkhutro:
                    ChuTroActivity.DSkhutroLoad.cancel();
                    Toast.makeText(context,"Lỗi mạng, vui lòng thử làm mới lại",Toast.LENGTH_LONG).show();
                    break;
                case Api.actionThemPhongTro:
                    PhongTroActivity.loadThemPhong.cancel();
                    Toast.makeText(context,"Lỗi kết nối, vui lòng kiểm tra mạng và thử lại",Toast.LENGTH_LONG).show();
                    break;
                case Api.actionLayDSphongtro:
                    ChiTietKhuTro.loadDSphong.cancel();
                    Toast.makeText(context,"Lỗi kết nối, vui lòng kiểm tra mạng và thử lại",Toast.LENGTH_LONG).show();
                    break;
                case Api.actionLayAnhPhong:
                    ChiTietPhongTro.loadAnhPhong.cancel();
                    Toast.makeText(context,"Lỗi kết nối, vui lòng kiểm tra mạng và thử lại",Toast.LENGTH_LONG).show();
                    break;
            }
        }else {

            Log.d("MainActivity", "String: " + s);
            try {
                JSONObject jsonObject = new JSONObject(s); // String s tra ve mot chuoi Json ta gán vào đối tượng json để lấy dữ liệu

                switch (this.action) {
                    case Api.actionExistCheck: {
                        dkTKActivity.ktTkDialog.cancel();
                        if (!jsonObject.getBoolean("error")) { // Lấy ra giá trị của key 'error' trong chuỗi json. Nếu giá trị khác true, mean: value=false thì:
                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            final XulyDK callBack = (XulyDK) mCallBack.get();
                            if (callBack != null) {
                                callBack.exeTaotaikhoan();
                            }
                            Log.d("dkTKActivity", "Message: " + jsonObject.getString("message"));
                        }
                        if (jsonObject.getBoolean("error")) {
                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            Log.d("dkTKActivity", "Message: " + jsonObject.getString("message"));
                        }
                    }
                    break;

                    case Api.actionTaoTK: {
                        dkTKActivity.taotkDialog.cancel();
                        if (!jsonObject.getBoolean("error")) {
                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            Log.d("dkTKActivity", "Message: " + jsonObject.getString("message"));
                            final XulyDK callBack = (XulyDK) mCallBack.get();
                            callBack.gotoDangNhap();
                        }
                    }
                    break;

                    case Api.actionDN: {
                        DangNhapActivity.dialogLoad.cancel();
                        if (!jsonObject.getBoolean("error")) {
                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            final KiemtraDN callBack = (KiemtraDN) mCallBack.get();
                            callBack.DangNhap();
                        }
                        if (jsonObject.getBoolean("error")) {
                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            Log.d("dkTKActivity", "Message: " + jsonObject.getString("message"));
                        }
                    }
                    break;

                    case Api.actionLayTTchutro:{
                        ChuTroActivity.LayTTload.cancel();
                        if (!jsonObject.getBoolean("error")){
                            Toast.makeText(context, "Hoàn tất lấy thông tin", Toast.LENGTH_SHORT).show();
                            final TTChuTro callBack=(TTChuTro) mCallBack.get();
                            callBack.layTTchutro(new JSONObject(jsonObject.getString("chutro")));
                        }
                    }break;

                    case Api.actionUpdateTTchutro:{
                        ChuTroActivity.TTCNload.cancel();
                        if (!jsonObject.getBoolean("error")){
                            Toast.makeText(context,"Đã cập nhật",Toast.LENGTH_SHORT).show();
                            final TTChuTro callBack=(TTChuTro) mCallBack.get();
                            callBack.updateTT();
                        }else {
                            Toast.makeText(context,"Không thành công\nvui lòng thử lại.",Toast.LENGTH_SHORT).show();
                        }
                    }break;

                    case Api.actionThemKhuTro:{
                        KhuTroActivity.themKTdialog.cancel();
                        if (!jsonObject.getBoolean("error")){
                            Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                        }
                    }break;

                    case Api.actionLayDSkhutro:{
                        ChuTroActivity.DSkhutroLoad.cancel();
                        if (!jsonObject.getBoolean("error")){
                            final TTChuTro callBack=(TTChuTro) mCallBack.get();
                            callBack.layDSkhutro(jsonObject.getJSONArray("khutros"));
                            Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context,"Lấy danh sách khu trọ thất bại",Toast.LENGTH_SHORT).show();
                        }
                    }break;

                    case Api.actionThemPhongTro:{
                        PhongTroActivity.loadThemPhong.cancel();
                        if (!jsonObject.getBoolean("error")){
                            Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                        }
                    }break;

                    case Api.actionLayDSphongtro:{
                        ChiTietKhuTro.loadDSphong.cancel();
                        if (!jsonObject.getBoolean("error")){
                            final DSPhongTro callBack=(DSPhongTro)mCallBack.get();
                            callBack.layDSphongtro(jsonObject.getJSONArray("phongtros"));
                            Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context,"Lấy danh sách phòng thất bại",Toast.LENGTH_SHORT).show();
                        }
                    }break;

                    case Api.actionLayAnhPhong:{
                        ChiTietPhongTro.loadAnhPhong.cancel();
                        if (!jsonObject.getBoolean("error")){
                            final DSAnhPhong callBack=(DSAnhPhong)mCallBack.get();
                            callBack.layDSanhphong(jsonObject.getJSONArray("anhphongs"));
                            Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context,"Lấy danh sách phòng thất bại",Toast.LENGTH_SHORT).show();
                        }
                    }break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected String doInBackground(Void... voids) { //Được thực thi trong quá trình tiến trình chạy nền
        XuLyYeuCauURL xuLyYeuCauURL=new XuLyYeuCauURL();
        if (requestCode==113){
            return xuLyYeuCauURL.sendPostRequest(url,params);
        }
        return null;
    }
}
