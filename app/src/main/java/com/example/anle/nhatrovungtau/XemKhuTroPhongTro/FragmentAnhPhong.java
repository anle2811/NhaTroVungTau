package com.example.anle.nhatrovungtau.XemKhuTroPhongTro;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anle.nhatrovungtau.CustomAdapter.RecycAnhPhongAdapter;
import com.example.anle.nhatrovungtau.Models.AnhPhong;
import com.example.anle.nhatrovungtau.PhpDB.Api;
import com.example.anle.nhatrovungtau.PhpDB.PerformNetworkRequest;
import com.example.anle.nhatrovungtau.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragmentAnhPhong extends Fragment implements ChiTietPhongTro.GoiFragment,PerformNetworkRequest.DSAnhPhong {

    private static final int REQUEST_CODE=113;
    private static String IDPHONG;
    private ViewGroup rootView;
    private RecyclerView recyc_anhphong;
    private StaggeredGridLayoutManager manager;
    private List<AnhPhong> anhPhongList;
    private ChiTietPhongTro chiTietPhongTro;

    private boolean daLoad=false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=(ViewGroup) inflater.inflate(R.layout.fragment_anhphong,container,false);
        init();
        setLayoutRecyc();
        chiTietPhongTro=(ChiTietPhongTro)getActivity();
        IDPHONG=chiTietPhongTro.chuyenTT().get("Idphong");
        return rootView;
    }

    public void init(){
        recyc_anhphong=rootView.findViewById(R.id.recyc_anhphong);
    }

    public void loadAnhPhong(Context context){
        HashMap<String,String> params=new HashMap<>();
        params.put("Idphong",IDPHONG);
        PerformNetworkRequest request=new PerformNetworkRequest(Api.URL_LAY_ANHPHONG,Api.actionLayAnhPhong,params,REQUEST_CODE,context,FragmentAnhPhong.this);
        request.execute();
    }

    public void setLayoutRecyc(){
        manager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyc_anhphong.setLayoutManager(manager);
    }


    @Override
    public void goiFragment(Context context) {
        if (!daLoad){
            loadAnhPhong(context);
        }
    }

    @Override
    public void layDSanhphong(JSONArray dsUrlAnh) {
        anhPhongList=new ArrayList<>();
        for (int k=0;k<dsUrlAnh.length();k++){
            try {
                JSONObject object=dsUrlAnh.getJSONObject(k);
                anhPhongList.add(new AnhPhong(String.valueOf(object.getInt("Idimg")),object.getString("Url")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        RecycAnhPhongAdapter adapter=new RecycAnhPhongAdapter(getActivity(),anhPhongList);
        recyc_anhphong.setAdapter(adapter);
        daLoad = true;
    }
}
