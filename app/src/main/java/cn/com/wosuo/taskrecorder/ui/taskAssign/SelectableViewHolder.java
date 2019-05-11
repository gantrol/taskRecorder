package cn.com.wosuo.taskrecorder.ui.taskAssign;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.vo.SelectableUser;

public class SelectableViewHolder extends RecyclerView.ViewHolder {
//    https://medium.com/@maydin/multi-and-single-selection-in-recyclerview-d29587a7dee2

    public static final int MULTI_SELECTION = 2;
    public static final int SINGLE_SELECTION = 1;
    private SelectableUser mUser;
    TextView mUserNameTV;
    TextView mUserMailTV;
    CheckBox mCheckBox;
    OnItemSelectedListener itemSelectedListener;


    public SelectableViewHolder(View view, OnItemSelectedListener listener) {
        super(view);
        itemSelectedListener = listener;
        mUserNameTV = view.findViewById(R.id.username_tv);
        mUserMailTV = view.findViewById(R.id.usermail_tv);
        mCheckBox = view.findViewById(R.id.user_checkbox);
        itemView.setOnClickListener(view1 -> {
            boolean value = mUser.isSelected() && getItemViewType() == MULTI_SELECTION;
             setChecked(!value);
             itemSelectedListener.onItemSelected(mUser);

        });
    }

    public void setChecked(boolean value) {
        mCheckBox.setChecked(value);
        mUser.setSelected(value);
    }

    public interface OnItemSelectedListener {

        void onItemSelected(SelectableUser user);
    }

}
