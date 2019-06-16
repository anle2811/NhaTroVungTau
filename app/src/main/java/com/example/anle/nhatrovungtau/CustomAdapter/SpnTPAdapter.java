package com.example.anle.nhatrovungtau.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.anle.nhatrovungtau.Models.ThanhPho;
import com.example.anle.nhatrovungtau.R;

import java.util.List;

public class SpnTPAdapter extends ArrayAdapter<ThanhPho> {
    private Context context;
    private int resource;
    private List<ThanhPho> thanhPhoList;

    public SpnTPAdapter(Context context,int resource,List<ThanhPho> thanhPhoList){
        super(context,resource,thanhPhoList);
        this.context=context;
        this.resource=resource;
        this.thanhPhoList=thanhPhoList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView=LayoutInflater.from(context).inflate(resource,parent,false);
        }

        ThanhPho thanhPho=thanhPhoList.get(position);
        TextView tv_tentp=convertView.findViewById(R.id.tv_tentp);
        tv_tentp.setText(thanhPho.getTenhienthi());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView=LayoutInflater.from(context).inflate(R.layout.custom_dropdown_tp,parent,false);
        }

        ThanhPho thanhPho=thanhPhoList.get(position);
        TextView tv_drop_tentp=convertView.findViewById(R.id.tv_drop_tentp);
        tv_drop_tentp.setText(thanhPho.getTenhienthi());

        return convertView;
    }
}
