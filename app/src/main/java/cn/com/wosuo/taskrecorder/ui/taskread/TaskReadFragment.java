package cn.com.wosuo.taskrecorder.ui.taskread;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import cn.com.wosuo.taskrecorder.AppExecutors;
import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.api.HttpUtil;
import cn.com.wosuo.taskrecorder.pref.AppPreferencesHelper;
import cn.com.wosuo.taskrecorder.ui.adapter.PhotoReadAdapter;
import cn.com.wosuo.taskrecorder.ui.taskloc.TaskCenterPointActivity;
import cn.com.wosuo.taskrecorder.ui.taskloc.TaskTrackActivity;
import cn.com.wosuo.taskrecorder.ui.taskloc.TextureSupportMapFragment;
import cn.com.wosuo.taskrecorder.ui.taskphoto.TaskPhotoActivity;
import cn.com.wosuo.taskrecorder.util.CoordinateTypeUtil;
import cn.com.wosuo.taskrecorder.util.DateUtil;
import cn.com.wosuo.taskrecorder.util.FinalMap;
import cn.com.wosuo.taskrecorder.util.JsonParser;
import cn.com.wosuo.taskrecorder.util.LocPointComparator;
import cn.com.wosuo.taskrecorder.viewmodel.TaskViewModel;
import cn.com.wosuo.taskrecorder.vo.LocCenterPoint;
import cn.com.wosuo.taskrecorder.vo.Task;
import cn.com.wosuo.taskrecorder.vo.Track;
import cn.com.wosuo.taskrecorder.vo.TrackData;
import cn.com.wosuo.taskrecorder.vo.Tracks;
import cn.com.wosuo.taskrecorder.vo.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static cn.com.wosuo.taskrecorder.api.Urls.COMPANY_GET_EXECUTOR;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.GROUP_GROUP;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.MANAGER_GROUP;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TASK_CREATE;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TASK_DONE;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TASK_PROGRESS;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TASK_TEST;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.USER_GROUP;


public class TaskReadFragment extends Fragment {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    private String TAG = "查阅任务";

    @OnTextChanged(R.id.task_title)
    void onTextChanged(CharSequence text){
        mTask.setTitle(text.toString());
    }
    @BindView(R.id.photo_read_recycler_view)
    RecyclerView mPhotoReadRecyclerView;
    @BindView(R.id.task_id) TextView mIdTextView;
    @BindView(R.id.task_title) TextView mTitleTextView;
    @BindView(R.id.task_create) TextView mCreateDateTextView;
    @BindView(R.id.task_update) TextView mUpdateDateTextView;
    @BindView(R.id.task_finish) TextView mFinishDateTextView;
    @BindView(R.id.task_assignee) TextView mAssigneeTextView;
    @BindView(R.id.task_assigner) TextView mAssignerTextView;
    @BindView(R.id.task_executor) TextView mExecutorTextView;
    @BindView(R.id.executor_layout) LinearLayout mExecutorLinearLayout;
    @BindView(R.id.task_type) TextView mTypeTextView;
    @BindView(R.id.task_status) TextView mStatusTextView;
    @BindView(R.id.task_detail_text) TextView mDetailTextView;
    @BindView(R.id.toolbar_title) TextView mToolbarTitleTextView;
    @BindView(R.id.fab_menu) FloatingActionMenu mFloatingActionMenu;
    @BindView(R.id.fab_explore) FloatingActionButton fabExplore;
    @BindView(R.id.fab_photo) FloatingActionButton fabPhoto;
    @BindView(R.id.fab_route) FloatingActionButton fabRoute;
    static final String ARG_Task_ID = "task_id";
    private List<String> sUserType = FinalMap.getUserTypeList();
    private CharSequence[] sTaskStatus = FinalMap.getTaskStatusList();
    private ArrayList<String> sCanChooseTaskStatus = new ArrayList<>();
    private final static Map<Integer, String> statusCodeMap = FinalMap.getStatusCodeMap();
    BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
    BitmapDescriptor stbd = BitmapDescriptorFactory.fromResource(R.drawable.ic_me_history_startpoint);
    BitmapDescriptor enbd = BitmapDescriptorFactory.fromResource(R.drawable.ic_me_history_finishpoint);
    List<BitmapDescriptor> textureList = FinalMap.getTextureList();
    private static final String DIALOG_TASK_TYPE = "DialogType";
    float mCurrentZoom = 19f;
    private AppExecutors mAppExecutors = new AppExecutors();
    private int taskId;
    private int userType;
    private Task mTask;
    private Unbinder unbinder;
    private TaskViewModel viewModel;

