package cn.com.wosuo.taskrecorder.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.util.DateUtil;
import cn.com.wosuo.taskrecorder.util.FinalMap;
import cn.com.wosuo.taskrecorder.vo.Task;

import static cn.com.wosuo.taskrecorder.util.FinalStrings.TASK_CREATE;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TASK_DONE;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TASK_PROGRESS;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TASK_TEST;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {
    private OnItemClickListener clickListener;
    private List<Task> aimTasks;
    private Context mContext;
    String[] sTaskStatus = FinalMap.getTaskStatusList();

    public void setAimTasks(List<Task> taskList, Context context) {
        aimTasks = taskList;
        mContext = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_task, parent, false);
        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder taskHolder, int position) {
        taskHolder.bind(aimTasks.get(position));
    }

    @Override
    public int getItemCount() {
        return aimTasks == null ? 0 : aimTasks.size();
    }

    public void setClickListener(OnItemClickListener clickListener) {
//        TODO: different type
//         https://stackoverflow.com/questions/46372780/android-firebase-different-types-of-users-login
        this.clickListener = clickListener;
    }

    public class TaskHolder extends RecyclerView.ViewHolder{

        private Task mTask;
        TextView mTaskNameTV;
        TextView mTaskUpdateTV;
        ImageView mImageView;

        public TaskHolder(View view) {
            super(view);
            mTaskNameTV = view.findViewById(R.id.task_title);
            mTaskUpdateTV = view.findViewById(R.id.task_update_date);
            mImageView = view.findViewById(R.id.task_status_iv);
            itemView.setOnClickListener(view1 -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && clickListener != null)
                    clickListener.onItemClick(aimTasks.get(position));
            });
        }

        void bind(Task task){
            mTask = task;
            mTaskNameTV.setText(mTask.getTitle());
            mTaskUpdateTV.setText(DateUtil
                    .unixTimestampToChinaMiddleDateString(mTask.getUpdateAt()));
            String status = sTaskStatus[task.getStatus()];
//            {TASK_CREATE, TASK_PROGRESS, TASK_TEST, TASK_DONE}
            switch (status) {
                case TASK_CREATE:
                    mImageView.setImageDrawable(mContext
                            .getResources().getDrawable(R.drawable.ic_fiber_new_78_24dp));
                    break;
                case TASK_PROGRESS:
                    mImageView.setImageDrawable(mContext
                            .getResources().getDrawable(R.drawable.ic_update_61_24dp));
                    break;
                case TASK_TEST:
                    mImageView.setImageDrawable(mContext
                            .getResources().getDrawable(R.drawable.ic_help_outline_61_24dp));
                    break;
                case TASK_DONE:
                    mImageView.setImageDrawable(mContext
                            .getResources().getDrawable(R.drawable.ic_check_61_24dp));
                    break;
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Task task);
    }
}

