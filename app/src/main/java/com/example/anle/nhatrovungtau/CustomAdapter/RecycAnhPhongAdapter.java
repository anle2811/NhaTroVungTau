package com.example.anle.nhatrovungtau.CustomAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.anle.nhatrovungtau.Models.AnhPhong;
import com.example.anle.nhatrovungtau.R;
import com.example.anle.nhatrovungtau.TestKhuTro;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;

public class RecycAnhPhongAdapter extends RecyclerView.Adapter<RecycAnhPhongAdapter.AnhPhongViewHolder> {

    private Context context;
    private List<AnhPhong> anhPhongList;
    private WeakReference<Object> mcallBack;

    public RecycAnhPhongAdapter(Context context,List<AnhPhong> urlImgPhong_list){
        this.context=context;
        this.anhPhongList=urlImgPhong_list;
    }

    @NonNull
    @Override
    public AnhPhongViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.recyc_anhphong,viewGroup,false);
        return new AnhPhongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnhPhongViewHolder anhPhongViewHolder, int i) {
        AnhPhong anhPhong=anhPhongList.get(i);
        Picasso.get().load(anhPhong.getUrl()).placeholder(R.drawable.icon_null_image).into(anhPhongViewHolder.img_recyc_item_anhphong);
    }

    @Override
    public int getItemCount() {
        return anhPhongList.size();
    }

    public class AnhPhongViewHolder extends RecyclerView.ViewHolder{

        private ImageView img_recyc_item_anhphong;

        public AnhPhongViewHolder(View view){
            super(view);
            img_recyc_item_anhphong=view.findViewById(R.id.img_recyc_item_anhphong);
        }
    }
}
