package com.example.anle.nhatrovungtau;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.anle.nhatrovungtau.Fragments.DKTKTiepTheo;
import com.example.anle.nhatrovungtau.Fragments.FragmentTTCB;
import com.example.anle.nhatrovungtau.Fragments.FragmentTTDN;
import com.example.anle.nhatrovungtau.PhpDB.Api;
import com.example.anle.nhatrovungtau.PhpDB.PerformNetworkRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class dkTKActivity extends AppCompatActivity implements DKTKTiepTheo,PerformNetworkRequest.XulyDK {
    private static final String TAG=dkTKActivity.class.getSimpleName();
    private static final int REQUEST_CODE=113;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private Button btn_tieptuc,btn_trolai;
    private List<Fragment> list;
    private HashMap<String,String> params;
    private HashMap<String,String> paramsTentk;
    private FragmentTTCB fragmentTTCB;
    private FragmentTTDN fragmentTTDN;
    public static DialogLoad ktTkDialog;
    public static DialogLoad taotkDialog;

    private String maxacnhan;
    private FirebaseAuth mAuth;
    private String VerificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dk_tk);
        init();
        list=new ArrayList<>();
        list.add(fragmentTTCB);
        list.add(fragmentTTDN);
        viewPager=findViewById(R.id.pager);
        pagerAdapter=new SlidePagerAdapter(getSupportFragmentManager(),list);
        viewPager.setAdapter(pagerAdapter);
        initButton();
        if (viewPager.getCurrentItem()==0){
            btn_trolai.setVisibility(View.INVISIBLE);
        }

    }
    public void init(){
        fragmentTTCB=new FragmentTTCB();
        fragmentTTDN=new FragmentTTDN();
        params=new HashMap<>();
        paramsTentk=new HashMap<>();
        ktTkDialog=new DialogLoad(this,"Đang kiểm tra tên tài khoản...");
        taotkDialog=new DialogLoad(this,"Đang tạo tài khoản...");
        mAuth=FirebaseAuth.getInstance();
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            /*String code=phoneAuthCredential.getSmsCode();
            if (code!=null){
                Log.d(TAG,"Code: "+code);
                verifyVerificationCode(code);
            }*/
        }
        @Override
        public void onVerificationFailed(FirebaseException e) { //Phương thức này được gọi khi không gửi được code xác minh
            Toast.makeText(dkTKActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) { //Phương thức này đưuọc gọi khi code được gửi thành công
            super.onCodeSent(s, forceResendingToken);
            VerificationId=s;
            Toast.makeText(dkTKActivity.this,"Gửi mã xác nhận thành công",Toast.LENGTH_LONG).show();
        }
    };
    @Override
    public void guimaxacnhan(String sdt) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                sdt,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                dkTKActivity.this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        Toast.makeText(dkTKActivity.this,"Đang gửi...",Toast.LENGTH_SHORT).show();
    }
    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerificationId, code);
        //signing the user
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(dkTKActivity.this,"Xác thực thành công",Toast.LENGTH_SHORT).show();
                            kiemtraTentk();
                        }else {
                            Toast.makeText(dkTKActivity.this,"Xác minh thất bại ",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void truyenDL(String Hoten, String Cmnd, String Email, String Diachi, String Ngaysinh, String Gioitinh) {
       params.put("Hoten",Hoten);
       params.put("Gioitinh",Gioitinh);
       params.put("Ngaysinh",Ngaysinh);
       params.put("Cmnd",Cmnd);
       params.put("Email",Email);
       params.put("Diachi",Diachi);
       nextStep();
    }

    public void nextStep(){
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        btn_trolai.setVisibility(View.VISIBLE);
    }

    @Override
    public void truyenDL2(String Sdt, String Tentk, String Matkhau,String maxacnhan) {
        paramsTentk.put("Tentk",Tentk);
        params.put("Tentk",Tentk);
        params.put("Matkhau",Matkhau);
        params.put("Sdt",Sdt);
        this.maxacnhan=maxacnhan;
        Log.d(TAG,"Sequence: "+Sdt+" "+Tentk+" "+Matkhau);
        verifyVerificationCode(this.maxacnhan);
    }

    @Override
    public void exeTaotaikhoan() {
        Log.d("MainActivity","extTaotaikhoan: called");
        taoTaiKhoan();
    }

    @Override
    public void gotoDangNhap() {
        Intent intent=new Intent(dkTKActivity.this,DangNhapActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("Taikhoan",params.get("Tentk"));
        bundle.putString("Matkhau",params.get("Matkhau"));
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    public void taoTaiKhoan(){
        PerformNetworkRequest request=new PerformNetworkRequest(Api.URL_TAO_TAIKHOAN,Api.actionTaoTK,params,REQUEST_CODE,getApplicationContext(),this);
        request.execute();
    }

    public void kiemtraTentk(){
        PerformNetworkRequest request=new PerformNetworkRequest(Api.URL_ACC_EXIST_CHECK,Api.actionExistCheck,paramsTentk,REQUEST_CODE,getApplicationContext(),this);
        request.execute();
    }

    public void initButton(){
        btn_tieptuc=findViewById(R.id.btn_tieptuc);
        btn_trolai=findViewById(R.id.btn_trolai);

        btn_tieptuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem()==0) {
                    fragmentTTCB.tieptheo();
                    return;
                }
                if (viewPager.getCurrentItem()==1){
                    fragmentTTDN.tieptheo();
                    return;
                }

            }
        });
        btn_trolai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
                if (viewPager.getCurrentItem()==0){
                    btn_trolai.setVisibility(View.INVISIBLE);
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        }
        else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            btn_trolai.setVisibility(View.INVISIBLE);
        }
    }

}
