package com.example.anle.nhatrovungtau;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anle.nhatrovungtau.CustomAdapter.StaggeredRecyclerAdapter;
import com.example.anle.nhatrovungtau.KhuTroPhongTro.KhuTroActivity;
import com.example.anle.nhatrovungtau.PhpDB.Api;
import com.example.anle.nhatrovungtau.PhpDB.PerformNetworkRequest;
import com.example.anle.nhatrovungtau.XemKhuTroPhongTro.ChiTietKhuTro;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChuTroActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,PerformNetworkRequest.TTChuTro,StaggeredRecyclerAdapter.ChonKhuTro {

    private SessionManager loginManager;

    private static String TENTK;
    private static final int REQUEST_CODE=113;
    public static DialogLoad TTCNload;
    public static DialogLoad LayTTload;

    private LinearLayout ln_themkhutro;
    private TextView tv_tenchutro;
    private ImageButton ibtn_dangxuat;
    private ImageButton ibtn_ttcn;
    private ImageButton ibtn_lammoiDS;
    public static RelativeLayout rel_reload;
    public static RelativeLayout rel_nokhutro;

    private Dialog dialogTTCN;
    private View viewTTCN;
    private EditText edt_hoten,edt_cmnd,edt_email,edt_diachi;
    private Spinner spn_ngay,spn_thang,spn_nam;
    private RadioGroup rdg_gioitinh;
    private RadioButton rd_nam,rd_nu;
    private Button btn_dong,btn_chinhsua;

    private String tenchutro,cmnd,email,diachi,gioitinh,ngaysinh;
    private int ngay,thang,nam;
    private String[] ngaySinh;

    private RecyclerView staggeredRC;
    private StaggeredGridLayoutManager manager;
    private StaggeredRecyclerAdapter adapter;
    private List<TestKhuTro> khuTroList;

    public static DialogLoad DSkhutroLoad;
    private static final int RESULT_CN=11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chu_tro);
        initThemkhutro();
        initAll();
        showTTCN();
        yeuCauTTchutro();
        initDSkhutro();
        setupLammoiDS();
        setUpReloadTT();
    }

    public void initAll(){
        loginManager=new SessionManager(getApplicationContext());
        tv_tenchutro=findViewById(R.id.tv_tenchutro);
        ibtn_dangxuat=findViewById(R.id.ibtn_dangxuat);
        ibtn_ttcn=findViewById(R.id.ibtn_ttcn);
        ibtn_lammoiDS=findViewById(R.id.ibtn_qltro);
        rel_reload=findViewById(R.id.rel_reload);
        rel_nokhutro=findViewById(R.id.rel_NoKhuTro);
        LayTTload=new DialogLoad(this,"Đang tải thông tin...");
        DSkhutroLoad=new DialogLoad(this,"Đang làm mới danh sách Khu Trọ...");
        checkLoged();
        dangxuat();
    }

    public void dangxuat(){
        ibtn_dangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginManager.logOut();
            }
        });
    }

    public void checkLoged(){
        try {
            loginManager.checkLogin();
            TENTK=loginManager.getTAIKHOAN();
            Log.d("KT","TENTK_:"+TENTK);
        }catch (Exception e){
            Log.d("KT","..."+e.getMessage());
        }

    }

    public void initThemkhutro(){
        ln_themkhutro=findViewById(R.id.ln_themkhutro);
        ln_themkhutro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChuTroActivity.this,KhuTroActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("TENTK",TENTK);
                intent.putExtras(bundle);
                startActivityForResult(intent,666);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==666){
            if (resultCode==RESULT_OK){
                setUpLayDSKhuTro();
            }
        }
        if (requestCode==446){
            if (resultCode==RESULT_OK){
                setUpLayDSKhuTro();
            }
        }
    }

    public void setUpReloadTT(){
        rel_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yeuCauTTchutro();
            }
        });
    }

    public void yeuCauTTchutro(){
        HashMap<String,String> params=new HashMap<>();
        params.put("Tentk",TENTK);
        Log.d("KT","TENTK:__"+TENTK);
        PerformNetworkRequest request=new PerformNetworkRequest(Api.URL_LAY_TT_CHUTRO,Api.actionLayTTchutro,params,REQUEST_CODE,getApplicationContext(),this);
        request.execute();
    }

    public void setupLammoiDS(){
        ibtn_lammoiDS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpLayDSKhuTro();
            }
        });
    }

    public void setUpLayDSKhuTro(){
        HashMap<String,String> params=new HashMap<>();
        params.put("Tentk",TENTK);
        PerformNetworkRequest request=new PerformNetworkRequest(Api.URL_LAY_DS_KHUTRO,Api.actionLayDSkhutro,
                params,REQUEST_CODE,getApplicationContext(),ChuTroActivity.this);
        request.execute();
    }

    public void initDSkhutro(){
        khuTroList=new ArrayList<>();
        staggeredRC=findViewById(R.id.recyc_KhuTro);
        manager=new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        staggeredRC.setLayoutManager(manager);
        adapter=new StaggeredRecyclerAdapter(ChuTroActivity.this,khuTroList,ChuTroActivity.this);
        staggeredRC.setAdapter(adapter);
    }

    @Override
    public void xemChiTiet(int position) {
        chuyenTTKhuTro(position);
    }

    public void chuyenTTKhuTro(int position){
        Intent intent=new Intent(ChuTroActivity.this,ChiTietKhuTro.class);
        Bundle bundle=new Bundle();
        bundle.putString("Tentk",TENTK);
        bundle.putString("Idkhutro",String.valueOf(khuTroList.get(position).getIdkhutro()));
        bundle.putString("Tenkhutro",khuTroList.get(position).getTenKhu());
        bundle.putString("Diachikhu",khuTroList.get(position).getDiaChi());
        bundle.putString("Thanhpho",khuTroList.get(position).getThanhPho());
        bundle.putString("Phuong",khuTroList.get(position).getPhuong());
        bundle.putString("Motakhu",khuTroList.get(position).getMota());
        bundle.putString("Image",khuTroList.get(position).getAnhKhu());
        intent.putExtras(bundle);
        startActivityForResult(intent,446);
    }

    @Override
    public void layDSkhutro(JSONArray dskhutro) {
        if (dskhutro.length()>0) {
            khuTroList.clear();
            for (int k = 0; k < dskhutro.length(); k++) {
                try {
                    JSONObject object = dskhutro.getJSONObject(k);
                    Log.d("Loi", "JSONObject " + object.toString());
                    khuTroList.add(new TestKhuTro(object.getInt("Idkhutro"), object.getDouble("Lat"),
                            object.getDouble("Lng"),
                            object.getString("Tenkhutro"),
                            object.getInt("Slphong"),
                            object.getString("Diachi"),
                            object.getString("Mota"),
                            object.getString("Img"),
                            object.getString("Tentp"),
                            object.getString("Phuong")));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Loi", "Error: " + e.getMessage());
                }
            }
            adapter.notifyDataSetChanged();
            rel_nokhutro.setVisibility(View.GONE);
        }else {
            rel_nokhutro.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void layTTchutro(JSONObject jsonObject) {
        try {
            this.tenchutro=jsonObject.getString("hoten");
            tv_tenchutro.setText(tenchutro);
            this.cmnd=jsonObject.getString("cmnd");
            this.email=jsonObject.getString("email");
            this.diachi=jsonObject.getString("diachi");
            this.gioitinh=jsonObject.getString("gioitinh");
            this.ngaysinh=jsonObject.getString("ngaysinh");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        tachNgaySinh();
        setUpLayDSKhuTro();
    }

    public void loadTTchutro(){
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (bundle!=null){
            TENTK=bundle.getString("TENTK");
        }
    }

    public void tachNgaySinh(){
        ngaySinh=this.ngaysinh.split("/");
        ngay=Integer.parseInt(ngaySinh[0]);
        thang=Integer.parseInt(ngaySinh[1]);
        nam=Integer.parseInt(ngaySinh[2]);
    }
    public void updateSpnNgaySinh(){
        for(int k=1;k<=31;k++){
            if (ngay==k){
                spn_ngay.setSelection(k-1);
                break;
            }
        }

        for(int k=1;k<=12;k++){
            if (thang==k){
                spn_thang.setSelection(k-1);
                break;
            }
        }

        int dem=0;
        for(int k=2019;k>=1960;k--){
            dem++;
            if (nam==k){
                spn_nam.setSelection(dem-1);
                break;
            }
        }
    }

    public void updateRadioGioiTinh(){
        if (this.gioitinh.equals("Nam")){
            rd_nam.setChecked(true);
        }else {
            rd_nu.setChecked(true);
        }
    }

    public void initChonGioitinh(View view){
        rdg_gioitinh=view.findViewById(R.id.rdg_gioitinh);
        rdg_gioitinh.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()){
                    case R.id.rd_nam: ChuTroActivity.this.gioitinh="Nam";break;
                    case R.id.rd_nu: ChuTroActivity.this.gioitinh="Nữ";break;
                }
            }
        });
    }

    public void capNhatTT(){
        this.tenchutro=edt_hoten.getText().toString().trim();
        this.cmnd=edt_cmnd.getText().toString().trim();
        this.email=edt_email.getText().toString().trim();
        this.diachi=edt_diachi.getText().toString().trim();

        HashMap<String,String> params=new HashMap<>();
        params.put("Hoten",tenchutro);
        params.put("Cmnd",cmnd);
        params.put("Email",email);
        params.put("Diachi",diachi);
        params.put("Ngaysinh",ngaysinh);
        params.put("Gioitinh",gioitinh);
        params.put("Tentk",TENTK);

        PerformNetworkRequest request=new PerformNetworkRequest(Api.URL_UPDATE_TT_CHUTRO,
                Api.actionUpdateTTchutro,params,REQUEST_CODE,getApplicationContext(),this);
        request.execute();
    }

    @Override
    public void updateTT() {
        dongDialogTTCN();
        tachNgaySinh();
    }

    protected void dongDialogTTCN(){
        enableChinhsuaTTCN(false);
        btn_chinhsua.setText("CHỈNH SỬA");
        tv_tenchutro.setText(tenchutro);
        dialogTTCN.dismiss();
    }

    public void showTTCN(){
        dialogTTCN=new Dialog(ChuTroActivity.this);
        viewTTCN=LayoutInflater.from(ChuTroActivity.this).inflate(R.layout.dialog_ttcn,(ViewGroup)findViewById(R.id.relLayout_ttcn),false);
        dialogTTCN.setContentView(viewTTCN);
        dialogTTCN.setCanceledOnTouchOutside(false);
        dialogTTCN.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TTCNload =new DialogLoad(dialogTTCN.getContext(),"Đang lưu lại cập nhật...");

        initEdtTTCN(viewTTCN);
        initAndsetupSpinner(viewTTCN);
        initChonGioitinh(viewTTCN);

        rd_nam=viewTTCN.findViewById(R.id.rd_nam);
        rd_nu=viewTTCN.findViewById(R.id.rd_nu);

        btn_dong=viewTTCN.findViewById(R.id.btn_dong);
        btn_chinhsua=viewTTCN.findViewById(R.id.btn_chinhsua);

        btn_dong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dongDialogTTCN();
            }
        });

        btn_chinhsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edt_hoten.isEnabled()){
                    btn_chinhsua.setText("CẬP NHẬT");
                    enableChinhsuaTTCN(true);
                }else {
                    capNhatTT();
                }
            }
        });

        ibtn_ttcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEdtTTCN();
                updateSpnNgaySinh();
                updateRadioGioiTinh();
                enableChinhsuaTTCN(false);
                dialogTTCN.show();
            }
        });
    }

    public void initEdtTTCN(View view){
        edt_hoten=view.findViewById(R.id.edt_hoten);
        edt_cmnd=view.findViewById(R.id.edt_cmnd);
        edt_email=view.findViewById(R.id.edt_email);
        edt_diachi=view.findViewById(R.id.edt_diachi);
    }

    public void setEdtTTCN(){
        edt_hoten.setText(this.tenchutro);
        edt_cmnd.setText(this.cmnd);
        edt_email.setText(this.email);
        edt_diachi.setText(this.diachi);
    }

    public void enableChinhsuaTTCN(boolean TofF){
        edt_hoten.setEnabled(TofF);
        edt_cmnd.setEnabled(TofF);
        edt_email.setEnabled(TofF);
        edt_diachi.setEnabled(TofF);

        spn_ngay.setEnabled(TofF);
        spn_thang.setEnabled(TofF);
        spn_nam.setEnabled(TofF);

        rd_nam.setClickable(TofF);
        rd_nu.setClickable(TofF);
    }


    public void initAndsetupSpinner(View rootView){
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
       ngaysinh=spn_ngay.getSelectedItem().toString()+
                "/"+spn_thang.getSelectedItem().toString()+
                "/"+spn_nam.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
