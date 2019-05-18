package cn.com.wosuo.taskrecorder.ui.taskAssign;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import cn.com.wosuo.taskrecorder.SingleFragmentActivity;
import cn.com.wosuo.taskrecorder.vo.Task;

public class TaskAssignActivity extends SingleFragmentActivity {
    private static final String EXTRA_TASK = "cn.com.wosuo.taskrecorder.ui.taskAssign.task";
    private static final String EXTRA_MUTI = "muti";
    public static Intent newIntent(Context packageContext, Task task, boolean isMutiSelect){
        Intent intent = new Intent(packageContext, TaskAssignActivity.class);
        intent.putExtra(EXTRA_MUTI, isMutiSelect);
        intent.putExtra(EXTRA_TASK, task);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        Task task = (Task) getIntent().getSerializableExtra(EXTRA_TASK);
        boolean isMutiSelect = getIntent().getBooleanExtra(EXTRA_MUTI, false);
        return TaskAssignCompanyFragment.newInstance(task, isMutiSelect);
    }
}
