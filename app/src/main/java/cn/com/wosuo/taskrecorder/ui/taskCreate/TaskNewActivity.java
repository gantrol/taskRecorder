package cn.com.wosuo.taskrecorder.ui.taskCreate;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import java.util.UUID;

import cn.com.wosuo.taskrecorder.SingleFragmentActivity;
import cn.com.wosuo.taskrecorder.ui.taskread.TaskActivity;

public class TaskNewActivity extends SingleFragmentActivity{


    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, TaskNewActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return TaskNewFragment.newInstance();
    }
}
