package com.example.anle.nhatrovungtau;

import android.content.Intent;
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

    private static final int REQUEST_CODE=113;

    private Button btn_dangkytk;
    private Button btn_dangnhap;
    private EditText edt_taikhoan;
    private TextInputEditText edt_matkhau;
    public static ProgressBar prgbar_dangnhap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangnhapchutro);
        initAll();
        TK_MK_VuaDK();
        dangnhap();
        dangky();
    }
    public void initAll(){
        btn_dangkytk=findViewById(R.id.btn_dangkytk);
        btn_dangnhap=findViewById(R.id.btn_dangnhap);
        edt_taikhoan=findViewById(R.id.edt_taikhoan);
        edt_matkhau=findViewById(R.id.edt_matkhau);
        prgbar_dangnhap=findViewById(R.id.prgbar_dangnhap);
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
                xulyDangNhap();
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

    public void yeuCauTTchutro(){
        HashMap<String,String> params=new HashMap<>();
        params.put("Tentk",edt_taikhoan.getText().toString().trim());
        PerformNetworkRequest request=new PerformNetworkRequest(Api.URL_LAY_TT_CHUTRO,Api.actionLayTTchutro,params,REQUEST_CODE,getApplicationContext(),this);
        request.execute();
    }

    @Override
    public void DangNhap() {
        yeuCauTTchutro();
    }

    @Override
    public void layTTchutro(JSONObject jsonObject) {
        Intent intent=new Intent(DangNhapActivity.this,ChuTroActivity.class);
        Bundle bundle=new Bundle();
        try {
            bundle.putString("Hoten",jsonObject.getString("hoten"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
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
