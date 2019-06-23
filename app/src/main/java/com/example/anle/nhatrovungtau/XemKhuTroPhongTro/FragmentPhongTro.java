package com.example.anle.nhatrovungtau.XemKhuTroPhongTro;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.anle.nhatrovungtau.KhuTroPhongTro.PhongTroActivity;
import com.example.anle.nhatrovungtau.R;

import java.util.HashMap;

public class FragmentPhongTro extends Fragment {

    private static String TENTK;
    private static String IDKHUTRO;

    private HashMap<String,String> hashMap;

    private ChiTietKhuTro chiTietKhuTro;

    private ViewGroup rootView;

    private FrameLayout frame_buttonThem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=(ViewGroup)inflater.inflate(R.layout.fragment_phongtro,container,false);
        init();
        chiTietKhuTro=(ChiTietKhuTro) getActivity();
        hashMap=chiTietKhuTro.chuyenTenTKvaIDkhu();
        TENTK=hashMap.get("Tentk");
        IDKHUTRO=hashMap.get("Idkhutro");
        clickThemPhong();
        return rootView;
    }

    public void init(){
        frame_buttonThem=rootView.findViewById(R.id.frame_buttonThem);
    }

    public void clickThemPhong(){
        frame_buttonThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),PhongTroActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("Tentk",TENTK);
                bundle.putString("Idkhutro",IDKHUTRO);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
