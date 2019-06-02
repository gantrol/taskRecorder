package cn.com.wosuo.taskrecorder.ui.taskloc;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.com.wosuo.taskrecorder.AppExecutors;
import cn.com.wosuo.taskrecorder.BasicApp;
import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.pref.AppPreferencesHelper;
import cn.com.wosuo.taskrecorder.util.FinalMap;
import cn.com.wosuo.taskrecorder.util.MyOrientationListener;
import cn.com.wosuo.taskrecorder.viewmodel.TaskViewModel;

public abstract class TaskLocFragment extends Fragment {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView mToolbarTitleTextView;
    @BindView(R.id.toolbar_OK_btn) Button mToolbarOkButton;
    @BindView(R.id.toolbar_cancel_btn) Button mToolbarCancelButton;
    @OnClick(R.id.toolbar_cancel_btn)
    void cancelOnClick() {
        requireActivity().setResult(Activity.RESULT_CANCELED);
        requireActivity().finish();
    }
    @BindView(R.id.baiduMapView) MapView mMapView;
    static final String ARG_Task_ID = "task_id";
    public LocationClient mLocationClient = null;
    MyLocationListener myLocationListener = new MyLocationListener();
    ArrayList<String> sCoorType = FinalMap.getCoorTypeList();
    AppExecutors mAppExecutors = new AppExecutors();
    int userId;
    int taskId;
    Unbinder unbinder;
    TaskViewModel viewModel;
    BaiduMap mBaiduMap;
    boolean isFirstCheck = true;
    boolean isFirstPoint = true;
    MyLocationData locData;
    double mCurrentLatitude;
    double mCurrentLongitude;
    String mCurrentCoorType = sCoorType.get(2);
    float mCurrentAccracy;
    int mXDirection = 0;
    float mCurrentZoom = 40f;
    private MyOrientationListener myOrientationListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        userId = AppPreferencesHelper.getCurrentUserID();
        taskId = getArguments().getInt(ARG_Task_ID);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(setLayout(), container, false);
        unbinder = ButterKnife.bind(this, v);
        mLocationClient = BasicApp.getLocationClient(getActivity());
        mLocationClient.registerLocationListener(myLocationListener);

        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        initOritationListener();

        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)requireActivity()).getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayShowTitleEnabled(false);
        mToolbarTitleTextView.setText(setTitle());
        mToolbarOkButton.setOnClickListener(v1 -> okOnClick());
        mBaiduMap.setOnMapStatusChangeListener(mapStatusListener);
        requestPermissions();
        return v;
    }



    BaiduMap.OnMapStatusChangeListener mapStatusListener =
            new BaiduMap.OnMapStatusChangeListener() {
                @Override
                public void onMapStatusChangeStart(MapStatus arg0) {
                }

                @Override
                public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
                }

                @Override
                public void onMapStatusChangeFinish(MapStatus arg0) {
                    mCurrentZoom = arg0.zoom;
                }
                @Override
                public void onMapStatusChange(MapStatus arg0) {
                }
            };

    abstract int setLayout();

    abstract void okOnClick();

    /**
     * set title of toolbar
     * @return title of toolbar
     */
    abstract String setTitle();



    void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    void initLocation(){
        LocationClientOption locationOption = new LocationClientOption();
        locationOption.setScanSpan(800);
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationOption.setIsNeedAddress(true);
        locationOption.setOpenAutoNotifyMode();
        locationOption.setOpenGps(true);
        locationOption.setIsNeedLocationDescribe(true);
        locationOption.setCoorType(mCurrentCoorType);
        mLocationClient.setLocOption(locationOption);
    }

    void onReceiveLocationListener(BDLocation location){
        LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.animateMapStatus(update);
        if (isFirstCheck) {
            update = MapStatusUpdateFactory.zoomTo(mCurrentZoom);
            mBaiduMap.animateMapStatus(update);
            isFirstCheck = false;
        }
        mCurrentLatitude = location.getLatitude();
        mCurrentLongitude = location.getLongitude();
        mCurrentAccracy = location.getRadius();
        MyLocationData.Builder mLocationBuilder = new MyLocationData.Builder();
        mLocationBuilder.latitude(mCurrentLatitude)
                .longitude(mCurrentLongitude)
                .direction(mXDirection);
        MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING, true, null);
        MyLocationData myLocationData = mLocationBuilder.build();
        mBaiduMap.setMyLocationConfiguration(myLocationConfiguration);
        mBaiduMap.setMyLocationData(myLocationData);
    }

    public void requestPermissions(){
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.
                permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.
                permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.
                permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.
                    size()]);
            ActivityCompat.requestPermissions(requireActivity(), permissions, 1);
        }
        else {
            requestLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(requireActivity(),
                                    "必须同意所有权限才能使用本程序",
                                    Toast.LENGTH_SHORT).show();
                            requireActivity().finish();
                            return;
                        }
                    }
                    requestLocation();
                }
                else {
                    Toast.makeText(requireActivity(),
                            "发生未知错误", Toast.LENGTH_SHORT).show();
                    requireActivity().finish();
                }
                break;
            default:
        }
    }


    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            onReceiveLocationListener(location);
        }

        public void onConnectHotSpotMessage(String s, int i) {
        }
    }

    /**
     * 初始化方向传感器
     */
    void initOritationListener() {
        myOrientationListener = new MyOrientationListener(requireActivity().getApplicationContext());
        myOrientationListener.setOnOrientationListener(x -> {
            mXDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    .direction(mXDirection)
                    .latitude(mCurrentLatitude)
                    .longitude(mCurrentLongitude).build();
            mBaiduMap.setMyLocationData(locData);
        });
    }

    void onChangeTaskStatusMessage(String message) {
//        mCreateButtom.setEnabled(true);
        if (getActivity() != null)
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        if (!mLocationClient.isStarted())
        {
            mLocationClient.start();
        }
        myOrientationListener.start();
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onStop() {
        // 取消注册传感器监听
        myOrientationListener.stop();
        super.onStop();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mLocationClient.unRegisterLocationListener(myLocationListener);
        mLocationClient.stop();
        mMapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
        unbinder.unbind();
    }
}
