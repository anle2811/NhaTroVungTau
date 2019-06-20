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
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.anle.nhatrovungtau.CustomAdapter.SpnTPAdapter;
import com.example.anle.nhatrovungtau.DialogLoad;
import com.example.anle.nhatrovungtau.Models.ThanhPho;
import com.example.anle.nhatrovungtau.PhpDB.Api;
import com.example.anle.nhatrovungtau.PhpDB.PerformNetworkRequest;
import com.example.anle.nhatrovungtau.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KhuTroActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static String TENTK;

    private static final int REQUEST_CODE=113;

    public static DialogLoad themKTdialog;
    private Button btn_luulai;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleMap map;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION; //Quyền truy cập vị trí chính xác
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION; //Quyền truy cập vị trí gần đúng
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234; //Code để kiểm tra quyền được cấp trong phương thức onRequestPermissionsResult()
    private boolean LocationPemissionsGranted = false; //Dùng để kiểm tra quyền vị trí đã được cấp chưa
    private static final float DEFAULT_ZOOM=16f;
    private double latitude,longitude;
    private EditText edt_lat,edt_lng;
    private EditText edt_tenkhutro,edt_diachikhutro,edt_motakhutro;
    private ImageView img_avtkhu;
    private String THANHPHO;

    private Dialog dialog_takeOfpick;
    private LinearLayout ln_chupanh,ln_chonanh,ln_xemanh;
    private Button btn_dong;
    private Bitmap fixBitmap;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte [] byteArray;
    private String convertImage;

    private Spinner spn_thanhpho;
    private List<ThanhPho> thanhPhoList;
    private SpnTPAdapter spnTPAdapter;

    private Button btn_dinhvitro;
    private BottomSheetBehavior bottomSheetBehavior;
    private ImageView img_dongGGmap;
    private LinearLayout botsheet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khu_tro);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        themKTdialog=new DialogLoad(this,"Đang thêm khu trọ...");
        getTENTK();
        initSpinner();
        initEditText();
        PhucHoiTrangThai(savedInstanceState);
        initBtnDinhvi();
        initDongVaLuu();
        setDialog_takeOfpick();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Tenkhutro",edt_tenkhutro.getText().toString().trim());
        outState.putString("Diachi",edt_diachikhutro.getText().toString().trim());
        outState.putString("Thanhpho",THANHPHO);
        outState.putString("Mota",edt_motakhutro.getText().toString().trim());
        outState.putString("Lat",String.valueOf(latitude));
        outState.putString("Lng",String.valueOf(longitude));
    }

    public void PhucHoiTrangThai(Bundle savedInstanceState){
        if (savedInstanceState!=null){
            edt_tenkhutro.setText(savedInstanceState.getString("Tenkhutro"));
            edt_diachikhutro.setText(savedInstanceState.getString("Diachi"));
            phuchoiChonTP(savedInstanceState.getString("Thanhpho"));
            edt_motakhutro.setText(savedInstanceState.getString("Mota"));
            edt_lat.setText("LAT: "+savedInstanceState.getString("Lat"));
            edt_lng.setText("LNG: "+savedInstanceState.getString("Lng"));
            latitude=Double.parseDouble(savedInstanceState.getString("Lat"));
            longitude=Double.parseDouble(savedInstanceState.getString("Lng"));
        }
    }

    private void getTENTK(){
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (bundle!=null){
            TENTK=bundle.getString("TENTK");
        }else {
            TENTK="";
        }
    }

    public void setupChupAnh(){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,2000);
    }

    public void setupChonAnh(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, ""), 2001);
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
                img_avtkhu.setImageBitmap(fixBitmap);
            }
        }
        if (requestCode==2000){
            if (resultCode==RESULT_OK){
                fixBitmap=(Bitmap) data.getExtras().get("data");
                img_avtkhu.setImageBitmap(fixBitmap);
            }
        }
        if (requestCode==2001){
            if (resultCode==RESULT_OK){
                fixBitmap=BitmapFactory.decodeFile(getAbsolutePath(data.getData()));
                img_avtkhu.setImageBitmap(fixBitmap);
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

    public void initDongVaLuu(){
        btn_luulai=findViewById(R.id.btn_luulai);
        btn_luulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadAnh();
            }
        });
    }

    public void UploadAnh(){
        byteArrayOutputStream=new ByteArrayOutputStream();
        HashMap<String,String> params=new HashMap<>();
        Log.d("Upanh","UploadAnh");
        try {
            fixBitmap.compress(Bitmap.CompressFormat.JPEG,60,byteArrayOutputStream);
            byteArray=byteArrayOutputStream.toByteArray();
            convertImage=Base64.encodeToString(byteArray,Base64.DEFAULT);
        }catch (Exception e){
            Log.d("Upanh","Exception: "+e.getMessage());
            return;
        }
        Log.d("Upanh","convertImage: "+convertImage);

        params.put("Tentk",TENTK);
        params.put("Tentp",THANHPHO);
        params.put("Lat",String.valueOf(latitude));
        params.put("Lng",String.valueOf(longitude));
        params.put("Tenkhutro",edt_tenkhutro.getText().toString().trim());
        params.put("Diachi",edt_diachikhutro.getText().toString().trim());
        params.put("Mota",edt_motakhutro.getText().toString().trim());
        params.put("ImgData",convertImage);
        PerformNetworkRequest request=new PerformNetworkRequest(Api.URL_THEM_KHUTRO,Api.actionThemKhuTro,params,REQUEST_CODE,getApplicationContext(),this);
        request.execute();
    }

    public void setDialog_takeOfpick(){
        dialog_takeOfpick=new Dialog(KhuTroActivity.this);
        View view=LayoutInflater.from(KhuTroActivity.this).inflate(R.layout.takeorpick_picdialog,(ViewGroup)findViewById(R.id.relLayout_TakeOrPick),false);
        dialog_takeOfpick.setContentView(view);
        dialog_takeOfpick.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btn_dong=view.findViewById(R.id.btn_dong);
        ln_chupanh=view.findViewById(R.id.ln_chupanh);
        ln_chonanh=view.findViewById(R.id.ln_chonanh);
        ln_xemanh=view.findViewById(R.id.ln_xemanh);
        btn_dong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_takeOfpick.dismiss();
            }
        });

        img_avtkhu=findViewById(R.id.img_avtkhu);
        img_avtkhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_takeOfpick.show();
            }
        });

        ln_chupanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_takeOfpick.dismiss();
                setupChupAnh();
            }
        });
        ln_chonanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_takeOfpick.dismiss();
                setupChonAnh();
            }
        });
        ln_xemanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_takeOfpick.dismiss();
                try {
                    ByteArrayOutputStream stream=new ByteArrayOutputStream();
                    fixBitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    byte [] byteArr=stream.toByteArray();
                    FileOutputStream fileOutput =openFileOutput("img.txt",Context.MODE_PRIVATE);
                    fileOutput.write(byteArr);
                    fileOutput.close();
                    Intent intent=new Intent(KhuTroActivity.this,PhotoFullScreen.class);
                    startActivityForResult(intent,1999);
                }catch (Exception e){
                    Log.d("Loi","Loi: "+e.getMessage());
                }

            }
        });
    }

    public void phuchoiChonTP(String THANHPHO){
        if (THANHPHO!=null){
            for (int k=1;k<=thanhPhoList.size()-1;k++){
                if (THANHPHO.equals(thanhPhoList.get(k).getTentp())){
                    spn_thanhpho.setSelection(k);
                }
            }
        }else {
            spn_thanhpho.setSelection(0);
        }
    }

    public void initSpinner(){
        spn_thanhpho=findViewById(R.id.spn_thanhpho);

        thanhPhoList=new ArrayList<>();
        thanhPhoList.add(new ThanhPho("Chọn thành phố...",""));
        thanhPhoList.add(new ThanhPho("Hà Nội","Ha Noi"));
        thanhPhoList.add(new ThanhPho("Đà Nẵng","Da Nang"));
        thanhPhoList.add(new ThanhPho("Vũng Tàu","Vung Tau"));
        thanhPhoList.add(new ThanhPho("Sài Gòn","Sai Gon"));
        thanhPhoList.add(new ThanhPho("Hội An","Hoi An"));
        thanhPhoList.add(new ThanhPho("Hạ Long","Ha Long"));
        thanhPhoList.add(new ThanhPho("Hải Phòng","Hai Phong"));

        spnTPAdapter=new SpnTPAdapter(KhuTroActivity.this,R.layout.custom_spinner_tp,thanhPhoList);

        spn_thanhpho.setAdapter(spnTPAdapter);

        spn_thanhpho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    THANHPHO=null;
                }else {
                    THANHPHO=thanhPhoList.get(position).getTentp().trim();
                    Toast.makeText(KhuTroActivity.this,thanhPhoList.get(position).getTentp(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void initEditText(){
        edt_tenkhutro=findViewById(R.id.edt_tenkhutro);
        edt_diachikhutro=findViewById(R.id.edt_diachikhutro);
        edt_motakhutro=findViewById(R.id.edt_motakhutro);
        img_avtkhu=findViewById(R.id.img_avtkhu);
        edt_lat=findViewById(R.id.edt_lat);
        edt_lng=findViewById(R.id.edt_lng);
    }
    public void initBtnDinhvi(){
        btn_dinhvitro=findViewById(R.id.btn_latlng);
        img_dongGGmap=findViewById(R.id.img_dongGGmap);
        botsheet=findViewById(R.id.bot_sheet_location);

        View bottomSheet=findViewById(R.id.bot_sheet_location);
        bottomSheetBehavior=BottomSheetBehavior.from(bottomSheet);

        btn_dinhvitro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setPeekHeight(botsheet.getHeight());
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                getLocationPermission();
            }
        });

        img_dongGGmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setPeekHeight(0);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }

    public void initMap() { //Khỏi tạo bản đồ trên một View
        Log.d("MyLocationActivity", "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.ggmap);
        //SupportMapFragment dùng để đặt bản đồ vào ứng dụng, với một thẻ <fragment> có android:name="com.google.android.gms.maps.SupportMapFragment" và id=@id/map
        mapFragment.getMapAsync(this); //Khởi tạo hệ thống bản đồ và View trên mapFragment
    }

    public void getLocationPermission(){ //Kiểm tra các thứ về việc cấp quyền truy cập vị trí
        Log.d("MyLocationActivity","getLocationPermission: getting location permissions");
        String[] permissions={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}; //Một mảng 2 quyền cho phép truy cập vị trí chính xác và cho phép truy cập vị trí gần đúng
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){ //Kiểm tra đã được cấp phép Fine_Location(truy cập vị trí chính xác) chưa
            //PackageManager.PERMISSION_GRANTED: Đã được cấp phép, PackageManager class để truy xuất các loại thông tin khác nhau về các gói ứng dụng hiện đang được cài đặt trên thiết bị
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),COURSE_LOCATION)==PackageManager.PERMISSION_GRANTED){ //Kiểm tra đã được cấp phép COAR_LOCATION chưa
                LocationPemissionsGranted=true; //Quyền vị trí đã được cấp
                initMap();
            }else {
                ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE); //Yêu cầu cấp quyền cho ứng dụng nếu chưa cấp
            }
        }else {
            ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE); //Yêu cầu cấp quyền cho ứng dụng nếu chưa cấp
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { //Kết quả từ việc yêu cầu quyền, được gọi mỗi khi requestPermissions() được gọi
        Log.d("MyLocationActivity","onRequestPermissionsResult: called");
        LocationPemissionsGranted=false; //Quyền truy cập chưa được cấp
        switch (requestCode){ //Tương ứng với LOCATION_PERMISSION_REQUEST_CODE được đặt trong requestPermissions() khi yêu cầu quyền
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length>0){ //grantResults: Kết quả tương ứng với các quyền được cấp PERMISSION_GRANTED hoặc PERMISSION_DENIED. Ở đây length>0 là có kết quả trả về trong mảng int[] grantResults
                    for(int k=0;k<grantResults.length;k++){ // Kiểm tra kết quả trả về của việc cấp quyền
                        if(grantResults[k]!=PackageManager.PERMISSION_GRANTED){ // Nếu kết quả trả về của quyền trong mảng tại kết quả grantResults[0] khác với "quyền đã được cấp"
                            LocationPemissionsGranted=false; //Quyền chưa được cấp
                            Log.d("MyLocationActivity","onRequestPermissionsResult: permission failed");
                            return; //Thoát khỏi phương thức
                        }
                    }
                    //Không xảy ra if thì quyền được cấp
                    Log.d("MyLocationActivity","onRequestPermissionsResult: permission granted");
                    LocationPemissionsGranted=true; //Quyền được cấp
                    initMap(); // Bắt đầu khởi tạo bản đồ
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) { //GỌi Khi bản đồ được khỏi tạo đã sẳn sàng để sử dụng
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d("MyLocationActivity", "onMapReady: map is ready");
        map = googleMap;

        if (LocationPemissionsGranted) { //Quyền đã được cấp hết
            getDeviceLocation(); //Lấy vị trí hiện tại của thiết bị
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            map.setMyLocationEnabled(true); //Cho phép người dùng tương tác với vị trí của họ
        }
    }

    public void getDeviceLocation() { //Phương thức lấy vị trí hiện tại của thiết bị
        Log.d("MyLocationActivity", "getDeviceLocation: getting the devices current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this); //Dùng để tương tác với vị trí
        try {
            Task location = mFusedLocationProviderClient.getLastLocation(); //Lấy vị trí hiện tại được gán cho Task để kiểm tra có lấy thành công hay không
            location.addOnCompleteListener(new OnCompleteListener() {  //Xử lý khi tác vụ hoàn thành
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()&& task.getResult() != null) { //Nếu thành công
                        Log.d("MyLocationActivity", "onComplete: found location!");
                        Location currentLocation = (Location) task.getResult();  //Lấy kết quả sau khi tác vụ thành công, trả về vị trí hiện tại vào đối tượng Location
                        Log.d("MyLocationActivity", "onComplete: movecamera");
                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM); //Mức độ thu phóng càng lớn càng xem được nhiều chi tiết hơn
                        Log.d("MyLocationActivity", "onComplete: finish");
                    } else {
                        Log.d("MyLocationActivity", "onComplete: current location is null");
                    }
                }
            });
        } catch (SecurityException e) {
            Log.e("MyLocationActivity", "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    public void moveCamera(LatLng latLng, float zoom) {
        latitude=latLng.latitude;
        longitude=latLng.longitude;
        edt_lat.setText("LAT: "+latitude);
        edt_lng.setText("LNG: "+longitude);
        Log.d("MyLocationActivity", "movaCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom)); //Di chuyển tầm nhìn tới địa điểm vị trí hiện tại

    }
}
