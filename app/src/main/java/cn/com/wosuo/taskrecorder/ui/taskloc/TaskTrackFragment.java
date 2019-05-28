package cn.com.wosuo.taskrecorder.ui.taskloc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.util.DateUtil;
import cn.com.wosuo.taskrecorder.util.FinalMap;
import cn.com.wosuo.taskrecorder.util.JsonParser;
import cn.com.wosuo.taskrecorder.vo.TrackData;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static cn.com.wosuo.taskrecorder.util.JsonParser.generateTrackJson;

public class TaskTrackFragment extends TaskLocFragment {
    @BindView(R.id.stop_btn) Button finish;
    @OnClick(R.id.stop_btn)
    void stopOnClick(){
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();

            progressBarRl.setVisibility(View.GONE);

            if (isFirstPoint) {
                points.clear();
                last = new LatLng(0, 0);
                return;
            }

            MarkerOptions oFinish = new MarkerOptions();// 地图标记覆盖物参数配置类
            oFinish.position(points.get(points.size() - 1));
            oFinish.icon(finishBD);// 设置覆盖物图片
            mBaiduMap.addOverlay(oFinish); // 在地图上添加此图层

            //复位
            points.clear();
            last = new LatLng(0, 0);
            isFirstPoint = true;
        }
        isStart = false;
    }
    @BindView(R.id.write_btn) Button start;

    @OnClick(R.id.write_btn)
    void writeOnClick(){
        isStart = true;
        requestLocation();
    }

    @BindView(R.id.progressBarRl) RelativeLayout progressBarRl;
    @BindView(R.id.info) TextView info;
    private static final String TAG = "巡查路线";
    private BitmapDescriptor startBD = BitmapDescriptorFactory.fromResource(R.drawable.ic_me_history_startpoint);
    private BitmapDescriptor finishBD = BitmapDescriptorFactory.fromResource(R.drawable.ic_me_history_finishpoint);

    List<LatLng> points = new ArrayList<>();//位置点集合
    List<TrackData> sTrackData = new ArrayList<>();
    Polyline mPolyline;//运动轨迹图层
    LatLng last = new LatLng(0, 0);//上一个定位点
    MapStatus.Builder builder;
    private boolean isStart = false;

    public static TaskTrackFragment newInstance(int taskId) {
        Bundle args = new Bundle();
        args.putInt(ARG_Task_ID, taskId);
        TaskTrackFragment fragment = new TaskTrackFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    int setLayout() {
        return R.layout.fragment_task_track;
    }

    @Override
    void okOnClick() {
        if (sTrackData.isEmpty()){
            Toast.makeText(getContext(), "还没有数据", Toast.LENGTH_SHORT).show();
        } else {
            String trackData = generateTrackJson(sTrackData, sCoorType.indexOf(mCurrentCoorType));
            viewModel.addTaskTrack(trackData, taskId, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    mAppExecutors.mainThread().execute(()
                            -> onChangeTaskStatusMessage(FinalMap.statusCodeFail));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body().string();
                    int statusCode = JsonParser.parseCreateTaskJson(responseBody);
                    String message = FinalMap.getStatusCodeMap().get(statusCode);
                    mAppExecutors.mainThread().execute(()
                            -> onChangeTaskStatusMessage(message));
                    requireActivity().finish();
                }
            });
        }
    }

    void addTrackData(BDLocation location){
        TrackData newData =  new TrackData(
                location.getLongitude(), location.getLatitude(), userId,
                DateUtil.getUnixTimestamp());
        sTrackData.add(newData);
    }

    @Override
    void requestLocation(){
        initLocation();
        if (isStart) {
            progressBarRl.setVisibility(View.VISIBLE);
            info.setText("请稍后...");
            mBaiduMap.clear();
        }
    }

    @Override
    void onReceiveLocationListener(BDLocation location) {
        super.onReceiveLocationListener(location);
        if (!isStart) {
            return;
        }
        info.setText("请稍后...");
        if (isFirstPoint) {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            if(ll != null){
                isFirstPoint = false;
                points.add(ll);//加入集合
                addTrackData(location);
                last = ll;
                //显示当前定位点，缩放地图
                locateAndZoom(location, ll);
                //标记起点图层位置
                MarkerOptions oStart = new MarkerOptions();// 地图标记覆盖物参数配置类
                oStart.position(points.get(0));// 覆盖物位置点，第一个点为起点
                oStart.icon(startBD);// 设置覆盖物图片
                mBaiduMap.addOverlay(oStart); // 在地图上添加此图层
                progressBarRl.setVisibility(View.GONE);
            }
            return;//画轨迹最少得2个点，首地定位到这里就可以返回了
        }
        //从第二个点开始
        LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
        //sdk回调gps位置的频率是1秒1个，位置点太近动态画在图上不是很明显，可以设置点之间距离大于为2米才添加到集合中
        if (DistanceUtil.getDistance(last, ll) < 2) {
            return;
        }
        points.add(ll);//如果要运动完成后画整个轨迹，位置点都在这个集合中
        addTrackData(location);
        last = ll;
        //显示当前定位点，缩放地图
        locateAndZoom(location, ll);
        //清除上一次轨迹，避免重叠绘画
        mMapView.getMap().clear();
        //起始点图层也会被清除，重新绘画
        MarkerOptions oStart = new MarkerOptions();
        oStart.position(points.get(0));
        oStart.icon(startBD);
        mBaiduMap.addOverlay(oStart);
        //将points集合中的点绘制轨迹线条图层，显示在地图上
        OverlayOptions ooPolyline = new PolylineOptions().width(13).color(0xAAFF0000).points(points);
        mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
    }


    private void locateAndZoom(final BDLocation location, LatLng ll) {

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
//        locData = new MyLocationData.Builder().accuracy(0)
//                // 此处设置开发者获取到的方向信息，顺时针0-360
//                .direction(mXDirection).latitude(location.getLatitude())
//                .longitude(location.getLongitude()).build();
//        mBaiduMap.setMyLocationData(locData);
//
//        builder = new MapStatus.Builder();
//        builder.target(ll).zoom(mCurrentZoom);
//        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    @Override
    String setTitle() {
        return TAG;
    }

    @Override
    public void onDestroy() {
        startBD.recycle();
        finishBD.recycle();
        mMapView.getMap().clear();
        super.onDestroy();
    }
}
