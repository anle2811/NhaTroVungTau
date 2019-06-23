package com.example.anle.nhatrovungtau.XemKhuTroPhongTro;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anle.nhatrovungtau.DialogLoad;
import com.example.anle.nhatrovungtau.R;
import com.example.anle.nhatrovungtau.SlidePagerAdapter;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChiTietKhuTro extends AppCompatActivity{

    public interface GoiFragment{
        void goiFragment(Context context);
    }

    private WeakReference<Object> callBack;

    private static String TENTK;
    private static String IDKHUTRO;

    public static DialogLoad loadDSphong;

    private List<Fragment> fragmentList;
    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private FragmentKhuTro fragmentKhuTro;
    private FragmentPhongTro fragmentPhongTro;
    private TabLayout tabLayout;

    private CollapsingToolbarLayout toolbarLayout;
    private ImageView img_detailKhuTro;
    private HashMap<String,String> detailKhuTro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_khu_tro);
        init();
        setupTitleCollapsToolbar();
        getTTkhutro();
        setupViewPager();
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
                toolbarLayout.setTitle(bundle.getString("Tenkhutro"));
                HashMap<String,String> detailKhuTro=new HashMap<>();
                detailKhuTro.put("Thanhpho",bundle.getString("Thanhpho"));
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
        pagerAdapter=new SlidePagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:Toast.makeText(ChiTietKhuTro.this,"Tag 1",Toast.LENGTH_SHORT).show();break;
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
        img_detailKhuTro=findViewById(R.id.img_detailKhuTro);
        loadDSphong=new DialogLoad(this,"Đang lấy danh sách phòng...");
    }
}
