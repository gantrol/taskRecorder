package cn.com.wosuo.taskrecorder.ui.taskloc;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;

import java.io.IOException;

import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.util.FinalMap;
import cn.com.wosuo.taskrecorder.util.JsonParser;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TaskCenterPointFragment extends TaskLocFragment {

    private double mFinalLongitude;
    private double mFinalLatitude;

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
        mFinalLongitude = mCurrentLongitude;
        mFinalLatitude = mCurrentLatitude;
        viewModel.setTaskCenterPointCoordinate(
                taskId, mFinalLongitude, mFinalLatitude,
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
            int statusCode = JsonParser.parseChangeTaskStatusJson(responseBody).a;
            String message = FinalMap.getStatusCodeMap().get(statusCode);
            mAppExecutors.mainThread().execute(()
                    -> onChangeTaskStatusMessage(message));
            mAppExecutors.diskIO().execute(() ->
                    viewModel.setTaskCenterPointCoordinateLocally(taskId, mFinalLongitude,
                            mFinalLatitude, sCoorType.indexOf(mCurrentCoorType)));
            requireActivity().setResult(Activity.RESULT_OK);
            requireActivity().finish();
        }
    };

    @Override
    String setTitle() {
        return TAG;
    }
}
