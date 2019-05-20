package cn.com.wosuo.taskrecorder.ui.taskEdit;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import cn.com.wosuo.taskrecorder.SingleFragmentActivity;
import cn.com.wosuo.taskrecorder.vo.Task;


public class TaskEditActivity extends SingleFragmentActivity {

    private static final String EXTRA_TASK = "cn.com.wosuo.taskrecorder.ui.taskAssign.task";

    public static Intent newIntent(Context packageContext, Task task){
        Intent intent = new Intent(packageContext, TaskEditActivity.class);
        intent.putExtra(EXTRA_TASK, task);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        Task task = (Task) getIntent().getSerializableExtra(EXTRA_TASK);
        return TaskEditFragment.newInstance(task);
    }
}