    public static TaskReadFragment newInstance(int taskId) {
        Bundle args = new Bundle();
        args.putInt(ARG_Task_ID, taskId);
        TaskReadFragment fragment = new TaskReadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        userType = AppPreferencesHelper.getCurrentUserLoginState();
        super.onCreate(savedInstanceState);
        taskId = getArguments() != null ? getArguments().getInt(ARG_Task_ID) : 0;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO: 4 左边缘向右拉退出
        View v = inflater.inflate(R.layout.fragment_task, container, false);
        unbinder = ButterKnife.bind(this, v);
        setHasOptionsMenu(true);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((AppCompatActivity)requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbarTitleTextView.setText(TAG);
//        TODO: 分用户显示页面？
        mPhotoReadRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final PhotoReadAdapter adapter = new PhotoReadAdapter();
        mPhotoReadRecyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        viewModel.getTasksById(taskId).observe(this, taskResource -> {
            mTask = taskResource;
            if (mTask != null) {
                TaskReadFragment.this.updateUI(mTask);
                if (mTask.getAssignee_id() > 0) {
                    viewModel.getUser(mTask.getAssignee_id()).observe(this, aeeResource -> {
                        User mAssignee = aeeResource.data;
                        if (mAssignee != null) mAssigneeTextView.setText(mAssignee.getName());
                    });
//                    有assignee的时候，才有结果
//                    1.照片
                    viewModel.getPhotoResultsByTaskID(taskId).observe(this, photoResource -> {
                        if (photoResource != null)
                            adapter.submitList(photoResource.data);
                    });
                }
                if (mTask.getAssigner_id() > 0) {
                    viewModel.getUser(mTask.getAssigner_id()).observe(this, aerResource -> {
                        User mAssigner = aerResource.data;
                        if (mAssigner != null) mAssignerTextView.setText(mAssigner.getName());
                    });
                }
            }
        });

        int myType = AppPreferencesHelper.getCurrentUserLoginState();
        List<String> sUserType= FinalMap.getUserTypeList();
        if(myType == sUserType.indexOf(USER_GROUP)){
//            mFloatingActionMenu.hideMenuButton(false);
            fabRoute.setOnClickListener(clickListener);
            fabPhoto.setOnClickListener(clickListener);
            fabExplore.setOnClickListener(clickListener);
        }
        else {
            fabRoute.setVisibility(View.GONE);
            fabPhoto.setVisibility(View.GONE);
            fabExplore.setVisibility(View.GONE);
        }
        if (myType == sUserType.indexOf(GROUP_GROUP)){
            HttpUtil.GET(COMPANY_GET_EXECUTOR + taskId, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String responseBody = response.body() != null ? response.body().string() : null;
                    List<User> users = JsonParser.parseGetExecutorJson(responseBody);
                    StringBuilder usersSb = new StringBuilder();
                    for (User user : users) {
                        usersSb.append(user.getName());
                        usersSb.append(" ");
                    }

                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.postDelayed(()
                            -> Objects.requireNonNull(getActivity()).runOnUiThread(()
                            -> mExecutorTextView.setText(usersSb)), 100);
                }
            });
        }
        else {
            mExecutorLinearLayout.setVisibility(View.GONE);
        }

//        LatLng GEO_BEIJING = new LatLng(39.945, 116.404);
//        TODO:无数据状态


