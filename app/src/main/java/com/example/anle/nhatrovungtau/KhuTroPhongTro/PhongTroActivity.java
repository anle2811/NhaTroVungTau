package com.example.anle.nhatrovungtau.KhuTroPhongTro;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.anle.nhatrovungtau.R;

public class PhongTroActivity extends AppCompatActivity {

    private BottomSheetBehavior bottomSheetBehavior;
    private EditText edt_mota;
    private LinearLayout ln_luuvahuy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phong_tro);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initAll();
        //initBottomSheet();
    }
    public void initAll(){
        edt_mota=findViewById(R.id.edt_mota);
    }
    public void initBottomSheet(){
        View bottomSheet=findViewById(R.id.bot_sheet_pic);
        bottomSheetBehavior=BottomSheetBehavior.from(bottomSheet);

    }

}
