package com.example.anle.nhatrovungtau.KhuTroPhongTro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.anle.nhatrovungtau.R;
import com.example.anle.nhatrovungtau.XemKhuTroPhongTro.ChiTietPhongTro;
import com.squareup.picasso.Picasso;

public class PhotoFullPhongTro extends AppCompatActivity {

    private ImageView img_fullScreen;
    private Button btn_xoaanh;
    private Button btn_dong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_full_phong_tro);
        img_fullScreen=findViewById(R.id.img_anhPhongFull);
        btn_xoaanh=findViewById(R.id.btn_xoaAnhPhong);
        btn_dong=findViewById(R.id.btn_thoatFull);
        Picasso.get().load(getUrl()).placeholder(R.drawable.icon_null_image).into(img_fullScreen);
        setUpThoat();
        setUpXoaAnh();
    }

    public void setUpThoat(){
        btn_dong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setUpXoaAnh(){
        btn_xoaanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PhotoFullPhongTro.this,ChiTietPhongTro.class);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    public String getUrl(){
        String url=null;
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        url=bundle.getString("URL");
        return url;
    }
}
