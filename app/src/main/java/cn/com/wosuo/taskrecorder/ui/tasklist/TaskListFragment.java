package cn.com.wosuo.taskrecorder.ui.tasklist;

import android.content.Intent;
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
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.pref.AppPreferencesHelper;
import cn.com.wosuo.taskrecorder.ui.adapter.TaskAdapter;
import cn.com.wosuo.taskrecorder.ui.taskCreate.TaskNewActivity;
import cn.com.wosuo.taskrecorder.ui.taskread.TaskReadActivity;
import cn.com.wosuo.taskrecorder.util.FinalMap;
import cn.com.wosuo.taskrecorder.viewmodel.TaskViewModel;
import cn.com.wosuo.taskrecorder.vo.Task;

import static cn.com.wosuo.taskrecorder.util.FinalStrings.UserField.MANAGER_GROUP;

public class TaskListFragment extends Fragment {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.task_recycler_view)RecyclerView mTaskRecyclerView;
    @BindView(R.id.toolbar_title) TextView mToolbarTitleTextView;
    @BindView(R.id.fab) FloatingActionButton mFloatingActionButton;
    private Unbinder unbinder;
    TaskViewModel taskViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)requireActivity()).getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayShowTitleEnabled(false);
        mToolbarTitleTextView.setText("任务列表");

        mTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final TaskAdapter adapter = new TaskAdapter();
        adapter.setClickListener(new TaskOnClickListener());
        mTaskRecyclerView.setAdapter(adapter);
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.getCurrentUserAllTasks().observe(this,
                listResource -> {
                    if (listResource != null)
                        adapter.setAimTasks(listResource.data, getContext());
                });
        int userType = AppPreferencesHelper.getCurrentUserLoginState();
        List<String> sUserType= FinalMap.getUserTypeList();

        if (userType == sUserType.indexOf(MANAGER_GROUP)){
            mFloatingActionButton.setOnClickListener(new NewTaskOnClickListener());
//            https://stackoverflow.com/questions/31624935/floatingactionbutton-expand-into-a-new-activity
        } else {
//            questions/31269958/floatingactionbutton-doesnt-hide
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) mFloatingActionButton.getLayoutParams();
            p.setAnchorId(View.NO_ID);
            mFloatingActionButton.setLayoutParams(p);
            mFloatingActionButton.hide();
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class TaskOnClickListener implements TaskAdapter.OnItemClickListener{
        @Override
        public void onItemClick(Task task) {
            Intent intent = TaskReadActivity.newIntent(getActivity(), task.getTaskID());
            startActivity(intent);
        }
    }

    private class NewTaskOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = TaskNewActivity.newIntent(getActivity());
            startActivity(intent);
        }
    }
}