package com.example.anle.nhatrovungtau.XemKhuTroPhongTro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.anle.nhatrovungtau.PhpDB.Api;
import com.example.anle.nhatrovungtau.PhpDB.PerformNetworkRequest;
import com.example.anle.nhatrovungtau.R;

import java.util.HashMap;

public class FragmentTTPhong extends Fragment implements PerformNetworkRequest.CapNhatTTPhong,ChiTietPhongTro.GoiFragment {
    private static final int REQUEST_CODE=113;
    private static String IDPHONG;

    private ViewGroup rootView;
    private EditText edt_songuoi;
    private Button btn_capnhatSnguoi;
    private TextView tv_giaphong;
    private TextView tv_dientich;
    private TextView tv_mota;
    private ChiTietPhongTro chiTietPhongTro;
    private int SnguoiBandau;
    private boolean checkLaySN=false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=(ViewGroup)inflater.inflate(R.layout.fragment_ttphong,container,false);
        try {
            init();
            chiTietPhongTro=(ChiTietPhongTro)getActivity();
            setTT(chiTietPhongTro.chuyenTT());
        }catch (Exception e){
            Log.d("A1", "onCreateView: "+e.getMessage());
            Log.d("A1", "onCreateView: "+chiTietPhongTro.chuyenTT());
        }
        setEdt_songuoi();
        setBtn_capnhatSnguoi();
        return rootView;
    }

    public void init(){
        edt_songuoi=rootView.findViewById(R.id.edt_songuoi);
        tv_giaphong=rootView.findViewById(R.id.tv_giaPhong);
        tv_dientich=rootView.findViewById(R.id.tv_dientichphong);
        tv_mota=rootView.findViewById(R.id.tv_motaphong);
        btn_capnhatSnguoi=rootView.findViewById(R.id.btn_capnhatSnguoi);
    }

    public void setTT(HashMap<String,String> tt){
        IDPHONG=tt.get("Idphong");
        if (checkLaySN==false) {
            SnguoiBandau = Integer.parseInt(tt.get("Trangthai"));
            edt_songuoi.setText(tt.get("Trangthai"));
        }
        tv_giaphong.setText(tt.get("Giaphong"));
        tv_dientich.setText(tt.get("Dientich"));
        tv_mota.setText(tt.get("Mota"));
    }

    public void setEdt_songuoi(){
        edt_songuoi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!edt_songuoi.getText().toString().trim().isEmpty()){
                    if (Integer.parseInt(edt_songuoi.getText().toString().trim())!=SnguoiBandau){
                        btn_capnhatSnguoi.setVisibility(View.VISIBLE);
                    }else {
                        if (btn_capnhatSnguoi.getVisibility()==View.VISIBLE){
                            btn_capnhatSnguoi.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void capNhatSoNguoi(){
        HashMap<String,String> params=new HashMap<>();
        params.put("Idphong",IDPHONG);
        params.put("Trangthai",edt_songuoi.getText().toString().trim());
        PerformNetworkRequest request=new PerformNetworkRequest(Api.URL_CAPNHAT_SNGUOI,Api.actionCapNhatNguoi,params,REQUEST_CODE,getActivity(),FragmentTTPhong.this);
        request.execute();
    }

    public void setBtn_capnhatSnguoi(){
        btn_capnhatSnguoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capNhatSoNguoi();
            }
        });
    }

    @Override
    public void capNhatGhepY() {

    }

    @Override
    public void capNhatGhepN() {

    }

    @Override
    public void capnhatTTdone() {
        checkLaySN=true;
        SnguoiBandau=Integer.parseInt(edt_songuoi.getText().toString().trim());
        btn_capnhatSnguoi.setVisibility(View.GONE);
        getActivity().setResult(Activity.RESULT_OK);
    }

    @Override
    public void goiFragment(Context context) {
        setTT(chiTietPhongTro.chuyenTT());
    }

    @Override
    public void huythaydoi() {

    }

    @Override
    public void luuthaydoi(Context context) {

    }
}
