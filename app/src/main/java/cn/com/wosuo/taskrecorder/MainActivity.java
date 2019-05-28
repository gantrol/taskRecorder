package cn.com.wosuo.taskrecorder;


import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.wosuo.taskrecorder.ui.tasklist.TaskListFragment;
import cn.com.wosuo.taskrecorder.ui.usergroup.UserGroupFragment;
import cn.com.wosuo.taskrecorder.ui.userme.UserMeFragment;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.main_bottom_navigation) BottomNavigationView mBottomNavigationView;
    final Fragment mTaskListFragment = new TaskListFragment();
    final Fragment mUserGroupFragment = new UserGroupFragment();
    final Fragment mUserMeFragment = new UserMeFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment_container, mTaskListFragment).commit();
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
        = item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_list:
                    selectedFragment = mTaskListFragment;
                    break;
                case R.id.nav_group:
                    selectedFragment = mUserGroupFragment;
                    break;
                case R.id.nav_me:
                    selectedFragment = mUserMeFragment;
                    break;
            }
            switchFragment(selectedFragment);
            return true;
        };

    private void switchFragment(Fragment selectedFragment){
        if (selectedFragment != null)
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, selectedFragment).commit();
    }
}
