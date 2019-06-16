package com.example.anle.nhatrovungtau;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.anle.nhatrovungtau.KhuTroPhongTro.KhuTroActivity;

public class ChuTroActivity extends AppCompatActivity {

    private LinearLayout ln_themkhutro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chu_tro);
        initThemkhutro();
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
}
