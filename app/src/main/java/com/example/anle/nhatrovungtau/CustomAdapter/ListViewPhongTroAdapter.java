package com.example.anle.nhatrovungtau.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anle.nhatrovungtau.Models.PhongTro;
import com.example.anle.nhatrovungtau.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListViewPhongTroAdapter extends ArrayAdapter<PhongTro> {

    private Context context;
    private int resource;
    private List<PhongTro> phongTroList;

    public ListViewPhongTroAdapter(Context context, int resource, List<PhongTro> phongTroList) {
        super(context, resource, phongTroList);
        this.context = context;
        this.resource = resource;
        this.phongTroList = phongTroList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            convertView=LayoutInflater.from(context).inflate(resource,parent,false);
            viewHolder=new ListViewPhongTroAdapter.ViewHolder();
            viewHolder.img_hinhphong=convertView.findViewById(R.id.img_hinhphong);
            viewHolder.tv_giaphong=convertView.findViewById(R.id.tv_giaphong);
            viewHolder.tv_dientich=convertView.findViewById(R.id.tv_dientich);
            viewHolder.tv_songuoi=convertView.findViewById(R.id.tv_songuoi);
            viewHolder.img_trangthaighep=convertView.findViewById(R.id.img_trangthaighep);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        PhongTro phongTro=phongTroList.get(position);
        Picasso.get().load(phongTro.getImg()).placeholder(R.drawable.icon_null_image)
                .into(viewHolder.img_hinhphong);
        viewHolder.tv_giaphong.setText(phongTro.getGiaphong());
        viewHolder.tv_dientich.setText(phongTro.getDientich());
        viewHolder.tv_songuoi.setText(phongTro.getTrangthai()+"");
        if (phongTro.getGhep()!=0){
            viewHolder.img_trangthaighep.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public class ViewHolder{
        private ImageView img_hinhphong;
        private TextView tv_giaphong;
        private TextView tv_dientich;
        private TextView tv_songuoi;
        private ImageView img_trangthaighep;
    }
}
