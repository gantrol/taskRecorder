package cn.com.wosuo.taskrecorder.ui.taskAssign;

import android.app.Activity;
import android.content.Intent;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.ui.adapter.ChooseUserAdapter;
import cn.com.wosuo.taskrecorder.viewmodel.UserViewModel;
import cn.com.wosuo.taskrecorder.vo.Task;
import cn.com.wosuo.taskrecorder.vo.User;

import static cn.com.wosuo.taskrecorder.util.FinalStrings.USER_LIST;

public class TaskAssignCompanyFragment extends Fragment{

    private static final String TAG = "选择被指派人";
    private static final String ARG_TASK = "TASK";
    private static final String ARG_IS_MUTI_CHOICE = "muti";
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.user_choose_list_view) RecyclerView mChooseListView;
    @BindView(R.id.toolbar_title) TextView mToolbarTitleTextView;
    @BindView(R.id.toolbar_OK_btn) Button mOkButton;
    private Unbinder unbinder;
    private Task mTask;
    private boolean isMutiSelect;
    ChooseUserAdapter adapter;

    public static TaskAssignCompanyFragment newInstance(Task task, boolean isMutiSelect) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK, task);
        args.putBoolean(ARG_IS_MUTI_CHOICE, isMutiSelect);
        TaskAssignCompanyFragment fragment = new TaskAssignCompanyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null){
            isMutiSelect = getArguments().getBoolean(ARG_IS_MUTI_CHOICE);
            mTask = (Task) getArguments().getSerializable(ARG_TASK);
        }

        View view = inflater.inflate(R.layout.fragment_user_choose, container, false);
        unbinder = ButterKnife.bind(this, view);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        mChooseListView.setLayoutManager(manager);
        mChooseListView.setOnClickListener(v -> { });
        if (mTask == null)
            adapter = new ChooseUserAdapter(getContext(), isMutiSelect);
        else
            adapter = new ChooseUserAdapter(getContext(), mTask.getTaskID(), isMutiSelect);

        mChooseListView.setAdapter(adapter);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)requireActivity()).getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayShowTitleEnabled(false);
        mToolbarTitleTextView.setText(TAG);
        mOkButton.setText("确认");

        UserViewModel userViewModel =
                ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getUsersInCompany().observe(this, usersResource
                -> adapter.setUsers(usersResource.data));
        return view;
    }

    @OnClick(R.id.toolbar_OK_btn)
    void setResult() {
        Intent intent = new Intent();
        ArrayList<User> users = adapter.getSelectedUser();
        if (users == null || users.isEmpty()){
            Toast.makeText(getContext(), "请选择用户", Toast.LENGTH_SHORT).show();
            return;
        }
        intent.putParcelableArrayListExtra(USER_LIST, users);
        requireActivity().setResult(Activity.RESULT_OK, intent);
        requireActivity().finish();
    }

    @OnClick(R.id.toolbar_cancel_btn)
    void cancel() {
        Intent intent = new Intent();
        requireActivity().setResult(Activity.RESULT_CANCELED, intent);
        requireActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
