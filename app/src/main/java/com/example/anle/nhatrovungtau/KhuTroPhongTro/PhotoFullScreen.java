package com.example.anle.nhatrovungtau.KhuTroPhongTro;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.anle.nhatrovungtau.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PhotoFullScreen extends AppCompatActivity {

    private ImageButton btn_clock,btn_antiClock;
    private Button btn_luu,btn_dong;
    private ImageView img_xemanh;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_full_screen);
        btn_luu=findViewById(R.id.btn_luulai);
        btn_dong=findViewById(R.id.btn_donglai);
        btn_clock=findViewById(R.id.btn_xoayClock);
        btn_antiClock=findViewById(R.id.btn_xoayAntiClock);
        img_xemanh=findViewById(R.id.img_xemanh);
        try {
            img_xemanh.setImageBitmap(getBitmap());
            bitmap=getBitmap();
            initBtnXoay();
            luulai();
            btnDong();
        }catch (Exception e){
            Log.d("Loi","Loi: "+e.getMessage());
        }

    }

    public Bitmap getBitmap(){
        byte [] byteArr=null;
        try {
            File file=new File(getFilesDir(),"img.txt");
            FileInputStream in=new FileInputStream(file);
            long byteLength= file.length();
            byteArr=new byte[(int) byteLength];
            in.read(byteArr,0, (int) byteLength);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap=BitmapFactory.decodeByteArray(byteArr,0,byteArr.length);
        return bitmap;
    }

    public void luulai(){
        btn_luu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PhotoFullScreen.this,KhuTroActivity.class);
                chuyenBitmapThanhByteArr(bitmap);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    public void btnDong(){
        btn_dong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void chuyenBitmapThanhByteArr(Bitmap bitmap){
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte [] byteArr=stream.toByteArray();

        try {
            FileOutputStream fileOutput=openFileOutput("img.txt",Context.MODE_PRIVATE);
            fileOutput.write(byteArr);
            fileOutput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void xoayAnh(int gocxoay){
        Matrix matrix=new Matrix();
        Matrix mat=img_xemanh.getImageMatrix();
        matrix.set(mat);
        matrix.setRotate(gocxoay);
        bitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),
                bitmap.getHeight(),matrix,false);
        img_xemanh.setImageBitmap(bitmap);
    }

    public void initBtnXoay(){
        btn_clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xoayAnh(90);
            }
        });
        btn_antiClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xoayAnh(-90);
            }
        });
    }
}
