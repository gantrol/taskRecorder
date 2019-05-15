package cn.com.wosuo.taskrecorder.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.vo.User;

public class ChooseUserAdapter extends ListAdapter<User, ChooseUserAdapter.UserHolder> {
    private static final int MULTI_SELECTION = 2;
    private static final int SINGLE_SELECTION = 1;
    private OnItemClickListener clickListener;
    private boolean isMultiSelectionEnabled = false;

    public ChooseUserAdapter() {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_choose_user, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder userHolder, int position) {
        User user = getItem(position);
        userHolder.bind(user);
    }

    public void clickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    class UserHolder extends RecyclerView.ViewHolder{

        private User mUser;
        TextView mUserNameTV;
        TextView mUserMailTV;
        CheckBox mCheckBox;

        UserHolder(View view) {
            super(view);
            mUserNameTV = view.findViewById(R.id.username_tv);
            mUserMailTV = view.findViewById(R.id.usermail_tv);
            mCheckBox = view.findViewById(R.id.user_checkbox);
            itemView.setOnClickListener(view1 -> {
                boolean value = mUser.isSelected() && getItemViewType() == MULTI_SELECTION;
                setChecked(!value);
            });
        }

        void setChecked(boolean value) {
            mCheckBox.setChecked(value);
            mUser.setSelected(value);
        }

        void bind(User user){
            mUser = user;
            mUserNameTV.setText(mUser.getName());
            mUserMailTV.setText(mUser.getMail());
            mCheckBox.setChecked(mUser.isSelected());

        }
    }

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    @Override
    public int getItemViewType(int position) {
        if (isMultiSelectionEnabled) {
            return ChooseUserAdapter.MULTI_SELECTION;
        } else {
            return ChooseUserAdapter.SINGLE_SELECTION;
        }
    }
}

