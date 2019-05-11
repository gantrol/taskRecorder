package cn.com.wosuo.taskrecorder.ui.tasklist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.util.DateUtil;
import cn.com.wosuo.taskrecorder.vo.Task;

public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskHolder> {
    private OnItemClickListener clickListener;


    public TaskAdapter() {
        super(diffUtilCallback);
    }

    private static DiffUtil.ItemCallback<Task> diffUtilCallback = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getTaskID() == newItem.getTaskID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getTitle().equals(newItem.getTitle())
                    && oldItem.getCreateAt() == newItem.getCreateAt()
                    && oldItem.getType() == newItem.getType();
        }
    };

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_task, parent, false);
        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder taskHolder, int position) {
        Task task = getItem(position);
        taskHolder.bind(task);
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
                    clickListener.onItemClick(getItem(position));
            });
        }

        void bind(Task task){
            mTask = task;
            mTaskNameTV.setText(mTask.getTitle());
            mTaskUpdateTV.setText(DateUtil
                    .intTimestampToChinaMiddleDateString(mTask.getUpdateAt()));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Task task);
    }
}

