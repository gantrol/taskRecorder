package cn.com.wosuo.taskrecorder.ui.taskAssign;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.ui.adapter.ChooseUserAdapter;
import cn.com.wosuo.taskrecorder.viewmodel.UserViewModel;
import cn.com.wosuo.taskrecorder.vo.Task;
import cn.com.wosuo.taskrecorder.vo.User;

public class TaskAssignCompanyFragment extends Fragment{

    private static final String TAG = "选择被指派人";
    private static final String ARG_USER = "user";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.user_choose_list_view) RecyclerView mChooseListView;
    @BindView(R.id.toolbar_title) TextView mToolbarTitleTextView;
    private Unbinder unbinder;
    private Task mTask;
    ChooseUserAdapter adapter;

    public static TaskAssignCompanyFragment newInstance(Task task) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, task);
        TaskAssignCompanyFragment fragment = new TaskAssignCompanyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null){
            mTask = (Task) getArguments().getSerializable(ARG_USER);
        }

        View view = inflater.inflate(R.layout.fragment_user_choose, container, false);
        unbinder = ButterKnife.bind(this, view);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        mChooseListView.setLayoutManager(manager);
        mChooseListView.setOnClickListener(v -> { });
        if (mTask == null)
            adapter = new ChooseUserAdapter(getContext(), false);
        else
            adapter = new ChooseUserAdapter(getContext(), mTask.getTaskID(), false);
        mChooseListView.setAdapter(adapter);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)requireActivity()).getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayShowTitleEnabled(false);
        mToolbarTitleTextView.setText(TAG);

        UserViewModel userViewModel =
                ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, usersResource
                -> adapter.setUsers(usersResource.data));
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
