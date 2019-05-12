package cn.com.wosuo.taskrecorder.ui.taskphoto;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.recyclerview.widget.RecyclerView;

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
import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.util.Glide4Engine;
import cn.com.wosuo.taskrecorder.viewmodel.TaskViewModel;

import static android.app.Activity.RESULT_OK;


public class TaskPhotoFragment extends Fragment {
    public static TaskPhotoFragment newInstance(int taskId) {
        Bundle args = new Bundle();
        args.putInt(ARG_Task_ID, taskId);
        TaskPhotoFragment fragment = new TaskPhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.photo_recycler_view) RecyclerView mPhotoRecyclerView;
    @BindView(R.id.toolbar_title) TextView mToolbarTitleTextView;
    @BindView(R.id.add_photo_fab) FloatingActionButton mFloatingActionButton;

    static final String ARG_Task_ID = "task_id";
    private static final String TAG = "上传照片";
    private static final int REQUEST_IMAGE = 1;
    List<Uri> mSelected;
    private int taskId;
    private Unbinder unbinder;
    private TaskViewModel viewModel;

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
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
//            TODO: RecyclerView
//            https://stackoverflow.com/questions/37096547/how-to-get-data-from-edit-text-in-a-recyclerview
            Log.d("Matisse", "mSelected: " + mSelected);
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

        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.
                    size()]);
            ActivityCompat.requestPermissions(requireActivity(), permissions, 1);
        }
        else {
            requestImage();
        }
    }

    @OnClick(R.id.toolbar_OK_btn)
    public void uploadPhotos(){
        if (mSelected == null){
            Toast.makeText(getContext(), "请点击右下角添加图片", Toast.LENGTH_SHORT).show();
        } else {
//            TODO: upload with refroid2 from view model..
            Toast.makeText(getContext(), "Going on", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.toolbar_cancel_btn)
    public void photoCancel(){
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
    private void requestImage() {
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(9)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new Glide4Engine())
                .capture(true)
                .captureStrategy(new CaptureStrategy(true,
                        "cn.com.wosuo.taskrecorder.fileprovider", "test"))
                .forResult(REQUEST_IMAGE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
