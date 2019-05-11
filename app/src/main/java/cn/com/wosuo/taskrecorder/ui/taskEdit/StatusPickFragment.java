package cn.com.wosuo.taskrecorder.ui.taskEdit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import cn.com.wosuo.taskrecorder.R;

public class StatusPickFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_task_status, null);
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.change_task_type)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
