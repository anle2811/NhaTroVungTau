package com.example.anle.nhatrovungtau.CustomAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anle.nhatrovungtau.R;
import com.example.anle.nhatrovungtau.TestKhuTro;
import com.example.anle.nhatrovungtau.XemKhuTroPhongTro.ChiTietKhuTro;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;

public class StaggeredRecyclerAdapter extends RecyclerView.Adapter<StaggeredRecyclerAdapter.KhuTroViewHolder> {

    private Context context;
    private List<TestKhuTro> khuTroList;
    private WeakReference<Object> mcallBack;

    public StaggeredRecyclerAdapter(Context context,List<TestKhuTro> khuTroList,Object callBack){
        this.context=context;
        this.khuTroList=khuTroList;
        this.mcallBack=new WeakReference<>(callBack);
    }

    public interface ChonKhuTro{
        void xemChiTiet(int position);
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

        khuTroViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(context,"Ban chon: "+khuTroList.get(position).getTenKhu(),Toast.LENGTH_SHORT).show();
                final ChonKhuTro callBack=(ChonKhuTro) mcallBack.get();
                callBack.xemChiTiet(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return khuTroList.size();
    }


    public class KhuTroViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView img_KHUTRO;
        TextView tv_TENKHUTRO;
        TextView tv_DIACHI;
        TextView tv_THANHPHO;
        TextView tv_SLPHONG;

        private ItemClickListener itemClickListener;

        public KhuTroViewHolder(@NonNull View itemView) {
            super(itemView);
            img_KHUTRO=itemView.findViewById(R.id.img_KHUTRO);
            tv_TENKHUTRO=itemView.findViewById(R.id.tv_TENKHUTRO);
            tv_THANHPHO=itemView.findViewById(R.id.tv_THANHPHO);
            tv_DIACHI=itemView.findViewById(R.id.tv_DIACHI);
            tv_SLPHONG=itemView.findViewById(R.id.tv_SLPHONG);
            itemView.setOnClickListener(this);
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
