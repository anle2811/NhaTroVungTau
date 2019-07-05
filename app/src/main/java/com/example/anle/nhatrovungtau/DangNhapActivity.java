package com.example.anle.nhatrovungtau;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.anle.nhatrovungtau.PhpDB.Api;
import com.example.anle.nhatrovungtau.PhpDB.PerformNetworkRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DangNhapActivity extends AppCompatActivity implements PerformNetworkRequest.KiemtraDN{
    private AlertDialog.Builder builder;
    private static final int REQUEST_CODE=113;
    private SessionManager loginManager;
    private Button btn_dangkytk;
    private Button btn_dangnhap;
    private EditText edt_taikhoan;
    private TextInputEditText edt_matkhau;
    public static DialogLoad dialogLoad;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangnhapchutro);
        initAll();
        TK_MK_VuaDK();
        dangnhap();
        dangky();
        initDialogTBthieu();
    }
    public void initAll(){
        btn_dangkytk=findViewById(R.id.btn_dangkytk);
        btn_dangnhap=findViewById(R.id.btn_dangnhap);
        edt_taikhoan=findViewById(R.id.edt_taikhoan);
        edt_matkhau=findViewById(R.id.edt_matkhau);
        dialogLoad=new DialogLoad(this,"Đang Đăng Nhập...");
        loginManager=new SessionManager(getApplicationContext());
    }
    public void TK_MK_VuaDK(){
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (bundle!=null){
            edt_taikhoan.setText(bundle.getString("Taikhoan"));
            edt_matkhau.setText(bundle.getString("Matkhau"));
        }
    }
    public void dangnhap(){
        btn_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(kiemtranhap()==0) {
                    xulyDangNhap();
                }
            }
        });
    }

    public int kiemtranhap(){
        int check=0;
        String thieu="";
        if (edt_taikhoan.getText().toString().trim().isEmpty()){
            thieu=thieu+"[Chưa nhập tên tài khoản]"+"\n";
            check=1;
        }
        if (edt_matkhau.getText().toString().trim().isEmpty()){
            thieu=thieu+"[Chưa nhập mật khẩu]"+"\n";
            check=1;
        }
        if (check!=0){
            builder.setMessage(thieu);
            AlertDialog thongbao=builder.create();
            thongbao.show();
        }
        return check;
    }

    public void initDialogTBthieu(){
        builder=new AlertDialog.Builder(DangNhapActivity.this);
        builder.setTitle("Không thể tiếp tục!");
        builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    public void xulyDangNhap(){
        HashMap<String,String> params=new HashMap<>();
        params.put("Tentk",edt_taikhoan.getText().toString().trim());
        params.put("Matkhau",edt_matkhau.getText().toString().trim());
        PerformNetworkRequest request=new PerformNetworkRequest(Api.URL_KIEMTRA_DN,Api.actionDN,params,REQUEST_CODE,getApplicationContext(),this);
        request.execute();
    }

    @Override
    public void DangNhap() {
        Intent intent=new Intent(DangNhapActivity.this,ChuTroActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("TENTK",edt_taikhoan.getText().toString().trim());
        intent.putExtras(bundle);
        keepLogin();
        startActivity(intent);
        finish();
    }

    public void keepLogin(){
        loginManager.createLoginSession(edt_taikhoan.getText().toString().trim());
    }

    public void dangky(){
        btn_dangkytk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DangNhapActivity.this,dkTKActivity.class));
            }
        });
    }
}
