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

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.IOException;
import java.util.ArrayList;
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
import cn.com.wosuo.taskrecorder.ui.taskphoto.TaskPhotoActivity;
import cn.com.wosuo.taskrecorder.util.DateUtil;
import cn.com.wosuo.taskrecorder.util.FinalMap;
import cn.com.wosuo.taskrecorder.util.JsonParser;
import cn.com.wosuo.taskrecorder.viewmodel.TaskViewModel;
import cn.com.wosuo.taskrecorder.vo.PhotoResult;
import cn.com.wosuo.taskrecorder.vo.Task;
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
    private static final String DIALOG_TASK_TYPE = "DialogType";
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
        mToolbarTitleTextView.setText("任务详情");
        mFloatingActionMenu.hideMenuButton(true);

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
            mFloatingActionMenu.hideMenuButton(false);
            fabRoute.setOnClickListener(clickListener);
            fabPhoto.setOnClickListener(clickListener);
            fabExplore.setOnClickListener(clickListener);
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
                .setNegativeButton("不修改了", (dialog, which) -> dialog.dismiss())
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
            }
        }
    };
}
