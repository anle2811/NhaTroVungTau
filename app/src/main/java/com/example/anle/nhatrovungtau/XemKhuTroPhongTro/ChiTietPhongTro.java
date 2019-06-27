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
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.widget.ImageView;

import com.example.anle.nhatrovungtau.DialogLoad;
import com.example.anle.nhatrovungtau.R;
import com.example.anle.nhatrovungtau.SlidePagerAdapter;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChiTietPhongTro extends AppCompatActivity {

    private static String IDPHONG;

    public static DialogLoad loadAnhPhong;

    public interface GoiFragment{
        void goiFragment(Context context);
    }

    private WeakReference<Object> callBack;

    private List<Fragment> fragmentList;
    private ViewPager viewPager;
    private FragmentTTPhong fragmentTTPhong;
    private FragmentAnhPhong fragmentAnhPhong;
    private TabLayout tabLayout;
    private String [] tabtitle={"Thông Tin Phòng Trọ","Ảnh Phòng"};

    private CollapsingToolbarLayout toolbarLayout;
    private ImageView img_AVTPhong;
    private SwitchCompat swt_choghep;

    HashMap<String,String> detailPhongTro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_phong_tro);
        init();
        setupTitleCollapsToolbar();
        setupViewPager();
        getTTphongtro();
    }

    public void getTTphongtro(){
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (bundle!=null){
            if (bundle.getInt("Ghep")==0){
                swt_choghep.setChecked(false);
            }else {
                swt_choghep.setChecked(true);
            }
            IDPHONG=bundle.getString("Idphong");
            Picasso.get().load(bundle.getString("Image"))
                    .placeholder(R.drawable.icon_null_image)
                    .into(img_AVTPhong);
            try{
                toolbarLayout.setTitle("Phòng ("+bundle.getString("Tenkhutro")+")");
                HashMap<String,String> detailPhongTro=new HashMap<>();
                detailPhongTro.put("Idphong",IDPHONG);
                detailPhongTro.put("Giaphong",bundle.getString("Giaphong"));
                detailPhongTro.put("Trangthai",String.valueOf(bundle.getInt("Trangthai")));
                detailPhongTro.put("Dientich",bundle.getString("Dientich"));
                detailPhongTro.put("Mota",bundle.getString("Mota"));
                this.detailPhongTro=detailPhongTro;
            }catch (Exception e){
                Log.d("Loi","Error: "+e.getMessage());
            }

        }
    }

    public HashMap<String,String> chuyenTT(){
        return detailPhongTro;
    }

    public void setupViewPager(){
            tabLayout = findViewById(R.id.tab_ttphong_anh);
            viewPager = findViewById(R.id.viewpager_ttphong_anh);
            fragmentList = new ArrayList<>();
            fragmentList.add(fragmentTTPhong);
            fragmentList.add(fragmentAnhPhong);
            SlidePagerAdapter slidePagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), fragmentList);
            slidePagerAdapter.setTabtitle(tabtitle);
            viewPager.setAdapter(slidePagerAdapter);
            tabLayout.setupWithViewPager(viewPager);

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch (tab.getPosition()) {
                        case 0:
                            break;
                        case 1:
                            GoiFragment callback = (GoiFragment) callBack.get();
                            callback.goiFragment(getApplicationContext());
                            break;
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

    public void setupTitleCollapsToolbar(){
        toolbarLayout=findViewById(R.id.collap_toolbar_1);
        toolbarLayout.setTitle("SUSU");
        toolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        toolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
    }

    public void init(){
        fragmentTTPhong=new FragmentTTPhong();
        fragmentAnhPhong=new FragmentAnhPhong();
        callBack=new WeakReference<Object>(fragmentAnhPhong);
        img_AVTPhong=findViewById(R.id.img_detailPhongTro);
        swt_choghep=findViewById(R.id.swt_choghep);
        loadAnhPhong=new DialogLoad(ChiTietPhongTro.this,"Đang tải ảnh của phòng...");
    }
}
