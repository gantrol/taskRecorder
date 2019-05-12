package cn.com.wosuo.taskrecorder.ui.taskCreate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.com.wosuo.taskrecorder.AppExecutors;
import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.api.HttpUtil;
import cn.com.wosuo.taskrecorder.ui.taskAssign.TaskAssignActivity;
import cn.com.wosuo.taskrecorder.ui.taskAssign.TaskAssignCompanyFragment;
import cn.com.wosuo.taskrecorder.util.FinalMap;
import cn.com.wosuo.taskrecorder.util.JsonParser;
import cn.com.wosuo.taskrecorder.util.Resource;
import cn.com.wosuo.taskrecorder.viewmodel.UserViewModel;
import cn.com.wosuo.taskrecorder.vo.Task;
import cn.com.wosuo.taskrecorder.vo.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static cn.com.wosuo.taskrecorder.api.Urls.GET_OR_CREATE_TASKS;
import static cn.com.wosuo.taskrecorder.api.Urls.TASK_DESCRIPTION;
import static cn.com.wosuo.taskrecorder.api.Urls.TASK_TYPE;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TASK_ASSIGNEE;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TASK_TITLE;


public class TaskNewFragment extends Fragment {

    private static final String ARG_Assignee_ID = "task_id";
    private AppExecutors mAppExecutors = new AppExecutors();
    private Task mTask;
    private Unbinder unbinder;
    private int assigneeID = -1;
    private final static ArrayList<String> sTaskType = FinalMap.getTaskTypeList();
    private final static Map<Integer, String> statusCodeMap = FinalMap.getStatusCodeMap();
//    private final static ArrayList<String> sTaskStatus = FinalMap.getTaskStatusList();
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.toolbar_title) TextView mToolbarTitleTextView;

    @BindView(R.id.input_title) EditText mTitleEditText;
    @BindView(R.id.input_assignee) EditText mAssigneeEditText;
    @BindView(R.id.assigner_content) TextView mAssignerTextView;
    @BindView(R.id.task_type_spinner) Spinner mTypeSpinner;
//    @BindView(R.id.task_status_spinner) Spinner mStatusSpinner;
    @BindView(R.id.input_detail) EditText mDetailEditText;
    @BindView(R.id.create_btn) Button mCreateButtom;
    @BindView(R.id.cancel_btn) Button mCancelButtom;
    @BindView(R.id.choose_assignee_btn) Button mChooseAssigneeButtom;

    public static TaskNewFragment newInstance() {
        TaskNewFragment fragment = new TaskNewFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//        TODO:viewmodel
//         .https://stackoverflow.com/questions/12103953/how-to-pass-result-from-second-fragment-to-first-fragment
//         .不要手动输入ID……；
//         .UI dimens
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_task, container, false);
        unbinder = ButterKnife.bind(this, v);
        setHasOptionsMenu(true);
        ((AppCompatActivity)requireActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = ((AppCompatActivity)requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbarTitleTextView.setText("新建任务");
        ArrayAdapter<String> typeArrayAdapter = new ArrayAdapter<>(requireActivity(),android.R.layout.simple_list_item_1, sTaskType);
        mTypeSpinner.setAdapter(typeArrayAdapter);
//        https://stackoverflow.com/questions/12103953/how-to-pass-result-from-second-fragment-to-first-fragment
//        https://github.com/bignerdranch/recyclerview-multiselect
        return v;
    }

    @OnClick(R.id.create_btn)
    void createTask(){
        mCreateButtom.setEnabled(false);
        final String title = mTitleEditText.getText().toString();
        int assignee = assigneeID;
        if (!mAssigneeEditText.getText().toString().isEmpty())
            assignee = Integer.parseInt(mAssigneeEditText.getText().toString());

        final int type = mTypeSpinner.getSelectedItemPosition();
        final String detail = mDetailEditText.getText().toString();
        if (!validate(title, assignee, type, detail)){
            onCreateTaskMessage(FinalMap.statusCodeLost);
            return;
        }

//        TODO: 2创建并调用progressDialog
        RequestBody requestBody = new FormBody.Builder()
                .add(TASK_TITLE, title)
                .add(TASK_ASSIGNEE, Integer.toString(assigneeID))
                .add(TASK_TYPE, Integer.toString(assigneeID))
                .add(TASK_DESCRIPTION, detail)
                .build();

        HttpUtil.POST(GET_OR_CREATE_TASKS, requestBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                mAppExecutors.mainThread().execute(()
                        -> onCreateTaskMessage(FinalMap.statusCodeFail)
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String reponseData = response.body().string();
                int statusCode = JsonParser.parseCreateTaskJson(reponseData);
                String message = statusCodeMap.get(statusCode);
                mAppExecutors.mainThread().execute(()
                        -> onCreateTaskMessage(message)
                );
//                TODO: 1插入数据库
                requireActivity().finish();
            }
        });
    }

    @OnClick(R.id.choose_assignee_btn)
    void chooseAssignee(){
        Intent intent = TaskAssignActivity.newIntent(getActivity(), mTask);
        startActivity(intent);
    }

    private void onCreateTaskMessage(String message) {
        mCreateButtom.setEnabled(true);
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    private boolean validate(String title, int assignee, int type, String detail) {
        boolean valid = true;
        if (title.isEmpty() || title.length() < 3){
            mTitleEditText.setError(requireActivity().getResources().getString(R.string.valid_string_long));
            valid = false;
        }
        if (assignee < 0){
            mAssigneeEditText.setError(requireActivity().getResources().getString(R.string.valid_select_user));
            valid = false;
        }
        if (detail.isEmpty() || detail.length() < 3){
            mDetailEditText.setError(requireActivity().getResources().getString(R.string.valid_string_long));
            valid = false;
        }
        return valid;

    }

    @OnClick(R.id.cancel_btn)
    void cancel(){
        //TODO:2 alert window to check again for it
        requireActivity().onBackPressed();
//        requireActivity().getFragmentManager().popBackStack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

