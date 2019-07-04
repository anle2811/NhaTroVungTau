package com.example.anle.nhatrovungtau.XemKhuTroPhongTro;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.anle.nhatrovungtau.CustomAdapter.RecycAnhPhongAdapter;
import com.example.anle.nhatrovungtau.KhuTroPhongTro.PhongTroActivity;
import com.example.anle.nhatrovungtau.KhuTroPhongTro.PhotoFullPhongTro;
import com.example.anle.nhatrovungtau.KhuTroPhongTro.PhotoFullScreen;
import com.example.anle.nhatrovungtau.Models.AnhPhong;
import com.example.anle.nhatrovungtau.PhpDB.Api;
import com.example.anle.nhatrovungtau.PhpDB.PerformNetworkRequest;
import com.example.anle.nhatrovungtau.R;
import com.example.anle.nhatrovungtau.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class FragmentAnhPhong extends Fragment implements ChiTietPhongTro.GoiFragment,PerformNetworkRequest.DSAnhPhong,RecycAnhPhongAdapter.XemAnhPhong {

    private SessionManager loginManager;

    private static final int XOAANH=1;
    private static final int THEMANH=2;
    private int ACTION;
    private int ACTION1;
    private static final int CAMERA_PERMISSION_CODE=111;
    private static final int READ_EXTERNAL_PERMISSION_CODE=222;
    private int tempRequestCode;
    private static final int REQUEST_CODE=113;
    private static final int RESULT_XOA=28;
    private static String IDPHONG;
    private static String TENTK;
    private static String IDKHUTRO;
    private ViewGroup rootView;
    private RecyclerView recyc_anhphong;
    private StaggeredGridLayoutManager manager;
    private RecycAnhPhongAdapter adapter;
    private List<AnhPhong> anhPhongList;
    private List<AnhPhong> xoaAnhPhongList;
    private List<Integer> vitriPhucHoi;
    private ChiTietPhongTro chiTietPhongTro;
    private int vitriChon;
    private boolean daLoad=false;

    private FrameLayout frame_buttonThemAnh;
    private Dialog dialog_takeOfpick;
    private LinearLayout ln_chupanh,ln_chonanh,ln_xemanh;
    private Bitmap fixBitmap;
    private int itemSelect;
    private int soluongItem;
    private int luusoluongItem;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=(ViewGroup) inflater.inflate(R.layout.fragment_anhphong,container,false);
        init();
        setLayoutRecyc();
        chiTietPhongTro=(ChiTietPhongTro)getActivity();
        IDPHONG=chiTietPhongTro.chuyenTT().get("Idphong");
        IDKHUTRO=chiTietPhongTro.chuyenTT().get("Idkhutro");
        return rootView;
    }

    public void init(){
        loginManager=new SessionManager(getActivity());
        TENTK=loginManager.getTAIKHOAN();
        recyc_anhphong=rootView.findViewById(R.id.recyc_anhphong);
        xoaAnhPhongList=new ArrayList<>();
        vitriPhucHoi=new ArrayList<>();
        frame_buttonThemAnh=rootView.findViewById(R.id.frame_buttonThemAnh);
        setDialog_takeOfpick();
        setFrame_buttonThemAnh();
    }

    public void setFrame_buttonThemAnh(){
        frame_buttonThemAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_takeOfpick.show();
            }
        });
    }

    public void setupChupAnh(int requestCode){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,requestCode); //2000
    }

    public void setupChonAnh(int requestCode){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        startActivityForResult(Intent.createChooser(intent, ""), requestCode); //2001
    }

    public void capquyenCAMERA(int requestCode){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA)==
                    PackageManager.PERMISSION_DENIED){
                String [] permission={Manifest.permission.CAMERA};
                requestPermissions(permission,CAMERA_PERMISSION_CODE);
            }else {
                setupChupAnh(requestCode);
            }
        }else {
            setupChupAnh(requestCode);
        }
    }

    public void capquyenREAD_EXTERNAL(int requestCode){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==
                    PackageManager.PERMISSION_DENIED){
                String [] permission={Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permission,READ_EXTERNAL_PERMISSION_CODE);
            }else {
                setupChonAnh(requestCode);
            }
        }else {
            setupChonAnh(requestCode);
        }
    }

    private Uri getUri() {
        String state = Environment.getExternalStorageState();
        if(!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    public String getAbsolutePath(Uri uri) {
        if(Build.VERSION.SDK_INT >= 19){
            String id = "";
            if(uri.getLastPathSegment().split(":").length > 1)
                id = uri.getLastPathSegment().split(":")[1];
            else if(uri.getLastPathSegment().split(":").length > 0)
                id = uri.getLastPathSegment().split(":")[0];
            if(id.length() > 0){
                final String[] imageColumns = {MediaStore.Images.Media.DATA };
                final String imageOrderBy = null;
                Uri tempUri = getUri();
                Cursor imageCursor = getActivity().getContentResolver().query(tempUri, imageColumns, MediaStore.Images.Media._ID + "=" + id, null, imageOrderBy);
                if (imageCursor.moveToFirst()) {
                    return imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                }else{
                    return null;
                }
            }else{
                return null;
            }
        }else{
            String[] projection = { MediaStore.MediaColumns.DATA };
            Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            } else
                return null;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_PERMISSION_CODE:{
                if (grantResults.length>0&&grantResults[0]==
                        PackageManager.PERMISSION_GRANTED){
                    setupChupAnh(tempRequestCode);
                }else {
                    Toast.makeText(getActivity(),"Bạn chưa cấp quyền chụp ảnh!!",Toast.LENGTH_SHORT).show();
                }
            }break;

            case READ_EXTERNAL_PERMISSION_CODE:{
                if (grantResults.length>0&&grantResults[0]==
                        PackageManager.PERMISSION_GRANTED){
                    setupChonAnh(tempRequestCode);
                }else {
                    Toast.makeText(getActivity(),"Bạn chưa cấp quyền truy cập file!!",Toast.LENGTH_SHORT).show();
                }
            }break;
        }
    }

    public void setDialog_takeOfpick() {
        dialog_takeOfpick = new Dialog(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.takeorpick_picdialog, (ViewGroup) getActivity().findViewById(R.id.relLayout_TakeOrPick), false);
        dialog_takeOfpick.setContentView(view);
        dialog_takeOfpick.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btn_dong = view.findViewById(R.id.btn_dong);
        ln_chupanh = view.findViewById(R.id.ln_chupanh);
        ln_chonanh = view.findViewById(R.id.ln_chonanh);
        ln_xemanh = view.findViewById(R.id.ln_xemanh);
        ln_xemanh.setVisibility(View.GONE);
        btn_dong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_takeOfpick.cancel();
            }
        });

        ln_chupanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempRequestCode=2000;
                capquyenCAMERA(2000);
            }
        });

        ln_chonanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempRequestCode=2001;
                capquyenREAD_EXTERNAL(2001);
            }
        });
    }

    public void loadAnhPhong(Context context){
        HashMap<String,String> params=new HashMap<>();
        params.put("Idphong",IDPHONG);
        PerformNetworkRequest request=new PerformNetworkRequest(Api.URL_LAY_ANHPHONG,Api.actionLayAnhPhong,params,REQUEST_CODE,context,FragmentAnhPhong.this);
        request.execute();
    }

    @Override
    public void xemAnhPhong(int position) {
        setUpAnhFull(position);
        vitriChon=position;
    }

    @Override
    public void xemAnhVuaThem(int position) {
        itemSelect=position;
        setupXemAnh(anhPhongList.get(position).getBitmap(),1999);
    }

    public void setupXemAnh(Bitmap bitmap,int requestCode){
        try {
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byte [] byteArr=stream.toByteArray();
            FileOutputStream fileOutput =getActivity().openFileOutput("img.txt",Context.MODE_PRIVATE);
            fileOutput.write(byteArr);
            fileOutput.close();
            Intent intent=new Intent(getActivity(),PhotoFullScreen.class);
            startActivityForResult(intent,requestCode);
        }catch (Exception e){
            Log.d("Loi","Loi: "+e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==123){
            if (resultCode==RESULT_OK){
                xoaAnhPhongList.add(anhPhongList.get(vitriChon));
                vitriPhucHoi.add(vitriChon);
                anhPhongList.remove(vitriChon);
                adapter.notifyDataSetChanged();
                soluongItem--;
                ChiTietPhongTro.line_thaydoianh.setVisibility(View.VISIBLE);
                ACTION=XOAANH;
            }
        }
        if (requestCode==1999){
            if (resultCode==RESULT_OK){
                byte [] byteArr=null;
                File file=new File(getActivity().getFilesDir(),"img.txt");
                try {
                    FileInputStream fileInput=new FileInputStream(file);
                    long byteLength=file.length();
                    byteArr=new byte[(int)byteLength];
                    fileInput.read(byteArr,0,(int)byteLength);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fixBitmap=BitmapFactory.decodeByteArray(byteArr,0,byteArr.length);
                anhPhongList.set(itemSelect,new AnhPhong(fixBitmap,true));
                adapter.notifyDataSetChanged();
            }
            if (resultCode==RESULT_XOA){
                anhPhongList.remove(itemSelect);
                adapter.notifyDataSetChanged();
                if (anhPhongList.size()==soluongItem){
                    ACTION1=0;
                    if (ACTION==0){
                        ChiTietPhongTro.line_thaydoianh.setVisibility(View.GONE);
                    }
                }
            }
        }
        if (requestCode==2000){
            if (resultCode==RESULT_OK){
                try {
                    fixBitmap = (Bitmap) data.getExtras().get("data");
                    anhPhongList.add(new AnhPhong(fixBitmap, true));
                    adapter.notifyDataSetChanged();
                    ChiTietPhongTro.line_thaydoianh.setVisibility(View.VISIBLE);
                    ACTION1 = THEMANH;
                }catch (Exception e){
                    Log.d("CHUP", "ERROR: "+e.getMessage());
                }
            }
        }
        if (requestCode==2001){
            if (resultCode==RESULT_OK){
                if (data.getClipData()!=null){
                    int count=data.getClipData().getItemCount();
                    for (int i=0;i<count;i++){
                        Uri imageUri=data.getClipData().getItemAt(i).getUri();
                        fixBitmap=BitmapFactory.decodeFile(getAbsolutePath(imageUri));
                        anhPhongList.add(new AnhPhong(fixBitmap,true));
                    }
                    adapter.notifyDataSetChanged();
                    ChiTietPhongTro.line_thaydoianh.setVisibility(View.VISIBLE);
                    ACTION1=THEMANH;
                }else if (data.getData()!=null){
                    fixBitmap=BitmapFactory.decodeFile(getAbsolutePath(data.getData()));
                    anhPhongList.add(new AnhPhong(fixBitmap,true));
                    adapter.notifyDataSetChanged();
                    ChiTietPhongTro.line_thaydoianh.setVisibility(View.VISIBLE);
                    ACTION1=THEMANH;
                }

            }
        }
    }

    public void phucHoi(){
        for (int k=0;k<xoaAnhPhongList.size();k++){
            anhPhongList.add(vitriPhucHoi.get(k),xoaAnhPhongList.get(k));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void luuthaydoi(Context context) {
        if (ACTION==XOAANH){
            XoaAnhPhong(context);
        }else if(ACTION1==THEMANH) {
            ThemAnhPhong(context);
        }
    }

    @Override
    public void runThemAnhPhong(Context context) {
        ACTION=0;
        if(ACTION1==THEMANH){
            ThemAnhPhong(context);
        }else {
            ChiTietPhongTro.line_thaydoianh.setVisibility(View.GONE);
        }
    }

    @Override
    public void daThemAnhPhong(Context context) {
        ACTION1=0;
        soluongItem=anhPhongList.size();
        luusoluongItem=soluongItem;
        ChiTietPhongTro.line_thaydoianh.setVisibility(View.GONE);
        loadAnhPhong(context);
    }

    public void ThemAnhPhong(Context context){
        List<String> convert=new ArrayList<>();
        for (int k=soluongItem;k<anhPhongList.size();k++){
            convert.add(ConvertImageToString(anhPhongList.get(k).getBitmap(),50));
        }
        JSONArray jsonArray=new JSONArray(convert);
        HashMap<String,String> params=new HashMap<>();
        params.put("Tentk",TENTK);
        params.put("Idkhutro",IDKHUTRO);
        params.put("Idphong",IDPHONG);
        params.put("Soanh",String.valueOf(anhPhongList.size()-soluongItem));
        params.put("ArrDataAnh",jsonArray.toString());
        PerformNetworkRequest request=new PerformNetworkRequest(Api.URL_THEM_ANHPHONG,Api.actionThemAnhPhong,params,REQUEST_CODE,context,FragmentAnhPhong.this);
        request.execute();
    }

    public String ConvertImageToString(Bitmap bitmap,int quality){
        String convertImage=null;
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        byte [] byteArray=null;
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG,quality,byteArrayOutputStream);
            byteArray=byteArrayOutputStream.toByteArray();
            convertImage=Base64.encodeToString(byteArray,Base64.DEFAULT);
        }catch (Exception e){
            Log.d("Loi","Error: "+e.getMessage());
        }
        return convertImage;
    }

    public void XoaAnhPhong(Context context){
        int soAnh=xoaAnhPhongList.size();
        List<String> IDimg=new ArrayList<>();
        List<String> NAMEimg=new ArrayList<>();
        for(int k=0;k<soAnh;k++){
            IDimg.add(xoaAnhPhongList.get(k).getIdimg());
            NAMEimg.add(xoaAnhPhongList.get(k).getUrl().substring(xoaAnhPhongList.get(k).getUrl().lastIndexOf('/')+1));
        }
        JSONArray jArrIDimg=new JSONArray(IDimg);
        JSONArray jArrNAMEimg=new JSONArray(NAMEimg);
        HashMap<String,String> params=new HashMap<>();
        params.put("Tentk",TENTK);
        params.put("Soanh",String.valueOf(soAnh));
        params.put("ArrIdimg",jArrIDimg.toString());
        params.put("ArrAnh",jArrNAMEimg.toString());
        PerformNetworkRequest request=new PerformNetworkRequest(Api.URL_XOA_ANHPHONG,Api.actionXoaAnhPhong,params,REQUEST_CODE,context,FragmentAnhPhong.this);
        request.execute();
    }

    @Override
    public void huythaydoi() {
        if (ACTION==XOAANH){
            phucHoi();
            xoaAnhPhongList=new ArrayList<>();
            vitriPhucHoi=new ArrayList<>();
            soluongItem=luusoluongItem;
        }
        if (ACTION1==THEMANH){
            huyThemAnh();
        }
        ACTION=0;
        ACTION1=0;
    }

    public void huyThemAnh(){
        Log.d("SIZE","Size: "+anhPhongList.size());
        for (int k=anhPhongList.size()-1;k>=soluongItem;k--){
            anhPhongList.remove(k);
            Log.d("SIZE","Size: "+k);
        }
        adapter.notifyDataSetChanged();
    }

    public void setUpAnhFull(int positon){
        Intent intent=new Intent(getActivity(),PhotoFullPhongTro.class);
        Bundle bundle=new Bundle();
        bundle.putString("URL",anhPhongList.get(positon).getUrl());
        intent.putExtras(bundle);
        startActivityForResult(intent,123);
    }

    public void setLayoutRecyc(){
        manager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyc_anhphong.setLayoutManager(manager);
    }


    @Override
    public void goiFragment(Context context) {
        if (!daLoad){
            loadAnhPhong(context);
        }
    }

    @Override
    public void layDSanhphong(JSONArray dsUrlAnh) {
        anhPhongList=new ArrayList<>();
        for (int k=0;k<dsUrlAnh.length();k++){
            try {
                JSONObject object=dsUrlAnh.getJSONObject(k);
                anhPhongList.add(new AnhPhong(String.valueOf(object.getInt("Idimg")),object.getString("Url"),false));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter=new RecycAnhPhongAdapter(getActivity(),anhPhongList,FragmentAnhPhong.this);
        recyc_anhphong.setAdapter(adapter);
        daLoad = true;
        soluongItem=anhPhongList.size();
        luusoluongItem=soluongItem;
        Log.d("SIZE","Size: "+soluongItem);
    }
}
