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


//    https://inthecheesefactory.com/blog/fragment-state-saving-best-practices/en
//    https://stackoverflow.com/questions/4916209/which-design-patterns-are-used-on-android

    private static final String TAG = "MainActivity";
    @BindView(R.id.main_bottom_navigation) BottomNavigationView mBottomNavigationView;
    final Fragment mTaskListFragment = new TaskListFragment();
    final Fragment mUserGroupFragment = new UserGroupFragment();
    final Fragment mUserMeFragment = new UserMeFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = mTaskListFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.main_fragment_container, new TaskListFragment()).commit();
//        }
        fm.beginTransaction().add(R.id.main_fragment_container, mUserMeFragment, "3")
                .hide(mUserMeFragment).commit();
        fm.beginTransaction().add(R.id.main_fragment_container, mUserGroupFragment, "2")
                .hide(mUserGroupFragment).commit();
        fm.beginTransaction().add(R.id.main_fragment_container, mTaskListFragment, "1").commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
        = item -> {
//        TODO： 这种切换方式比较快，但横竖屏切换会出问题，
//         已经禁用，或许这样更好？
//         1.viewholder, 2. saveStaete;
            switch (item.getItemId()) {
                case R.id.nav_list:
                    fm.beginTransaction().hide(active).show(mTaskListFragment).commit();
                    active = mTaskListFragment;
                    break;
                case R.id.nav_group:
                    fm.beginTransaction().hide(active).show(mUserGroupFragment).commit();
                    active = mUserGroupFragment;
                    break;
                case R.id.nav_me:
                    fm.beginTransaction().hide(active).show(mUserMeFragment).commit();
                    active = mUserMeFragment;
                    break;
            }
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.main_fragment_container, selectedFragment).commit();
            return true;
        };

}
