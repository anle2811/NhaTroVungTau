package com.example.anle.nhatrovungtau.CustomAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.anle.nhatrovungtau.R;

import java.util.List;

public class GridViewAnhPhongAdapter extends ArrayAdapter<Bitmap> {

    private Context context;
    private int resource;
    private List<Bitmap> bitmapList;

    public GridViewAnhPhongAdapter(Context context,int resource,List<Bitmap> bitmapList){
        super(context,resource,bitmapList);
        this.context=context;
        this.resource=resource;
        this.bitmapList=bitmapList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            convertView=LayoutInflater.from(context).inflate(resource,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.img_anhPhong=convertView.findViewById(R.id.img_anhPhong);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        Bitmap bitmap=bitmapList.get(position);
        viewHolder.img_anhPhong.setImageBitmap(bitmap);
        return convertView;
    }

    public class ViewHolder{
        ImageView img_anhPhong;
    }
}
