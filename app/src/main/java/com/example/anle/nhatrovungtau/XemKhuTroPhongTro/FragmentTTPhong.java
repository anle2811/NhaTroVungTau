package com.example.anle.nhatrovungtau.XemKhuTroPhongTro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.anle.nhatrovungtau.R;

import java.util.HashMap;

public class FragmentTTPhong extends Fragment {

    private ViewGroup rootView;
    private EditText edt_songuoi;
    private TextView tv_giaphong;
    private TextView tv_dientich;
    private TextView tv_mota;
    private ChiTietPhongTro chiTietPhongTro;
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

        return rootView;
    }

    public void init(){
        edt_songuoi=rootView.findViewById(R.id.edt_songuoi);
        tv_giaphong=rootView.findViewById(R.id.tv_giaPhong);
        tv_dientich=rootView.findViewById(R.id.tv_dientichphong);
        tv_mota=rootView.findViewById(R.id.tv_motaphong);
    }

    public void setTT(HashMap<String,String> tt){
        edt_songuoi.setText(tt.get("Trangthai"));
        tv_giaphong.setText(tt.get("Giaphong"));
        tv_dientich.setText(tt.get("Dientich"));
        tv_mota.setText(tt.get("Mota"));
    }
}