        viewModel.getLocCenterPointByTaskID(taskId).observe(this, centerPointResource -> {
            LocCenterPoint locCenterPoint = centerPointResource.data;
//            TODO: 坐标轴转化z
            if (locCenterPoint != null){
                LatLng GEO_CENTER = CoordinateTypeUtil.toBaidull(locCenterPoint);
                MapStatusUpdate status2 = MapStatusUpdateFactory.newLatLng(GEO_CENTER);
                TextureSupportMapFragment map2 = (TextureSupportMapFragment) (getChildFragmentManager()
                        .findFragmentById(R.id.local_map_fragment));
                map2.getBaiduMap().setMapStatus(status2);
                status2 = MapStatusUpdateFactory.zoomTo(mCurrentZoom);
                map2.getBaiduMap().animateMapStatus(status2);
                OverlayOptions option = new MarkerOptions()
                        .position(GEO_CENTER)
                        .icon(bd);
                //在地图上添加Marker，并显示
                map2.getBaiduMap().addOverlay(option);
            }
        });

        viewModel.getTracksByTaskID(taskId).observe(this, trackResource -> {
            List<Tracks> tracksList = trackResource.data;
            if (tracksList != null && !tracksList.isEmpty()){
                int index = 0;
                for (Tracks tracks: tracksList){
                    Track track = tracks.getTrack();
                    if (track != null){
                        index++;
//                        TODO:贴标签？
//                        String lineTagString = "巡查路线" + index;
                        int coordinate = track.getCoordinate();
                        List<LatLng> points = new ArrayList<>();
                        //添加纹理索引
                        List<Integer> indexList = new ArrayList<>();

                        List<TrackData> trackDataList = track.getData();
                        Collections.sort(trackDataList, new LocPointComparator());

                        //  第一个点, 确定大致定位, 选定大概缩放区域
                        LatLng GEO_TRACK = CoordinateTypeUtil.toBaidull(trackDataList.get(0), coordinate);
                        MapStatusUpdate trackMapUpadate = MapStatusUpdateFactory.newLatLng(GEO_TRACK);
                        TextureSupportMapFragment TrackMap = (TextureSupportMapFragment) (getChildFragmentManager()
                                .findFragmentById(R.id.track_map_fragment));
                        BaiduMap baiduMap = TrackMap.getBaiduMap();
                        baiduMap.setMapStatus(trackMapUpadate);
                        trackMapUpadate = MapStatusUpdateFactory.zoomTo(mCurrentZoom);
                        baiduMap.animateMapStatus(trackMapUpadate);


                        if (trackDataList.size() >= 2){
                            Bundle bundle = new Bundle();
                            bundle.putInt("index", index);
                            //在地图上添加起始点的Marker，并显示
                            OverlayOptions option = new MarkerOptions()
                                    .position(GEO_TRACK)
                                    .icon(stbd);
                            baiduMap.addOverlay(option);
                            for (int i = 0; i < trackDataList.size(); i++){
                                TrackData trackData = trackDataList.get(i);
                                GEO_TRACK = CoordinateTypeUtil.toBaidull(trackData, coordinate);
                                points.add(GEO_TRACK);
                                trackMapUpadate = MapStatusUpdateFactory.newLatLng(GEO_TRACK);
                                baiduMap.setMapStatus(trackMapUpadate);
                                indexList.add(index % textureList.size() - 1);
                            }
                            //在地图上添加终止点的Marker，并显示
                            option = new MarkerOptions()
                                    .position(GEO_TRACK)
                                    .icon(enbd);
                            baiduMap.addOverlay(option);

                            //构建折线点坐标
                            // 添加纹理图片
                            //设置折线的属性
                            OverlayOptions mOverlayOptions = new PolylineOptions()
                                    .width(15)
                                    .extraInfo(bundle)
                                    .dottedLine(true)
                                    .points(points)
                                    .customTextureList(textureList)
                                    .textureIndex(indexList);//设置纹理列表
                            // 在地图上绘制折线
                            // mPloyline 折线对象
                            Overlay mPolyline = baiduMap.addOverlay(mOverlayOptions);
//                        TODO: 增加删除的监听器？
                        }
                        points.clear();
                        indexList.clear();
                    }
                }
            }
        });

