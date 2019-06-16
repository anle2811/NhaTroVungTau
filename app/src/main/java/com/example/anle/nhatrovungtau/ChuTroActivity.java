package com.example.anle.nhatrovungtau;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.anle.nhatrovungtau.KhuTroPhongTro.KhuTroActivity;

public class ChuTroActivity extends AppCompatActivity {

    private LinearLayout ln_themkhutro;
    private TextView tv_tenchutro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chu_tro);
        initThemkhutro();
        initAll();
        loadTTchutro();
    }

    public void initAll(){
        tv_tenchutro=findViewById(R.id.tv_tenchutro);
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
            tv_tenchutro.setText(bundle.getString("Hoten"));
        }
    }
}
