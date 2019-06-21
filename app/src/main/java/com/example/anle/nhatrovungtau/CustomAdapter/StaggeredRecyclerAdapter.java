package com.example.anle.nhatrovungtau.CustomAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anle.nhatrovungtau.R;
import com.example.anle.nhatrovungtau.TestKhuTro;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StaggeredRecyclerAdapter extends RecyclerView.Adapter<StaggeredRecyclerAdapter.KhuTroViewHolder> {

    private Context context;
    private List<TestKhuTro> khuTroList;

    public StaggeredRecyclerAdapter(Context context,List<TestKhuTro> khuTroList){
        this.context=context;
        this.khuTroList=khuTroList;
    }

    @NonNull
    @Override
    public KhuTroViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.itemkhutro_recyclerview,viewGroup,false);
        return new KhuTroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KhuTroViewHolder khuTroViewHolder, int i) {
        Picasso.get().load(khuTroList.get(i).getAnhKhu()).placeholder(R.drawable.icon_null_image)
                .into(khuTroViewHolder.img_KHUTRO);
        khuTroViewHolder.tv_TENKHUTRO.setText(khuTroList.get(i).getTenKhu());
        khuTroViewHolder.tv_THANHPHO.setText(khuTroList.get(i).getThanhPho());
        khuTroViewHolder.tv_DIACHI.setText(khuTroList.get(i).getDiaChi());
        khuTroViewHolder.tv_SLPHONG.setText(String.valueOf(khuTroList.get(i).getSoPhong()));
    }

    @Override
    public int getItemCount() {
        return khuTroList.size();
    }


    public class KhuTroViewHolder extends RecyclerView.ViewHolder{

        ImageView img_KHUTRO;
        TextView tv_TENKHUTRO;
        TextView tv_DIACHI;
        TextView tv_THANHPHO;
        TextView tv_SLPHONG;

        public KhuTroViewHolder(@NonNull View itemView) {
            super(itemView);
            img_KHUTRO=itemView.findViewById(R.id.img_KHUTRO);
            tv_TENKHUTRO=itemView.findViewById(R.id.tv_TENKHUTRO);
            tv_THANHPHO=itemView.findViewById(R.id.tv_THANHPHO);
            tv_DIACHI=itemView.findViewById(R.id.tv_DIACHI);
            tv_SLPHONG=itemView.findViewById(R.id.tv_SLPHONG);
        }
    }
}
