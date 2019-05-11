package cn.com.wosuo.taskrecorder.ui.taskphoto;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import cn.com.wosuo.taskrecorder.SingleFragmentActivity;

import static cn.com.wosuo.taskrecorder.ui.UiString.EXTRA_TASK_ID;

public class TaskPhotoActivity extends SingleFragmentActivity {
    public static Intent newIntent(Context packageContext, int taskID){
        Intent intent = new Intent(packageContext, TaskPhotoActivity.class);
        intent.putExtra(EXTRA_TASK_ID, taskID);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        int taskID = getIntent()
                .getIntExtra(EXTRA_TASK_ID, -1);
        return TaskPhotoFragment.newInstance(taskID);
    }
}
