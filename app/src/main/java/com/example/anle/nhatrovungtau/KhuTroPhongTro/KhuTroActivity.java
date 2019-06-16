package com.example.anle.nhatrovungtau.KhuTroPhongTro;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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

public class KhuTroActivity extends AppCompatActivity implements OnMapReadyCallback {

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleMap map;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION; //Quyền truy cập vị trí chính xác
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION; //Quyền truy cập vị trí gần đúng
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234; //Code để kiểm tra quyền được cấp trong phương thức onRequestPermissionsResult()
    private boolean LocationPemissionsGranted = false; //Dùng để kiểm tra quyền vị trí đã được cấp chưa
    private static final float DEFAULT_ZOOM=16f;
    private double latitude,longitude;
    private EditText edt_lat,edt_lng;

    private Button btn_dinhvitro;
    private BottomSheetBehavior bottomSheetBehavior;
    private ImageView img_dongGGmap;
    private LinearLayout botsheet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khu_tro);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initEditText();
        initBtnDinhvi();
    }
    public void initEditText(){
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
