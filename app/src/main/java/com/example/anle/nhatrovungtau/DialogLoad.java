package com.example.anle.nhatrovungtau;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class DialogLoad extends Dialog {

    private String trangthai;

    public DialogLoad(Context context, String trangthai) {
        super(context);
        this.trangthai = trangthai;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_progress_bar);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tv_trangthai=findViewById(R.id.tv_phamtram);
        tv_trangthai.setText(trangthai);
        setCanceledOnTouchOutside(false);
    }

}
