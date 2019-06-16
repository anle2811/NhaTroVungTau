package com.example.anle.nhatrovungtau;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.anle.nhatrovungtau.KhuTroPhongTro.KhuTroActivity;

import java.util.ArrayList;

public class ChuTroActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private LinearLayout ln_themkhutro;
    private TextView tv_tenchutro;
    private ImageButton ibtn_ttcn;

    private Dialog dialogTTCN;
    private EditText edt_hoten,edt_cmnd,edt_email,edt_diachi;
    private Spinner spn_ngay,spn_thang,spn_nam;
    private RadioGroup rdg_gioitinh;
    private RadioButton rd_nam,rd_nu;
    private Button btn_dong,btn_chinhsua;

    private String tenchutro,cmnd,email,diachi,gioitinh,ngaysinh;
    private int ngay,thang,nam;
    private String[] ngaySinh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chu_tro);
        initThemkhutro();
        initAll();
        loadTTchutro();
        tachNgaySinh();
        showTTCN();
    }

    public void initAll(){
        tv_tenchutro=findViewById(R.id.tv_tenchutro);
        ibtn_ttcn=findViewById(R.id.ibtn_ttcn);
    }

    public void initThemkhutro(){
        ln_themkhutro=findViewById(R.id.ln_themkhutro);
        ln_themkhutro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChuTroActivity.this,KhuTroActivity.class));
            }
        });
    }

    public void loadTTchutro(){
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (bundle!=null){
            this.tenchutro=bundle.getString("Hoten");
            tv_tenchutro.setText(tenchutro);
            this.cmnd=bundle.getString("Cmnd");
            this.email=bundle.getString("Email");
            this.diachi=bundle.getString("Diachi");
            this.gioitinh=bundle.getString("Gioitinh");
            this.ngaysinh=bundle.getString("Ngaysinh");
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

    public void showTTCN(){
        dialogTTCN=new Dialog(ChuTroActivity.this);
        View view=LayoutInflater.from(ChuTroActivity.this).inflate(R.layout.dialog_ttcn,(ViewGroup)findViewById(R.id.relLayout_ttcn),false);
        dialogTTCN.setContentView(view);
        dialogTTCN.setCanceledOnTouchOutside(false);
        dialogTTCN.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initEdtTTCN(view);
        initAndsetupSpinner(view);

        rd_nam=view.findViewById(R.id.rd_nam);
        rd_nu=view.findViewById(R.id.rd_nu);

        btn_dong=view.findViewById(R.id.btn_dong);
        btn_chinhsua=view.findViewById(R.id.btn_chinhsua);

        btn_dong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableChinhsuaTTCN(false);
                btn_chinhsua.setText("CHỈNH SỬA");
                dialogTTCN.dismiss();
            }
        });

        btn_chinhsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_chinhsua.setText("CẬP NHẬT");
                enableChinhsuaTTCN(true);
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

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
