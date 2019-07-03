package com.example.anle.nhatrovungtau.KhuTroPhongTro;

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
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anle.nhatrovungtau.CustomAdapter.GridViewAnhPhongAdapter;
import com.example.anle.nhatrovungtau.DialogLoad;
import com.example.anle.nhatrovungtau.PhpDB.Api;
import com.example.anle.nhatrovungtau.PhpDB.PerformNetworkRequest;
import com.example.anle.nhatrovungtau.R;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PhongTroActivity extends AppCompatActivity {

    private static String TENTK;
    private static String IDKHUTRO;
    private static final int REQUEST_CODE=113;
    private static final int RESULT_XOA=28;
    public static DialogLoad loadThemPhong;

    private static final int CAMERA_PERMISSION_CODE=111;
    private static final int READ_EXTERNAL_PERMISSION_CODE=222;
    private int tempRequestCode;
    private int checkChon;
    private boolean checkShowXemAnh=false;

    private LinearLayout botsheet;
    private FrameLayout frame_phongtro;
    private ImageView img_qlAnhphong;
    private TextView tv_iconQlanh;
    private BottomSheetBehavior bottomSheetBehavior;
    private EditText edt_giaphong,edt_dientich,edt_mota;
    private Button btn_themAnh;
    private Button btn_huythemphong;
    private Button btn_luuphong;
    private ImageView img_avtphong;

    private Dialog dialog_takeOfpick;
    private LinearLayout ln_chupanh;
    private LinearLayout ln_chonanh;
    private LinearLayout ln_xemanh;

    private Bitmap avtBitmap;
    private Bitmap fixBitmap;
    private GridView gridView_anhPhong;
    private List<Bitmap> bitmapList;
    private GridViewAnhPhongAdapter anhPhongAdapter;

    private int itemSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phong_tro);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getTKandID();
        initAll();
        initBottomSheet();
        setDialog_takeOfpick();
        setupChonAvtPhong();
        setupGridViewImgPhong();
        PhucHoiTrangThaiGridAnhPhong(savedInstanceState);
        setBtn_luuphong();
    }

    public void getTKandID(){
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (bundle!=null){
            TENTK=bundle.getString("Tentk");
            IDKHUTRO=bundle.getString("Idkhutro");
            Log.d("GET","GET: "+TENTK+"_"+IDKHUTRO);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("SizeListBitmap",bitmapList.size());
        LuuTrangThaiGridAnhPhong();
    }

    public void LuuTrangThaiGridAnhPhong(){
        int numItem=bitmapList.size();
        for (int k=0;k<numItem;k++){
            try {
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmapList.get(k).compress(Bitmap.CompressFormat.JPEG,100,stream);
                byte [] byteArr=stream.toByteArray();
                FileOutputStream fileOutput = openFileOutput("img"+k+".txt",Context.MODE_PRIVATE);
                fileOutput.write(byteArr);
                fileOutput.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void PhucHoiTrangThaiGridAnhPhong(Bundle savedInstanceState){
        if (savedInstanceState!=null){
            int numItem=savedInstanceState.getInt("SizeListBitmap");
            for (int k=0;k<numItem;k++){
                byte [] byteArr=null;
                File file=new File(getFilesDir(),"img"+k+".txt");
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
                bitmapList.add(fixBitmap);
            }
            anhPhongAdapter.notifyDataSetChanged();
        }
    }

    public void initAll(){
        edt_giaphong=findViewById(R.id.edt_giaphong);
        edt_dientich=findViewById(R.id.edt_dientich);
        edt_mota=findViewById(R.id.edt_mota);
        img_avtphong=findViewById(R.id.img_avtphong);
        btn_themAnh=findViewById(R.id.btn_themAnh);
        btn_huythemphong=findViewById(R.id.btn_huythemphong);
        btn_luuphong=findViewById(R.id.btn_luuphong);
        gridView_anhPhong=findViewById(R.id.gridView_anhPhong);
        loadThemPhong=new DialogLoad(this,"Đang thêm phòng trọ...");
    }

    public void setBtn_luuphong(){
        btn_luuphong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadPhong();
            }
        });
    }

    public void UploadPhong(){
        String convertImage=ConvertImageToString(avtBitmap,60);
        List<String> convert=new ArrayList<>();
        for (int k=0;k<bitmapList.size();k++){
            convert.add(ConvertImageToString(bitmapList.get(k),50));
        }
        JSONArray jsonArray=new JSONArray(convert);
        HashMap<String,String> params=new HashMap<>();
        params.put("Tentk",TENTK);
        params.put("Idkhutro",IDKHUTRO);
        params.put("Giaphong",edt_giaphong.getText().toString().trim());
        params.put("Dientich",edt_dientich.getText().toString().trim());
        params.put("Mota",edt_mota.getText().toString().trim());
        params.put("ImgData",convertImage);
        params.put("Soanh",String.valueOf(bitmapList.size()));
        params.put("ArrDataAnh",jsonArray.toString());
        PerformNetworkRequest request=new PerformNetworkRequest(Api.URL_THEM_PHONGTRO,Api.actionThemPhongTro,params,
                REQUEST_CODE,getApplicationContext(),PhongTroActivity.this);
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

    public void setupChonAvtPhong(){
        img_avtphong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkChon=1;
                if (checkShowXemAnh){
                    ln_xemanh.setVisibility(View.VISIBLE);
                }else {
                    ln_xemanh.setVisibility(View.GONE);
                }
                dialog_takeOfpick.show();
            }
        });
    }

    public void initBottomSheet(){
        View bottomSheet=findViewById(R.id.bot_sheet_pic);
        bottomSheetBehavior=BottomSheetBehavior.from(bottomSheet);
        botsheet=findViewById(R.id.bot_sheet_pic);
        img_qlAnhphong=findViewById(R.id.img_qlAnhphong);
        frame_phongtro=findViewById(R.id.frame_phongtro);
        tv_iconQlanh=findViewById(R.id.tv_iconQlAnh);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i==BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setPeekHeight(botsheet.getHeight());
                    tv_iconQlanh.setText("Nhấp vào icon để đóng");
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
        img_qlAnhphong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setPeekHeight(frame_phongtro.getHeight());
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    tv_iconQlanh.setText("Vuốt lên để quản lý ảnh");
                }
            }
        });

    }

    public void setDialog_takeOfpick() {
        dialog_takeOfpick = new Dialog(PhongTroActivity.this);
        View view = LayoutInflater.from(PhongTroActivity.this).inflate(R.layout.takeorpick_picdialog, (ViewGroup) findViewById(R.id.relLayout_TakeOrPick), false);
        dialog_takeOfpick.setContentView(view);
        dialog_takeOfpick.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btn_dong = view.findViewById(R.id.btn_dong);
        ln_chupanh = view.findViewById(R.id.ln_chupanh);
        ln_chonanh = view.findViewById(R.id.ln_chonanh);
        ln_xemanh = view.findViewById(R.id.ln_xemanh);

        ln_xemanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupXemAnh(avtBitmap,2004);
            }
        });

        btn_dong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_takeOfpick.dismiss();
            }
        });

        btn_themAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkChon=0;
                ln_xemanh.setVisibility(View.GONE);
                dialog_takeOfpick.show();
            }
        });

        ln_chupanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkChon==0){
                    tempRequestCode=2000;
                    capquyenCAMERA(2000);
                }
                if (checkChon==1){
                    tempRequestCode=2002;
                    capquyenCAMERA(2002);
                }
            }
        });

        ln_chonanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkChon==0){
                    tempRequestCode=2001;
                    capquyenREAD_EXTERNAL(2001);
                }
                if (checkChon==1){
                    tempRequestCode=2003;
                    capquyenREAD_EXTERNAL(2003);
                }
            }
        });
    }

    public void setupGridViewImgPhong(){
        bitmapList=new ArrayList<>();
        anhPhongAdapter=new GridViewAnhPhongAdapter(this,R.layout.gridview_anhphong,bitmapList);
        gridView_anhPhong.setAdapter(anhPhongAdapter);
        setupChonImgInGrid();
    }

    public void setupXemAnh(Bitmap bitmap,int requestCode){
        try {
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byte [] byteArr=stream.toByteArray();
            FileOutputStream fileOutput =openFileOutput("img.txt",Context.MODE_PRIVATE);
            fileOutput.write(byteArr);
            fileOutput.close();
            Intent intent=new Intent(PhongTroActivity.this,PhotoFullScreen.class);
            startActivityForResult(intent,requestCode);
        }catch (Exception e){
            Log.d("Loi","Loi: "+e.getMessage());
        }
    }

    public void setupChonImgInGrid(){
        gridView_anhPhong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemSelect=position;
                setupXemAnh(bitmapList.get(position),1999);
            }
        });
        gridView_anhPhong.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                bitmapList.remove(position);
                anhPhongAdapter.notifyDataSetChanged();
                Toast.makeText(PhongTroActivity.this,"Đã Xóa",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1999){
            if (resultCode==RESULT_OK){
                byte [] byteArr=null;
                File file=new File(getFilesDir(),"img.txt");
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
                bitmapList.set(itemSelect,fixBitmap);
                anhPhongAdapter.notifyDataSetChanged();
            }
            if (resultCode==RESULT_XOA){
                bitmapList.remove(itemSelect);
                anhPhongAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode==2000){
            if (resultCode==RESULT_OK){
                fixBitmap=(Bitmap) data.getExtras().get("data");
                bitmapList.add(fixBitmap);
                anhPhongAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode==2001){
            if (resultCode==RESULT_OK){
                if (data.getClipData()!=null){
                    int count=data.getClipData().getItemCount();
                    for (int i=0;i<count;i++){
                        Uri imageUri=data.getClipData().getItemAt(i).getUri();
                        fixBitmap=BitmapFactory.decodeFile(getAbsolutePath(imageUri));
                        bitmapList.add(fixBitmap);
                    }
                    anhPhongAdapter.notifyDataSetChanged();
                }else if (data.getData()!=null){
                    fixBitmap=BitmapFactory.decodeFile(getAbsolutePath(data.getData()));
                    bitmapList.add(fixBitmap);
                    anhPhongAdapter.notifyDataSetChanged();
                }

            }
        }
        if (requestCode==2002){
            if (resultCode==RESULT_OK){
                avtBitmap=(Bitmap) data.getExtras().get("data");
                img_avtphong.setImageBitmap(avtBitmap);
                checkShowXemAnh=true;
                dialog_takeOfpick.dismiss();
            }
        }
        if (requestCode==2003){
            if (resultCode==RESULT_OK){
                if (data.getData()!=null) {
                    avtBitmap = BitmapFactory.decodeFile(getAbsolutePath(data.getData()));
                    img_avtphong.setImageBitmap(avtBitmap);
                    checkShowXemAnh=true;
                    dialog_takeOfpick.dismiss();
                }
            }
        }
        if (requestCode==2004){
            if (resultCode==RESULT_OK) {
                byte [] byteArr=null;
                File file=new File(getFilesDir(),"img.txt");
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
                avtBitmap=BitmapFactory.decodeByteArray(byteArr,0,byteArr.length);
                img_avtphong.setImageBitmap(avtBitmap);
                dialog_takeOfpick.dismiss();
            }
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
                Cursor imageCursor = getContentResolver().query(tempUri, imageColumns, MediaStore.Images.Media._ID + "=" + id, null, imageOrderBy);
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
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            } else
                return null;
        }

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
            if (checkSelfPermission(Manifest.permission.CAMERA)==
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
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_PERMISSION_CODE:{
                if (grantResults.length>0&&grantResults[0]==
                        PackageManager.PERMISSION_GRANTED){
                    setupChupAnh(tempRequestCode);
                }else {
                    Toast.makeText(PhongTroActivity.this,"Bạn chưa cấp quyền chụp ảnh!!",Toast.LENGTH_SHORT).show();
                }
            }break;

            case READ_EXTERNAL_PERMISSION_CODE:{
                if (grantResults.length>0&&grantResults[0]==
                        PackageManager.PERMISSION_GRANTED){
                    setupChonAnh(tempRequestCode);
                }else {
                    Toast.makeText(PhongTroActivity.this,"Bạn chưa cấp quyền truy cập file!!",Toast.LENGTH_SHORT).show();
                }
            }break;
        }
    }
}
