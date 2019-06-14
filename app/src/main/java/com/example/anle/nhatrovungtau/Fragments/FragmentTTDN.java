package com.example.anle.nhatrovungtau.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.example.anle.nhatrovungtau.dkTKActivity;
import com.example.anle.nhatrovungtau.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class FragmentTTDN extends Fragment {
    private ViewGroup rootView;
    private DKTKTiepTheo dktkTiepTheo;
    private EditText edt_sdt,edt_tentk,edt_matkhau,edt_maxacnhan,edt_laimatkhau;
    private Button btn_guima;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=(ViewGroup)inflater.inflate(R.layout.fragment_dktk2,container,false);
        initEdittext();
        initBtn_guima();
        return rootView;
    }
    public void initEdittext(){
        edt_sdt=rootView.findViewById(R.id.edt_sdt);
        edt_tentk=rootView.findViewById(R.id.edt_tenDN);
        edt_matkhau=rootView.findViewById(R.id.edt_matkhau);
        edt_laimatkhau=rootView.findViewById(R.id.edt_laimatkhau);
        edt_maxacnhan=rootView.findViewById(R.id.edt_maxacnhan);
    }
    public void initBtn_guima(){
        btn_guima=rootView.findViewById(R.id.btn_guima);
        btn_guima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sdt="+84"+edt_sdt.getText().toString().trim();
                dktkTiepTheo.guimaxacnhan(sdt);
            }
        });
    }

    public void tieptheo(){
        dktkTiepTheo.truyenDL2(edt_sdt.getText().toString().trim(),
                edt_tentk.getText().toString().trim(),
                edt_matkhau.getText().toString().trim(),
                edt_maxacnhan.getText().toString().trim());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DKTKTiepTheo){
            dktkTiepTheo=(DKTKTiepTheo) context;
        }else {
            throw new RuntimeException(context.toString()+" phải implement DKTKTiepTheo");
        }
    }

}
