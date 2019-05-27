package cn.com.wosuo.taskrecorder.ui.taskloc;

import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import androidx.annotation.NonNull;

import java.io.IOException;

import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.util.FinalMap;
import cn.com.wosuo.taskrecorder.util.JsonParser;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TaskCenterPointFragment extends TaskLocFragment {

    private static final String TAG = "获取定位";

    public static TaskCenterPointFragment newInstance(int taskId) {
        Bundle args = new Bundle();
        args.putInt(ARG_Task_ID, taskId);
        TaskCenterPointFragment fragment = new TaskCenterPointFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    int setLayout() {
        return R.layout.fragment_task_loc;
    }

    @Override
    void okOnClick() {
        viewModel.setTaskCenterPointCoordinate(
                taskId, mCurrentLongitude, mCurrentLatitude,
                sCoorType.indexOf(mCurrentCoorType),
                centerPointCallback);
    }

    private Callback centerPointCallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            mAppExecutors.mainThread().execute(()
                    -> onChangeTaskStatusMessage(FinalMap.statusCodeFail));
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            String responseBody = response.body().string();
            int statusCode = JsonParser.parseChangeTaskStatusJson(responseBody);
            String message = FinalMap.getStatusCodeMap().get(statusCode);
            mAppExecutors.mainThread().execute(()
                    -> onChangeTaskStatusMessage(message));
            requireActivity().finish();
        }
    };

    void onReceiveLocationListener(BDLocation location) {
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

    @Override
    String setTitle() {
        return TAG;
    }
}
