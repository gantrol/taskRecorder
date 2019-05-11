package cn.com.wosuo.taskrecorder.ui.taskloc;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import cn.com.wosuo.taskrecorder.SingleFragmentActivity;

import static cn.com.wosuo.taskrecorder.ui.UiString.EXTRA_TASK_ID;

public class TaskCenterPointActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context packageContext, int taskID){
        Intent intent = new Intent(packageContext, TaskCenterPointActivity.class);
        intent.putExtra(EXTRA_TASK_ID, taskID);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        int taskID = getIntent()
                .getIntExtra(EXTRA_TASK_ID, -1);
        return TaskCenterPointFragment.newInstance(taskID);
    }
}
