package cn.com.wosuo.taskrecorder.ui.usergroup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.vo.User;

public class UserAdapter extends ListAdapter<User, UserAdapter.UserHolder> {
    private OnItemClickListener clickListener;


    public UserAdapter() {
        super(diffUtilCallback);
    }

    private static DiffUtil.ItemCallback<User> diffUtilCallback = new DiffUtil.ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getUid() == newItem.getUid();
        }

        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getName().equals(newItem.getName())
                    && oldItem.getMail().equals(newItem.getMail())
                    && oldItem.getType() == newItem.getType();
        }
    };

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_user, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder userHolder, int position) {
        User user = getItem(position);
        userHolder.bind(user);
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class UserHolder extends RecyclerView.ViewHolder{

        private User mUser;
        TextView mUserNameTV;
        TextView mUserMailTV;

        public UserHolder(View view) {
            super(view);
            mUserNameTV = view.findViewById(R.id.username_tv);
            mUserMailTV = view.findViewById(R.id.usermail_tv);
            itemView.setOnClickListener(view1 -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && clickListener != null)
                    clickListener.onItemClick(getItem(position));
            });
        }

        void bind(User user){
            mUser = user;
            mUserNameTV.setText(mUser.getName());
            mUserMailTV.setText(mUser.getMail());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(User user);
    }
}

