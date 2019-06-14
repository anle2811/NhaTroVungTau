package com.example.anle.nhatrovungtau;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class DangNhapActivity extends AppCompatActivity {
    private Button btn_dangkytk;
    private Button btn_dangnhap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangnhapchutro);
        btn_dangkytk=findViewById(R.id.btn_dangkytk);
        btn_dangnhap=findViewById(R.id.btn_dangnhap);
        dangnhap();
        dangky();
    }

    public void dangnhap(){
        btn_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DangNhapActivity.this,ChuTroActivity.class));
            }
        });
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
