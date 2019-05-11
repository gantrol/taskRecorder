package cn.com.wosuo.taskrecorder.ui.taskread;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import cn.com.wosuo.taskrecorder.SingleFragmentActivity;

import static cn.com.wosuo.taskrecorder.ui.UiString.EXTRA_TASK_ID;

public class TaskReadActivity extends SingleFragmentActivity{

    public static Intent newIntent(Context packageContext, int taskId){
        Intent intent = new Intent(packageContext, TaskReadActivity.class);
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
