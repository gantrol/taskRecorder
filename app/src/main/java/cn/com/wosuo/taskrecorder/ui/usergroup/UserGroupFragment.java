package cn.com.wosuo.taskrecorder.ui.usergroup;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.util.Resource;
import cn.com.wosuo.taskrecorder.viewmodel.UserViewModel;
import cn.com.wosuo.taskrecorder.vo.User;

public class UserGroupFragment extends Fragment {

    private static final String TAG = "组织成员";
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.group_recycler_view) RecyclerView mGroupRecyclerView;
    @BindView(R.id.group_dialog) TextView mDialog;
    @BindView(R.id.toolbar_title) TextView mToolbarTitleTextView;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        unbinder = ButterKnife.bind(this, view);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        mGroupRecyclerView.setLayoutManager(manager);
        final UserAdapter adapter = new UserAdapter();
        mGroupRecyclerView.setAdapter(adapter);
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)requireActivity()).getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayShowTitleEnabled(false);
        mToolbarTitleTextView.setText(TAG);

        UserViewModel userViewModel =
                ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, new Observer<Resource<List<User>>>() {
            @Override
            public void onChanged(Resource<List<User>> usersResource) {
                // onChanged method triggered every time LiveData changes. so do not need to call notifyDataSetChanged methods.
                adapter.submitList(usersResource.data);
            }
        });
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
