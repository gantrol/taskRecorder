package cn.com.wosuo.taskrecorder.ui.taskread;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import cn.com.wosuo.taskrecorder.SingleFragmentActivity;

import java.util.UUID;

public class TaskActivity extends SingleFragmentActivity {

    private static final String EXTRA_TASK_ID = "cn.com.wosuo.taskrecorder.crime_id";

    public static Intent newIntent(Context packageContext, UUID taskId){
        Intent intent = new Intent(packageContext, TaskActivity.class);
        intent.putExtra(EXTRA_TASK_ID, taskId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        int taskId = getIntent()
                .getIntExtra(EXTRA_TASK_ID, -1);
        return TaskReadFragment.newInstance(taskId);
    }

}