        return v;
    }

    private void updateUI(Task task) {
        mIdTextView.setText(String.valueOf(task.getTaskID()));
        mTitleTextView.setText(task.getTitle());
        mCreateDateTextView.setText(DateUtil.unixTimestampToFullDateString(task.getCreateAt()));
        mUpdateDateTextView.setText(DateUtil.unixTimestampToFullDateString(task.getUpdateAt()));
        mFinishDateTextView.setText(DateUtil.unixTimestampToFullDateString(task.getFinishAt()));
        mTypeTextView.setText(FinalMap.getTaskTypeList().get(task.getType()));
        mStatusTextView.setText(FinalMap.getTaskStatusList()[task.getStatus()]);
        mDetailTextView.setText(task.getDescription());
        mToolbarTitleTextView.setText(task.getTitle());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //switch (tasktype){
        //0:指派者可转1
        //1:指派者可转0、2
        //2:指派者可转1，创建者可转3
        //3:创建者可转2
        //}
        // Inflate the menu; this adds items to the action bar if it is present.
        sCanChooseTaskStatus.clear();  //String[] sTaskStatus = {TASK_CREATE, TASK_PROGRESS, TASK_TEST, TASK_DONE};
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_status);
        int type = (mTask == null) ? 0 : mTask.getType();
        if (userType == sUserType.indexOf(GROUP_GROUP)){
            switch (type){
                case 0:
                case 2:
                    sCanChooseTaskStatus.add(TASK_PROGRESS);
                    break;
                case 1:
                    sCanChooseTaskStatus.add(TASK_CREATE);
                    sCanChooseTaskStatus.add(TASK_TEST);
                    break;
                default:
                    item.setVisible(false);
                    break;
            }
        }
        else if (userType == sUserType.indexOf(MANAGER_GROUP)){
            switch (type){
                case 2:
                    sCanChooseTaskStatus.add(TASK_DONE);
                    break;
                case 3:
                    sCanChooseTaskStatus.add(TASK_TEST);
                    break;
                default:
                    item.setVisible(false);
                    break;
            }
        }
        else {
            item.setVisible(false);
        }
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_edited:
                startEditActivity();
                return true;
            case R.id.action_status:
                createStatusDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createStatusDialog() {
        int item = (mTask == null) ? 0 : mTask.getType();
        new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.change_task_type)
                .setSingleChoiceItems(
                        sCanChooseTaskStatus.toArray(new CharSequence[sCanChooseTaskStatus.size()]),
                        item, this::patchTaskStaus)
//                TODO: .setPositiveButton("确认", )
                .setNegativeButton("不修改", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void patchTaskStaus(DialogInterface dialog, int which) {
//        Progress dialog?
        TaskReadFragment.this.viewModel.setTaskStatus(taskId, which, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                mAppExecutors.mainThread().execute(()
                        -> {
                    dialog.dismiss();
                    onChangeTaskStatusMessage(FinalMap.statusCodeFail);
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response)
                    throws IOException {
                String responseData = response.body() != null ? response.body().string() : null;
                int statusCode = JsonParser.
                        parseChangeTaskStatusJson(responseData);
                String message = statusCodeMap.get(statusCode);
                mAppExecutors.mainThread().execute(()
                        -> {
                    dialog.dismiss();
                    onChangeTaskStatusMessage(message);
                });
            }
        });
    }

    private void startEditActivity() {

//        TODO: show?

    }

    private void onChangeTaskStatusMessage(String message) {
//        mCreateButtom.setEnabled(true);
        if (getActivity() != null)
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab_photo:
                    Intent photoIntent = TaskPhotoActivity.newIntent(requireContext(), taskId);
                    startActivity(photoIntent);
                    break;
                case R.id.fab_explore:
                    Intent exploreIntent = TaskCenterPointActivity.newIntent(requireContext(), taskId);
                    startActivity(exploreIntent);
                    break;
                case R.id.fab_route:
                    Intent routeIntent = TaskTrackActivity.newIntent(requireContext(), taskId);
                    startActivity(routeIntent);
                    break;
                case R.id.fab_edited:
                    startEditActivity();
                    break;
                case R.id.fab_status:
                    createStatusDialog();
                    break;
            }
        }
    };

}
