package cn.com.wosuo.taskrecorder.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.vo.User;

public class ChooseUserAdapter extends RecyclerView.Adapter<ChooseUserAdapter.UserHolder> {
    private static CheckBox lastChecked = null;
    private int last = -1;
    private List<User> aimUsers;
    private boolean isMultiSelection;
    private LayoutInflater layoutInflater;
    private int taskID;


    public ChooseUserAdapter(Context context, int taskID, boolean isMultiSelection){
        this(context, isMultiSelection);
        this.taskID = taskID;
    }

    public ChooseUserAdapter(Context context, boolean isMultiSelection){
        this.isMultiSelection = isMultiSelection;
        layoutInflater = LayoutInflater.from(context);
        aimUsers = new ArrayList<>();
    }

    public void setUsers(List<User> users) {
        aimUsers = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_choose_user, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder userHolder, int position) {
        userHolder.bind(aimUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return aimUsers == null ? 0 : aimUsers.size();
    }

    class UserHolder extends RecyclerView.ViewHolder{

        User mUser;
        TextView mUserNameTV;
        TextView mUserMailTV;
        CheckBox mCheckBox;

        UserHolder(View view) {
            super(view);
            mUserNameTV = view.findViewById(R.id.username_tv);
            mUserMailTV = view.findViewById(R.id.usermail_tv);
            mCheckBox = view.findViewById(R.id.user_checkbox);
        }

        void bind(User user){
            mUser = user;
            mUserNameTV.setText(mUser.getName());
            mUserMailTV.setText(mUser.getMail());
            mCheckBox.setChecked(mUser.isSelected());
            mCheckBox.setOnCheckedChangeListener(
                    (buttonView, isChecked) -> {
                        mUser.setSelected(isChecked);
                        if (!isMultiSelection){
                            if (!(lastChecked == null || mCheckBox.equals(lastChecked)))
                                lastChecked.setChecked(false);
                            lastChecked = this.mCheckBox;
                            last = getAdapterPosition();
                        }
            });
        }
    }

    public ArrayList<User> getSelectedUser(){
        ArrayList<User> sUserSelect = new ArrayList<>();
        for (User user: aimUsers){
            if (user.isSelected()) {
                sUserSelect.add(user);
            }
        }
        return sUserSelect;
    }
}

