package com.example.anle.nhatrovungtau.XemKhuTroPhongTro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.anle.nhatrovungtau.CustomAdapter.ListViewPhongTroAdapter;
import com.example.anle.nhatrovungtau.KhuTroPhongTro.PhongTroActivity;
import com.example.anle.nhatrovungtau.Models.PhongTro;
import com.example.anle.nhatrovungtau.PhpDB.Api;
import com.example.anle.nhatrovungtau.PhpDB.PerformNetworkRequest;
import com.example.anle.nhatrovungtau.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class FragmentPhongTro extends Fragment implements ChiTietKhuTro.GoiFragment,PerformNetworkRequest.DSPhongTro {

    private static String TENTK;
    private static String IDKHUTRO;
    private static final int REQUEST_CODE=113;
    private HashMap<String,String> hashMap;

    private ListView lv_phongtro;
    private ListViewPhongTroAdapter phongTroAdapter;
    private List<PhongTro> phongTroList;

    private ChiTietKhuTro chiTietKhuTro;

    private ViewGroup rootView;

    private FrameLayout frame_buttonThem;
    private Locale localeVN;
    private NumberFormat currencyVN;

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
        lv_phongtro=rootView.findViewById(R.id.lv_DSphongtro);
        localeVN = new Locale("vi", "VN");
        currencyVN = NumberFormat.getCurrencyInstance(localeVN);
    }

    @Override
    public void goiFragment(Context context) {
        loadDSphong(context);
    }

    public void loadDSphong(Context context){
        HashMap<String,String> params=new HashMap<>();
        params.put("Idkhutro",IDKHUTRO);
        PerformNetworkRequest request=new PerformNetworkRequest(Api.URL_LAY_DS_PHONGTRO,Api.actionLayDSphongtro,
                params,REQUEST_CODE,context,FragmentPhongTro.this);
        request.execute();
    }

    @Override
    public void layDSphongtro(JSONArray dsphong) {
        phongTroList=new ArrayList<>();
        for (int k=0;k<dsphong.length();k++){
            try {
                JSONObject object=dsphong.getJSONObject(k);
                phongTroList.add(new PhongTro(String.valueOf(object.getInt("Idphong")),
                        currencyVN.format(object.getDouble("Giaphong")),
                        String.valueOf(object.getDouble("Dientich")),
                        object.getInt("Trangthai"),
                        object.getInt("Ghep"),
                        object.getString("Img"),
                        object.getString("Mota")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ListViewPhongTroAdapter adapter=new ListViewPhongTroAdapter(getActivity(),R.layout.listview_dsphongtro,phongTroList);
        lv_phongtro.setAdapter(adapter);
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
