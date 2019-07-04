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
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.anle.nhatrovungtau.ChuTroActivity;
import com.example.anle.nhatrovungtau.DialogLoad;
import com.example.anle.nhatrovungtau.PhpDB.Api;
import com.example.anle.nhatrovungtau.PhpDB.PerformNetworkRequest;
import com.example.anle.nhatrovungtau.R;
import com.example.anle.nhatrovungtau.SlidePagerAdapter;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChiTietPhongTro extends AppCompatActivity implements PerformNetworkRequest.CapNhatTTPhong {

    private static String IDPHONG;
    private static final int REQUEST_CODE=113;
    public static DialogLoad loadAnhPhong;
    public static DialogLoad loadThayDoi;
    public static DialogLoad loadDoiGhep;
    public static DialogLoad loadCNTTphong;
    public static DialogLoad loadCNSnguoi;
    public static LinearLayout line_thaydoianh;
    private Button btn_luuthaydoi,btn_huythaydoi;
    private Button btn_suaTTPhong;
    public interface GoiFragment{
        void goiFragment(Context context);
        void huythaydoi();
        void luuthaydoi(Context context);
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
    private boolean checkGhepBanDau;
    HashMap<String,String> detailPhongTro;

    private Locale localeVN;
    private NumberFormat currencyVN;
    private Dialog dialogTTphong;
    private View viewTTphong;
    private EditText edt_CNgiaphong,edt_CNdientichphong,edt_CNmotaphong;
    private Button btn_dongCNphong,btn_CNphong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_phong_tro);
        init();
        setupTitleCollapsToolbar();
        setupViewPager();
        getTTphongtro();
        setUpLuuHuy();
        setUpDoiGhep();
        setUpDialogCNphong();
        setBtn_suaTTPhong();
    }

    public void getTTphongtro(){
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (bundle!=null){
            if (bundle.getInt("Ghep")==0){
                checkGhepBanDau=false;
                swt_choghep.setChecked(false);
            }else {
                checkGhepBanDau=false;
                swt_choghep.setChecked(true);
            }

            IDPHONG=bundle.getString("Idphong");
            Picasso.get().load(bundle.getString("Img"))
                    .placeholder(R.drawable.icon_null_image)
                    .into(img_AVTPhong);
            try{
                toolbarLayout.setTitle("P"+bundle.getString ("Vitrichon")+"("+bundle.getString("Tenkhutro")+")");
                HashMap<String,String> detailPhongTro=new HashMap<>();
                detailPhongTro.put("Idkhutro",bundle.getString("Idkhutro"));
                detailPhongTro.put("Idphong",IDPHONG);
                detailPhongTro.put("Giaphong",bundle.getString("Giaphong"));
                detailPhongTro.put("Trangthai",String.valueOf(bundle.getInt("Trangthai")));
                detailPhongTro.put("Dientich",bundle.getString("Dientich"));
                detailPhongTro.put("Mota",bundle.getString("Mota"));
                this.detailPhongTro=detailPhongTro;
            }catch (Exception e){
                Log.d("Loi","Error: "+e.getMessage());
            }
            checkGhepBanDau=true;
        }
    }

    public void setUpDoiGhep(){
        swt_choghep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkGhepBanDau==true){
                    if (isChecked){
                        capNhatTTGhep(1);
                    }else {
                        capNhatTTGhep(0);
                    }
                }
            }
        });
    }

    @Override
    public void capNhatGhepY() {
        if (swt_choghep.isChecked()){
            Toast.makeText(ChiTietPhongTro.this,"Đã bật ghép",Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
        }else {
            Toast.makeText(ChiTietPhongTro.this,"Đã tắt ghép",Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
        }
    }

    @Override
    public void capNhatGhepN() {
        if (swt_choghep.isChecked()){
            swt_choghep.setChecked(false);
        }else {
            swt_choghep.setChecked(true);
        }
    }

    public void capNhatTTGhep(int trangthai){
        HashMap<String,String> params=new HashMap<>();
        params.put("Idphong",IDPHONG);
        params.put("Ghep",String.valueOf(trangthai));
        PerformNetworkRequest request=new PerformNetworkRequest(Api.URL_CAPNHAT_GHEP,
                Api.actionCapNhatGhep,params,REQUEST_CODE,getApplicationContext(),ChiTietPhongTro.this);
        request.execute();
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
        toolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar1);
        toolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
    }

    public void init(){
        fragmentTTPhong=new FragmentTTPhong();
        fragmentAnhPhong=new FragmentAnhPhong();
        callBack=new WeakReference<Object>(fragmentAnhPhong);
        img_AVTPhong=findViewById(R.id.img_detailPhongTro);
        swt_choghep=findViewById(R.id.swt_choghep);
        loadAnhPhong=new DialogLoad(ChiTietPhongTro.this,"Đang tải ảnh của phòng...");
        loadThayDoi=new DialogLoad(ChiTietPhongTro.this,"Đang lưu thay đổi...");
        loadDoiGhep=new DialogLoad(ChiTietPhongTro.this,"Đang đổi trạng thái ghép...");
        loadCNTTphong=new DialogLoad(ChiTietPhongTro.this,"Đang lưu cập nhật...");
        loadCNSnguoi=new DialogLoad(ChiTietPhongTro.this,"Đang cập nhật số người...");
        line_thaydoianh=findViewById(R.id.line_thaydoianh);
        btn_luuthaydoi=findViewById(R.id.btn_luuthaydoi);
        btn_huythaydoi=findViewById(R.id.btn_huythaydoi);
        btn_suaTTPhong=findViewById(R.id.btn_suaTTPhong);
        localeVN = new Locale("vi", "VN");
        currencyVN = NumberFormat.getCurrencyInstance(localeVN);
    }

    public void setBtn_suaTTPhong(){
        btn_suaTTPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTTphongDialog();
                dialogTTphong.show();
            }
        });
    }

    public void setUpDialogCNphong(){
        dialogTTphong=new Dialog(ChiTietPhongTro.this);
        viewTTphong=LayoutInflater.from(ChiTietPhongTro.this).inflate(R.layout.dialog_ttphong,(ViewGroup)findViewById(R.id.relLayout_ttphong),false);
        dialogTTphong.setContentView(viewTTphong);
        dialogTTphong.setCanceledOnTouchOutside(false);
        dialogTTphong.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        edt_CNgiaphong=viewTTphong.findViewById(R.id.edt_CNgiaphong);
        edt_CNdientichphong=viewTTphong.findViewById(R.id.edt_CNdientichphong);
        edt_CNmotaphong=viewTTphong.findViewById(R.id.edt_CNmotaphong);

        btn_dongCNphong=viewTTphong.findViewById(R.id.btn_dongCNphong);
        btn_CNphong=viewTTphong.findViewById(R.id.btn_CNphong);

        btn_dongCNphong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTTphong.cancel();
            }
        });

        btn_CNphong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpluuCNphong();
            }
        });
    }

    public void setUpluuCNphong(){
        HashMap<String,String> params=new HashMap<>();
        params.put("Idphong",IDPHONG);
        params.put("Giaphong",edt_CNgiaphong.getText().toString().trim());
        params.put("Dientich",edt_CNdientichphong.getText().toString().trim());
        params.put("Mota",edt_CNmotaphong.getText().toString().trim());
        PerformNetworkRequest request=new PerformNetworkRequest(Api.URL_CAPNHAT_TTPHONG,Api.actionCapNhatTTPhong,params,REQUEST_CODE,getApplicationContext(),ChiTietPhongTro.this);
        request.execute();
    }

    @Override
    public void capnhatTTdone() {
        setResult(RESULT_OK);
    }

    public void setTTphongDialog(){
        int giaphong=0;
        try {
            giaphong=currencyVN.parse(detailPhongTro.get("Giaphong")).intValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        edt_CNgiaphong.setText(String.valueOf(giaphong));
        edt_CNdientichphong.setText(detailPhongTro.get("Dientich"));
        edt_CNmotaphong.setText(detailPhongTro.get("Mota"));
    }

    public void setUpLuuHuy(){
        btn_huythaydoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoiFragment callback=(GoiFragment)callBack.get();
                callback.huythaydoi();
                line_thaydoianh.setVisibility(View.GONE);
            }
        });
        btn_luuthaydoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoiFragment callback=(GoiFragment)callBack.get();
                callback.luuthaydoi(getApplicationContext());
            }
        });
    }

}
