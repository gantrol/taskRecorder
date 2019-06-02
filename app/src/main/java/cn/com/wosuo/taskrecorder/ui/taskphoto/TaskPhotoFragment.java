package cn.com.wosuo.taskrecorder.ui.taskphoto;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.com.wosuo.taskrecorder.AppExecutors;
import cn.com.wosuo.taskrecorder.BasicApp;
import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.ui.adapter.PhotoAdapter;
import cn.com.wosuo.taskrecorder.ui.sign.LoginActivity;
import cn.com.wosuo.taskrecorder.util.FinalMap;
import cn.com.wosuo.taskrecorder.util.Glide4Engine;
import cn.com.wosuo.taskrecorder.viewmodel.TaskViewModel;
import cn.com.wosuo.taskrecorder.vo.PhotoUpload;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.ResourceField.SOON;


public class TaskPhotoFragment extends Fragment {
    public static TaskPhotoFragment newInstance(int taskId) {
        Bundle args = new Bundle();
        args.putInt(ARG_Task_ID, taskId);
        TaskPhotoFragment fragment = new TaskPhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_OK_btn) Button mOkButton;
    @BindView(R.id.photo_recycler_view) RecyclerView mPhotoRecyclerView;
    @BindView(R.id.toolbar_title) TextView mToolbarTitleTextView;
    @BindView(R.id.add_photo_fab) FloatingActionButton mFloatingActionButton;

    static final String ARG_Task_ID = "task_id";
    private static final String TAG = "上传照片";
    MyLocationListener myLocationListener = new MyLocationListener();
    private AppExecutors mAppExecutors = new AppExecutors();
    private static final int REQUEST_IMAGE = 1;
    ArrayList<String> sCoorType = FinalMap.getCoorTypeList();
    String mCurrentCoorType = sCoorType.get(2);
    private int taskId;
    private Unbinder unbinder;
    private TaskViewModel viewModel;
    private PhotoAdapter mAdapter;
    private LocationClient mLocationClient;
    double mCurrentLatitude;
    double mCurrentLongitude;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskId = getArguments().getInt(ARG_Task_ID);
        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_photo, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)requireActivity()).getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayShowTitleEnabled(false);
        mToolbarTitleTextView.setText(TAG);
        mLocationClient = BasicApp.getLocationClient(getActivity());
        mLocationClient.registerLocationListener(myLocationListener);
        mPhotoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new PhotoAdapter(getContext(), new ArrayList<>(), taskId);
        mPhotoRecyclerView.setAdapter(mAdapter);
        requestPermissions();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            List<String> paths = Matisse.obtainPathResult(data);
            mAdapter.addPaths(paths);
        }
    }

    @OnClick(R.id.add_photo_fab)
    public void requestPermissions(){
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.
                permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.
                permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

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
            requestImage();
        }
    }

    @OnClick(R.id.toolbar_OK_btn)
    public void uploadPhotos(){

        progressDialog = new ProgressDialog(this.getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(SOON);
        progressDialog.show();

        mOkButton.setEnabled(false);
        List<PhotoUpload> photoUploads = mAdapter.retrieveData();
        if (photoUploads == null || photoUploads.isEmpty()){
            onPostPhotoMessage("请点击右下角添加图片");
            progressDialog.dismiss();
        } else {
            final int indexEnd = photoUploads.size() - 1;
            for (int i = 0; i < photoUploads.size(); i++) {
                PhotoUpload photoUpload = photoUploads.get(i);
                Call call = viewModel.uploadPhoto(photoUpload, mCurrentLatitude, mCurrentLongitude, taskId);
                if (call != null){
                    if (i == indexEnd) {
                        mAppExecutors.networkIO().execute( () ->
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call call, Response response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            mAppExecutors.mainThread().execute(()
                                                    -> onPostPhotoMessage("上传成功"));
                                            viewModel.resetPhotoListRateLimit(taskId);
                                            progressDialog.dismiss();
                                            Intent intent = new Intent();
                                            requireActivity().setResult(RESULT_OK, intent);
                                            requireActivity().finish();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call call, Throwable t) {
                                        mAppExecutors.mainThread().execute(()
                                                -> onPostPhotoMessage(t.getMessage()));
                                    }
                                })
                        );
                    } else {
                        mAppExecutors.networkIO().execute( () ->
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call call, Response response) {
                                    }

                                    @Override
                                    public void onFailure(Call call, Throwable t) {
                                    }
                                })
                        );
                    }
                } else {
                    onPostPhotoMessage("处理图片出错");
                }

            }
        }

    }

    private void onPostPhotoMessage(String message) {
        mOkButton.setEnabled(true);
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.toolbar_cancel_btn)
    public void photoCancel(){
        Intent intent = new Intent();
        requireActivity().setResult(Activity.RESULT_CANCELED, intent);
        requireActivity().finish();
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
                                    "同意后才能使用本功能",
                                    Toast.LENGTH_SHORT).show();
                            requireActivity().finish();
                            return;
                        }
                    }
                    requestImage();
                }
                else {
                    Toast.makeText(requireActivity(),
                            "已经禁用照相功能", Toast.LENGTH_SHORT).show();
                    requireActivity().finish();
                }
                break;
            default:
        }
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation(){
        LocationClientOption locationOption = new LocationClientOption();
        locationOption.setScanSpan(800);
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationOption.setIsNeedAddress(true);
        locationOption.setIsNeedLocationDescribe(true);
        locationOption.setCoorType(mCurrentCoorType);
        mLocationClient.setLocOption(locationOption);
    }

    private void requestImage() {
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG))
                .countable(true)
                .maxSelectable(9)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new Glide4Engine())
                .capture(true)
                .captureStrategy(new CaptureStrategy(true,
                        "cn.com.wosuo.taskrecorder.fileprovider", "场调记录"))
                .forResult(REQUEST_IMAGE);
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            mCurrentLatitude = location.getLatitude();
            mCurrentLongitude = location.getLongitude();
        }
    }

    @Override
    public void onStart() {
        if (!mLocationClient.isStarted())
        {
            mLocationClient.start();
        }
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }
}
