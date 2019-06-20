package com.example.anle.nhatrovungtau.PhpDB;
import com.example.anle.nhatrovungtau.ChuTroActivity;
import com.example.anle.nhatrovungtau.DangNhapActivity;
import com.example.anle.nhatrovungtau.KhuTroPhongTro.KhuTroActivity;
import com.example.anle.nhatrovungtau.dkTKActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
    }
    @Override
    protected void onPreExecute() { //Được gọi đầu tiên khi tiến trình được kích hoạt
        super.onPreExecute();
        switch (this.action){
            case Api.actionExistCheck:
            case Api.actionTaoTK:
                dkTKActivity.prgbar_tientrinh.setVisibility(View.VISIBLE);
                break;
            case Api.actionDN:
                DangNhapActivity.prgbar_dangnhap.setVisibility(View.VISIBLE);
                break;
            case Api.actionLayTTchutro:
                ChuTroActivity.prgbar_layTTCT.setVisibility(View.VISIBLE);
                break;
            case Api.actionUpdateTTchutro:
                ChuTroActivity.prgbar_ttcn.setVisibility(View.VISIBLE);
                break;

            case Api.actionThemKhuTro:
                KhuTroActivity.prgbar_themkhutro.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    protected void onPostExecute(String s) { //Sau khi tiến trình kết thúc thì hàm này sẽ được gọi. Ta có thể lấy được kết quả trả về sau khi tiến trình kết thúc, ở đây.
        super.onPostExecute(s);
        if (s==""){
            switch (this.action){
                case Api.actionExistCheck:
                case Api.actionTaoTK:
                    dkTKActivity.prgbar_tientrinh.setVisibility(View.GONE);
                    Toast.makeText(context,"Loi ket noi, gui lai ma va thu lai",Toast.LENGTH_LONG).show();
                    break;
                case Api.actionDN:
                    DangNhapActivity.prgbar_dangnhap.setVisibility(View.GONE);
                    Toast.makeText(context,"Loi ket noi, vui long thu lai",Toast.LENGTH_LONG).show();
                    break;
                case Api.actionLayTTchutro:
                    ChuTroActivity.prgbar_layTTCT.setVisibility(View.GONE);
                    Toast.makeText(context,"Loi ket noi, vui long thu lai",Toast.LENGTH_LONG).show();
                    break;
                case Api.actionUpdateTTchutro:
                    ChuTroActivity.prgbar_ttcn.setVisibility(View.GONE);
                    Toast.makeText(context,"Loi ket noi, vui long thu lai",Toast.LENGTH_LONG).show();
                    break;
                case Api.actionThemKhuTro:
                    KhuTroActivity.prgbar_themkhutro.setVisibility(View.GONE);
                    Toast.makeText(context,"Loi ket noi, vui long thu lai",Toast.LENGTH_LONG).show();
                    break;
            }
        }else {

            Log.d("MainActivity", "String: " + s);
            try {
                JSONObject jsonObject = new JSONObject(s); // String s tra ve mot chuoi Json ta gán vào đối tượng json để lấy dữ liệu

                switch (this.action) {
                    case Api.actionExistCheck: {
                        dkTKActivity.prgbar_tientrinh.setVisibility(View.GONE);
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
                        dkTKActivity.prgbar_tientrinh.setVisibility(View.GONE);
                        if (!jsonObject.getBoolean("error")) {
                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            Log.d("dkTKActivity", "Message: " + jsonObject.getString("message"));
                            final XulyDK callBack = (XulyDK) mCallBack.get();
                            callBack.gotoDangNhap();
                        }
                    }
                    break;

                    case Api.actionDN: {
                        DangNhapActivity.prgbar_dangnhap.setVisibility(View.GONE);
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
                        if (!jsonObject.getBoolean("error")){
                            Toast.makeText(context, "Hoàn tất lấy thông tin", Toast.LENGTH_SHORT).show();
                            final TTChuTro callBack=(TTChuTro) mCallBack.get();
                            callBack.layTTchutro(new JSONObject(jsonObject.getString("chutro")));
                        }
                    }break;

                    case Api.actionUpdateTTchutro:{
                        if (!jsonObject.getBoolean("error")){
                            Toast.makeText(context,"Đã cập nhật",Toast.LENGTH_SHORT).show();
                            final TTChuTro callBack=(TTChuTro) mCallBack.get();
                            callBack.updateTT();
                        }else {
                            Toast.makeText(context,"Không thành công\nvui lòng thử lại.",Toast.LENGTH_SHORT).show();
                        }
                    }break;

                    case Api.actionThemKhuTro:{
                        KhuTroActivity.prgbar_themkhutro.setVisibility(View.GONE);
                        if (!jsonObject.getBoolean("error")){
                            Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
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
