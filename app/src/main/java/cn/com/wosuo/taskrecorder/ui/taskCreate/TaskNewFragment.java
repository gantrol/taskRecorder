package cn.com.wosuo.taskrecorder.ui.taskCreate;

import android.app.Activity;
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
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.com.wosuo.taskrecorder.AppExecutors;
import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.ui.taskAssign.TaskAssignActivity;
import cn.com.wosuo.taskrecorder.util.FinalMap;
import cn.com.wosuo.taskrecorder.viewmodel.TaskViewModel;
import cn.com.wosuo.taskrecorder.vo.Task;
import cn.com.wosuo.taskrecorder.vo.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static cn.com.wosuo.taskrecorder.util.FinalStrings.USER_LIST;


public class TaskNewFragment extends Fragment {

    private static final String TAG = "新建任务";
    private static final String ARG_Assignee_ID = "task_id";
    private static final int REQUEST_ASSIGNEE = 0;
    private AppExecutors mAppExecutors = new AppExecutors();
    private User me;
    private Task mTask;
    private Unbinder unbinder;
    private int assigneeID = -1;
    private final static ArrayList<String> sTaskType = FinalMap.getTaskTypeList();
    private final static Map<Integer, String> statusCodeMap = FinalMap.getStatusCodeMap();
    private TaskViewModel viewModel;
    String title;
    String detail;
    int type;

    //    private final static ArrayList<String> sTaskStatus = FinalMap.getTaskStatusList();
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.toolbar_title) TextView mToolbarTitleTextView;

    @BindView(R.id.input_title) EditText mTitleEditText;
    @BindView(R.id.input_assignee) EditText mAssigneeEditText;
    @BindView(R.id.assigner_content) TextView mAssignerTextView;
    @BindView(R.id.assignee_content) TextView mAssigneeTextView;
    @BindView(R.id.task_type_spinner) Spinner mTypeSpinner;
    //    @BindView(R.id.task_status_spinner) Spinner mStatusSpinner;
    @BindView(R.id.input_detail) EditText mDetailEditText;
    @BindView(R.id.create_btn) Button mCreateButtom;
    @BindView(R.id.cancel_btn) Button mCancelButtom;
    @BindView(R.id.choose_assignee_btn) Button mChooseAssigneeButtom;

    public static TaskNewFragment newInstance() {
        return new TaskNewFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_task, container, false);
        unbinder = ButterKnife.bind(this, v);
        setHasOptionsMenu(true);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((AppCompatActivity)requireActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayShowTitleEnabled(false);
        mToolbarTitleTextView.setText(TAG);
        me = viewModel.getMe();
        mAssignerTextView.setText(me.getName());
        ArrayAdapter<String> typeArrayAdapter = new ArrayAdapter<>(requireActivity(),android.R.layout.simple_list_item_1, sTaskType);
        mTypeSpinner.setAdapter(typeArrayAdapter);
        return v;
    }

    @OnClick(R.id.create_btn)
    void createTask(){
        mCreateButtom.setEnabled(false);
        title = mTitleEditText.getText().toString();
        int assignee = assigneeID;
        if (!mAssigneeEditText.getText().toString().isEmpty())
            assignee = Integer.parseInt(mAssigneeEditText.getText().toString());

        type = mTypeSpinner.getSelectedItemPosition();
        detail = mDetailEditText.getText().toString();
        if (!validate(title, assignee, type, detail)){
            onCreateTaskMessage(FinalMap.statusCodeLost);
            return;
        }

        Call<ResponseBody> call = viewModel.createTask(title, assignee, type, detail);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int statusCode = response.code();
                String message = statusCodeMap.get(statusCode);
                mAppExecutors.mainThread().execute(()
                        -> onCreateTaskMessage(message)
                );
                viewModel.resetTaskListRateLimit(me.getUid());
                requireActivity().setResult(Activity.RESULT_OK, new Intent());
                requireActivity().finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                    mAppExecutors.mainThread().execute(()
                            -> onCreateTaskMessage(FinalMap.statusCodeFail));
            }
        });



//        TODO: 2创建并调用progressDialog
    }

    @OnClick(R.id.choose_assignee_btn)
    void chooseAssignee(){
        Intent intent = TaskAssignActivity.newIntent(getActivity(), null, false);
        startActivityForResult(intent, REQUEST_ASSIGNEE);
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
        Intent intent = new Intent();
        requireActivity().setResult(Activity.RESULT_CANCELED, intent);
        requireActivity().finish();
//        requireActivity().onBackPressed();
//        requireActivity().getFragmentManager().popBackStack();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ASSIGNEE){
            if (resultCode == Activity.RESULT_OK){
                ArrayList<User> aeeList = data.getParcelableArrayListExtra(USER_LIST);
                User assignee = aeeList.get(0);
                mAppExecutors.mainThread().execute(() -> {
                        mAssigneeEditText.setText(String.valueOf(assignee.getUid()));
                        mAssigneeTextView.setText(assignee.getName());
                });

            } else {
                mAppExecutors.mainThread().execute(() ->
                        Toast.makeText(getContext(), TAG + ":" + "操作取消",
                                Toast.LENGTH_SHORT).show());
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

