package cn.com.wosuo.taskrecorder.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.util.DateUtil;
import cn.com.wosuo.taskrecorder.vo.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {
    private OnItemClickListener clickListener;
    private List<Task> aimTasks;

    public void setAimTasks(List<Task> taskList) {
        aimTasks = taskList;
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

        public TaskHolder(View view) {
            super(view);
            mTaskNameTV = view.findViewById(R.id.task_title);
            mTaskUpdateTV = view.findViewById(R.id.task_update_date);
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
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Task task);
    }
}

