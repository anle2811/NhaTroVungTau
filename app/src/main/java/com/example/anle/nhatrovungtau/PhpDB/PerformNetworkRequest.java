package com.example.anle.nhatrovungtau.PhpDB;
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
    private WeakReference<ResponseInActivity> mCallBack;
    String url; //Chuoi url ket noi toi database thuc hien yeu cau trong code php
    private String action;
    HashMap<String,String> params; //HashMap là 1 kiểu đối tượng lưu giá trị theo cặp key / value.
    // Đây là tham số truyền vào với các tham số đi kèm với giá trị của chúng
    int requestCode; //Mã yêu cầu: để kiểm tra phải sử dụng phương thức nào
    Context context;
    public ResponseInActivity response=null;
    public PerformNetworkRequest(String url,String action,HashMap<String,String> params,int requestCode,Context context,ResponseInActivity callback){ //Phương thức khởi tạo
        this.url=url;
        this.action=action;
        this.params=params;
        this.requestCode=requestCode;
        this.context=context;
        this.mCallBack=new WeakReference<>(callback);
    }
    public interface ResponseInActivity{
        void exeTaotaikhoan();
        void gotoDangNhap();
    }
    @Override
    protected void onPreExecute() { //Được gọi đầu tiên khi tiến trình được kích hoạt
        super.onPreExecute();
        dkTKActivity.prgbar_tientrinh.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(String s) { //Sau khi tiến trình kết thúc thì hàm này sẽ được gọi. Ta có thể lấy được kết quả trả về sau khi tiến trình kết thúc, ở đây.
        super.onPostExecute(s);
        dkTKActivity.prgbar_tientrinh.setVisibility(View.GONE);
        Log.d("MainActivity","String: "+s);
        try{
            JSONObject jsonObject=new JSONObject(s); // String s tra ve mot chuoi Json ta gán vào đối tượng json để lấy dữ liệu


            try {
                if (jsonObject.getString("action").equals("checkTK")){
                    if (!jsonObject.getBoolean("error")){ // Lấy ra giá trị của key 'error' trong chuỗi json. Nếu giá trị khác true, mean: value=false thì:
                        Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                        final ResponseInActivity callBack=mCallBack.get();
                        if (callBack!=null){
                            callBack.exeTaotaikhoan();
                        }
                        Log.d("dkTKActivity","Message: "+jsonObject.getString("message"));
                    }
                    if (jsonObject.getBoolean("error")){
                        Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                        Log.d("dkTKActivity","Message: "+jsonObject.getString("message"));
                    }
                }
            }catch (Exception e){
                Log.d("dkTKActivity","JsonObject: "+e.getMessage());
            }


            if (!jsonObject.getBoolean("error")){ // Lấy ra giá trị của key 'error' trong chuỗi json. Nếu giá trị khác true, mean: value=false thì:
                Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                Log.d("dkTKActivity","Message: "+jsonObject.getString("message"));
                if (this.action.equals("taoTaiKhoan")){
                    final ResponseInActivity callBack=mCallBack.get();
                    callBack.gotoDangNhap();
                }
            }
        }catch (JSONException e){
            Log.d("MainActivity","Fail: "+e.toString());
            e.printStackTrace();
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
