package com.example.anle.nhatrovungtau.XemKhuTroPhongTro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.anle.nhatrovungtau.R;

import java.util.HashMap;

public class FragmentKhuTro extends Fragment {

    private ChiTietKhuTro chiTietKhuTro;

    private ScrollView scroll_chitietkhu;
    private TextView tv_thanhpho;
    private TextView tv_diachi;
    private TextView tv_mota;
    private ViewGroup rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=(ViewGroup)inflater.inflate(R.layout.fragment_khutro,container,false);
        init();
        chiTietKhuTro=(ChiTietKhuTro) getActivity();
        setTextTT(chiTietKhuTro.chuyenTT());
        return rootView;
    }

    public void init(){
        tv_thanhpho=rootView.findViewById(R.id.tv_TP);
        tv_diachi=rootView.findViewById(R.id.tv_DC);
        tv_mota=rootView.findViewById(R.id.tv_MOTA);
        scroll_chitietkhu=rootView.findViewById(R.id.scroll_chitietkhu);
        ViewCompat.setNestedScrollingEnabled(scroll_chitietkhu,true);
    }

    public void setTextTT(HashMap<String,String> tt){
        tv_thanhpho.setText(tt.get("Thanhpho"));
        tv_diachi.setText(tt.get("Diachikhu"));
        tv_mota.setText(tt.get("Motakhu"));
    }

}
