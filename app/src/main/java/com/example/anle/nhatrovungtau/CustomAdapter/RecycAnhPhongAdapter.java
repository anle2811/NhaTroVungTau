package com.example.anle.nhatrovungtau.CustomAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.anle.nhatrovungtau.Models.AnhPhong;
import com.example.anle.nhatrovungtau.R;
import com.example.anle.nhatrovungtau.TestKhuTro;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;
import java.util.List;

public class RecycAnhPhongAdapter extends RecyclerView.Adapter<RecycAnhPhongAdapter.AnhPhongViewHolder> {

    private Context context;
    private List<AnhPhong> anhPhongList;
    private WeakReference<Object> mcallBack;

    public RecycAnhPhongAdapter(Context context,List<AnhPhong> urlImgPhong_list,Object callback){
        this.context=context;
        this.anhPhongList=urlImgPhong_list;
        this.mcallBack=new WeakReference<>(callback);
    }

    public interface XemAnhPhong{
        void xemAnhPhong(int position);
        void xemAnhVuaThem(int position);
    }

    @NonNull
    @Override
    public AnhPhongViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.recyc_anhphong,viewGroup,false);
        return new AnhPhongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnhPhongViewHolder anhPhongViewHolder, int i) {
        final AnhPhong anhPhong=anhPhongList.get(i);
        if (anhPhong.isType()==false){
            Picasso.get().load(anhPhong.getUrl()).placeholder(R.drawable.icon_null_image).into(anhPhongViewHolder.img_recyc_item_anhphong);
        }
        if (anhPhong.isType()==true){
            anhPhongViewHolder.img_recyc_item_anhphong.setImageBitmap(anhPhong.getBitmap());
        }
        anhPhongViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (anhPhongList.get(position).isType()==false){
                    final XemAnhPhong callBack=(XemAnhPhong) mcallBack.get();
                    callBack.xemAnhPhong(position);
                }else {
                    final XemAnhPhong callBack=(XemAnhPhong) mcallBack.get();
                    callBack.xemAnhVuaThem(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return anhPhongList.size();
    }

    public class AnhPhongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView img_recyc_item_anhphong;

        private ItemClickListener itemClickListener;

        public AnhPhongViewHolder(View view){
            super(view);
            img_recyc_item_anhphong=view.findViewById(R.id.img_recyc_item_anhphong);
            view.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener=itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition());
        }

    }
}
