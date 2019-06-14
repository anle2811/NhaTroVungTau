package com.example.anle.nhatrovungtau.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.anle.nhatrovungtau.Models.ChuTro;
import com.example.anle.nhatrovungtau.R;

import java.util.ArrayList;

public class FragmentTTCB extends Fragment implements AdapterView.OnItemSelectedListener {

    private DKTKTiepTheo tiepTheo;
    private ViewGroup rootView;
    private Spinner spn_ngay,spn_thang,spn_nam;
    private RadioGroup rdg_gioitinh;
    private EditText edt_hoten,edt_cmnd,edt_email,edt_diachi;
    private String ngay,thang,nam;
    private String sinhnhat;
    private String gioitinh;
    private static final String TAG=FragmentTTCB.class.getSimpleName();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView");
        rootView=(ViewGroup)inflater.inflate(R.layout.fragment_dktk1,container,false);
        initEdittext();
        initAndsetupSpinner();
        initChonGioiTinh();
        return rootView;
    }

    public void tieptheo(){
        tiepTheo.truyenDL(edt_hoten.getText()+"",edt_cmnd.getText()+"",edt_email.getText()+"",
                edt_diachi.getText()+"",sinhnhat,gioitinh);
    }


    public void initEdittext(){
        edt_hoten=rootView.findViewById(R.id.edt_hoten);
        edt_cmnd=rootView.findViewById(R.id.edt_cmnd);
        edt_email=rootView.findViewById(R.id.edt_email);
        edt_diachi=rootView.findViewById(R.id.edt_diachi);
    }

    public void initChonGioiTinh(){
        rdg_gioitinh=rootView.findViewById(R.id.rdg_gioitinh);

        rdg_gioitinh.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()){
                    case R.id.rd_nam: gioitinh="Nam";break;
                    case R.id.rd_nu: gioitinh="Nữ";break;
                }
            }
        });
    }

    public void initAndsetupSpinner(){
        spn_ngay=rootView.findViewById(R.id.spn_ngay);
        spn_thang=rootView.findViewById(R.id.spn_thang);
        spn_nam=rootView.findViewById(R.id.spn_nam);

        ArrayList<String> list_item_ngay=new ArrayList<>();
        ArrayList<String> list_item_thang=new ArrayList<>();
        ArrayList<String> list_item_nam=new ArrayList<>();

        for(int k=1;k<=31;k++){
            list_item_ngay.add(String.valueOf(k));
        }

        for(int k=1;k<=12;k++){
            list_item_thang.add(String.valueOf(k));
        }

        for(int k=2019;k>=1960;k--){
            list_item_nam.add(String.valueOf(k));
        }

        ArrayAdapter<String> adapterNgay=new ArrayAdapter(rootView.getContext(),android.R.layout.simple_spinner_dropdown_item,list_item_ngay);
        ArrayAdapter<String> adapterThang=new ArrayAdapter(rootView.getContext(),android.R.layout.simple_spinner_dropdown_item,list_item_thang);
        ArrayAdapter<String> adapterNam=new ArrayAdapter(rootView.getContext(),android.R.layout.simple_spinner_dropdown_item,list_item_nam);

        spn_ngay.setAdapter(adapterNgay);
        spn_thang.setAdapter(adapterThang);
        spn_nam.setAdapter(adapterNam);

        spn_ngay.setOnItemSelectedListener(this);
        spn_thang.setOnItemSelectedListener(this);
        spn_nam.setOnItemSelectedListener(this);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spn_ngay: ngay=spn_ngay.getSelectedItem().toString();break;
            case R.id.spn_thang: thang=spn_thang.getSelectedItem().toString();break;
            case R.id.spn_nam: nam=spn_nam.getSelectedItem().toString();break;
        }
        sinhnhat=ngay+"/"+thang+"/"+nam;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DKTKTiepTheo){
            tiepTheo=(DKTKTiepTheo) context;
        }else {
            throw new RuntimeException(context.toString()+" phải implement DKTKTiepTheo");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate()");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"onActivityCreated()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG,"onDetach()");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) { //Đang xử lý lưu trạng thái Fragment
        super.onSaveInstanceState(outState);

    }

    public static FragmentTTCB newInstance(int page){
        FragmentTTCB ttcb=new FragmentTTCB();
        Bundle args=new Bundle();

        return ttcb;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }
}
