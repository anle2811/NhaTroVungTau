package com.example.anle.nhatrovungtau.XemKhuTroPhongTro;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anle.nhatrovungtau.DialogLoad;
import com.example.anle.nhatrovungtau.PhpDB.Api;
import com.example.anle.nhatrovungtau.PhpDB.PerformNetworkRequest;
import com.example.anle.nhatrovungtau.R;
import com.example.anle.nhatrovungtau.SlidePagerAdapter;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChiTietKhuTro extends AppCompatActivity implements PerformNetworkRequest.xulyChiTietKhuTro{

    public interface GoiFragment{
        void goiFragment(Context context);
    }

    private WeakReference<Object> callBack;
    private WeakReference<Object> callBackKT;

    private static final int REQUEST_CODE=113;

    private static final int RESULT_CN=11;

    private static String TENTK;
    private static String IDKHUTRO;
    private static String TENKHUTRO;
    public static DialogLoad loadDSphong;
    public static DialogLoad loadXoaKhuTro;
    public static DialogLoad loadCNkhutro;

    private List<Fragment> fragmentList;
    private ViewPager viewPager;
    private FragmentKhuTro fragmentKhuTro;
    private FragmentPhongTro fragmentPhongTro;
    private TabLayout tabLayout;
    private String [] tabtitle={"Thông Tin Khu Trọ","Quản Lý Phòng"};

    private CollapsingToolbarLayout toolbarLayout;
    private ImageView img_detailKhuTro;
    private HashMap<String,String> detailKhuTro;

    private Button btn_xoaKhuTro;
    private Button btn_suaTTkhutro;

    private Dialog dialogTTkhu;
    private View viewTTkhu;
    private EditText edt_CNtenkhutro,edt_CNdiachikhu,edt_CNmotakhu;
    private Button btn_dongCNkhu,btn_CNkhu;
    private Spinner spn_CNthanhpho,spn_CNphuong;
    private List<String> thanhPhoList;
    private List<String> phuongList;
    private ArrayAdapter<String> adapterPhuong;
    private ArrayAdapter<String> adapterTP;
    private String THANHPHO;
    private String PHUONG="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_khu_tro);
        init();
        setupTitleCollapsToolbar();
        getTTkhutro();
        setupViewPager();
        setBtn_xoaKhuTro();
        setUpDialogCNkhu();
        setBtn_suaTTkhutro();
    }

    public void getTTkhutro(){
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (bundle!=null){
            TENTK=bundle.getString("Tentk");
            IDKHUTRO=bundle.getString("Idkhutro");
            Picasso.get().load(bundle.getString("Image"))
                    .placeholder(R.drawable.icon_null_image)
                    .into(img_detailKhuTro);
            try{
                TENKHUTRO=bundle.getString("Tenkhutro");
                toolbarLayout.setTitle(bundle.getString("Tenkhutro"));
                HashMap<String,String> detailKhuTro=new HashMap<>();
                detailKhuTro.put("Thanhpho",bundle.getString("Thanhpho"));
                detailKhuTro.put("Phuong",bundle.getString("Phuong"));
                detailKhuTro.put("Diachikhu",bundle.getString("Diachikhu"));
                detailKhuTro.put("Motakhu",bundle.getString("Motakhu"));
                this.detailKhuTro=detailKhuTro;
            }catch (Exception e){
                Log.d("Loi","Error: "+e.getMessage());
            }

        }
    }

    public HashMap<String,String> chuyenTenTKvaIDkhu(){
        HashMap<String,String> IDandTK=new HashMap<>();
        IDandTK.put("Tentk",TENTK);
        IDandTK.put("Idkhutro",IDKHUTRO);
        IDandTK.put("Tenkhutro",TENKHUTRO);
        return IDandTK;
    }

    public HashMap<String,String> chuyenTT(){
        return detailKhuTro;
    }

    public void setupTitleCollapsToolbar(){
        toolbarLayout=findViewById(R.id.collap_toolbar);
        toolbarLayout.setTitle("MIKITA");
        toolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        toolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
    }

    public void setupViewPager(){
        tabLayout=findViewById(R.id.tab_khutro_phongtro);
        fragmentList=new ArrayList<>();
        fragmentList.add(fragmentKhuTro);
        fragmentList.add(fragmentPhongTro);
        viewPager=findViewById(R.id.viewpager_KTPT);
        SlidePagerAdapter slidePagerAdapter=new SlidePagerAdapter(getSupportFragmentManager(),fragmentList);
        slidePagerAdapter.setTabtitle(tabtitle);
        viewPager.setAdapter(slidePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:break;
                    case 1: GoiFragment callBack=(GoiFragment) ChiTietKhuTro.this.callBack.get();
                            callBack.goiFragment(getApplicationContext());break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void init(){
        fragmentKhuTro=new FragmentKhuTro();
        fragmentPhongTro=new FragmentPhongTro();
        callBack= new WeakReference<Object>(fragmentPhongTro);
        callBackKT=new WeakReference<Object>(fragmentKhuTro);
        img_detailKhuTro=findViewById(R.id.img_detailKhuTro);
        loadDSphong=new DialogLoad(this,"Đang lấy danh sách phòng...");
        loadXoaKhuTro=new DialogLoad(this,"Đang xóa khu trọ...");
        loadCNkhutro=new DialogLoad(this,"Đang lưu lại cập nhật...");
        btn_xoaKhuTro=findViewById(R.id.btn_xoakhutro);
        btn_suaTTkhutro=findViewById(R.id.btn_suaTTkhutro);
    }

    public void setBtn_xoaKhuTro(){
        btn_xoaKhuTro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> params=new HashMap<>();
                params.put("Tentk",TENTK);
                params.put("Idkhutro",IDKHUTRO);
                PerformNetworkRequest request=new PerformNetworkRequest(Api.URL_XOA_KHUTRO,Api.actionXoaKhuTro,params,REQUEST_CODE,getApplicationContext(),ChiTietKhuTro.this);
                request.execute();
            }
        });
    }

    public void setBtn_suaTTkhutro(){
        btn_suaTTkhutro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataDialogCNkhu();
                dialogTTkhu.show();
            }
        });
    }

    public void setDataDialogCNkhu(){
        edt_CNtenkhutro.setText(TENKHUTRO);
        edt_CNdiachikhu.setText(detailKhuTro.get("Diachikhu"));
        edt_CNmotakhu.setText(detailKhuTro.get("Motakhu"));
        setSpnTPdataShow();
        setSpnPHUONGdataShow();
    }

    public void setSpnTPdataShow(){
        switch (detailKhuTro.get("Thanhpho")){
            case "Hà Nội":spn_CNthanhpho.setSelection(0);break;
            case "Đà Nẵng":spn_CNthanhpho.setSelection(1);break;
            case "Vũng Tàu":spn_CNthanhpho.setSelection(2);break;
            case "Sài Gòn":spn_CNthanhpho.setSelection(3);break;
            case "Hội An":spn_CNthanhpho.setSelection(4);break;
            case "Hạ Long":spn_CNthanhpho.setSelection(5);break;
            case "Hải Phòng":spn_CNthanhpho.setSelection(6);break;
            default:spn_CNthanhpho.setSelection(0);break;
        }
    }

    public void setSpnPHUONGdataShow(){
        switch (detailKhuTro.get("Thanhpho")){
            case "Hà Nội":break;
            case "Đà Nẵng":break;
            case "Vũng Tàu":{
                setPhuongList(2);
                switch (detailKhuTro.get("Phuong")){
                    case "Phường Thắng Nhất":spn_CNphuong.setSelection(0);break;
                    case "Phường Thắng Nhì":spn_CNphuong.setSelection(1);break;
                    case "Phường Thắng Tam":spn_CNphuong.setSelection(2);break;
                    case "Phường Bốn":spn_CNphuong.setSelection(3);break;
                    case "Phường Năm":spn_CNphuong.setSelection(4);break;
                    case "Phường Sáu":spn_CNphuong.setSelection(5);break;
                    case "Phường Bảy":spn_CNphuong.setSelection(6);break;
                    case "Phường Tám":spn_CNphuong.setSelection(7);break;
                    case "Phường Chín":spn_CNphuong.setSelection(8);break;
                    case "Phường Mười":spn_CNphuong.setSelection(9);break;
                    case "Phường Rạch Dừa":spn_CNphuong.setSelection(10);break;
                    case "Phường Mười Một":spn_CNphuong.setSelection(11);break;
                    case "Phường Mười Hai":spn_CNphuong.setSelection(12);break;
                    default:spn_CNphuong.setSelection(0);break;
                }
            }break;
            case "Sài Gòn":break;
            case "Hội An":break;
            case "Hạ Long":break;
            case "Hải Phòng":break;
            default:break;
        }
    }

    public void setUpDialogCNkhu(){
        dialogTTkhu=new Dialog(ChiTietKhuTro.this);
        viewTTkhu=LayoutInflater.from(ChiTietKhuTro.this).inflate(R.layout.dialog_ttkhutro,(ViewGroup)findViewById(R.id.relLayout_ttkhutro),false);
        dialogTTkhu.setContentView(viewTTkhu);
        dialogTTkhu.setCanceledOnTouchOutside(false);
        dialogTTkhu.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        edt_CNtenkhutro=viewTTkhu.findViewById(R.id.edt_CNtenkhutro);
        edt_CNdiachikhu=viewTTkhu.findViewById(R.id.edt_CNdiachikhu);
        edt_CNmotakhu=viewTTkhu.findViewById(R.id.edt_CNmotakhu);

        btn_dongCNkhu=viewTTkhu.findViewById(R.id.btn_dongCNkhu);
        btn_CNkhu=viewTTkhu.findViewById(R.id.btn_CNkhu);

        btn_dongCNkhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTTkhu.cancel();
            }
        });

        btn_CNkhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpluuCNkhu();
            }
        });

        setUpSpnTpvaPhuong(viewTTkhu);

    }

    public void setUpSpnTpvaPhuong(View viewTTkhu){
        spn_CNthanhpho=viewTTkhu.findViewById(R.id.spn_CNthanhpho);
        spn_CNphuong=viewTTkhu.findViewById(R.id.spn_CNphuong);
        thanhPhoList=new ArrayList<>();
        phuongList=new ArrayList<>();

        thanhPhoList.add("Hà Nội");
        thanhPhoList.add("Đà Nẵng");
        thanhPhoList.add("Vũng Tàu");
        thanhPhoList.add("Sài Gòn");
        thanhPhoList.add("Hội An");
        thanhPhoList.add("Hạ Long");
        thanhPhoList.add("Hải Phòng");

        adapterPhuong=new ArrayAdapter<>(ChiTietKhuTro.this,android.R.layout.simple_spinner_dropdown_item,phuongList);
        spn_CNphuong.setAdapter(adapterPhuong);

        adapterTP=new ArrayAdapter<>(ChiTietKhuTro.this,android.R.layout.simple_spinner_dropdown_item,thanhPhoList);
        spn_CNthanhpho.setAdapter(adapterTP);

        spn_CNthanhpho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setPhuongList(position);
                THANHPHO=thanhPhoList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spn_CNphuong.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PHUONG=phuongList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setPhuongList(int i){
        switch (i){
            case 2:{
                phuongList.clear();
                phuongList.add("Phường Thắng Nhất");
                phuongList.add("Phường Thắng Nhì");
                phuongList.add("Phường Thắng Tam");
                phuongList.add("Phường Bốn");
                phuongList.add("Phường Năm");
                phuongList.add("Phường Sáu");
                phuongList.add("Phường Bảy");
                phuongList.add("Phường Tám");
                phuongList.add("Phường Chín");
                phuongList.add("Phường Mười");
                phuongList.add("Phường Rạch Dừa");
                phuongList.add("Phường Mười Một");
                phuongList.add("Phường Mười Hai");
                adapterPhuong.notifyDataSetChanged();
            }break;
            default: {
                phuongList.clear();
                adapterPhuong.notifyDataSetChanged();
            }break;
        }
    }

    public void setUpluuCNkhu(){
        HashMap<String, String> params = new HashMap<>();
        params.put("Idkhutro", IDKHUTRO);
        params.put("Tenkhutro", edt_CNtenkhutro.getText().toString().trim());
        params.put("Diachi", edt_CNdiachikhu.getText().toString().trim());
        params.put("Mota", edt_CNmotakhu.getText().toString().trim());
        params.put("Tentp", THANHPHO);
        params.put("Phuong", PHUONG);
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CAPNHAT_KHU, Api.actionCapNhatKhu, params, REQUEST_CODE, getApplicationContext(), ChiTietKhuTro.this);
        request.execute();
    }

    @Override
    public void CNkhutroDone() {
        CapNhatLaiGiaoDien();
        dialogTTkhu.cancel();
        setResult(RESULT_OK);
    }

    public void CapNhatLaiGiaoDien(){
        TENKHUTRO=edt_CNtenkhutro.getText().toString().trim();
        toolbarLayout.setTitle(TENKHUTRO);
        HashMap<String,String> detailKhuTro=new HashMap<>();
        detailKhuTro.put("Thanhpho",THANHPHO);
        detailKhuTro.put("Phuong",PHUONG);
        detailKhuTro.put("Diachikhu",edt_CNdiachikhu.getText().toString().trim());
        detailKhuTro.put("Motakhu",edt_CNmotakhu.getText().toString().trim());
        this.detailKhuTro=detailKhuTro;
        GoiFragment call=(GoiFragment)callBackKT.get();
        call.goiFragment(getApplicationContext());
    }

    @Override
    public void xoaKhuTroDone() {
        setResult(RESULT_OK);
        finish();
    }
}
