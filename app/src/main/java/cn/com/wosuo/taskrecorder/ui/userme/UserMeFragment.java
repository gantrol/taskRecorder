package cn.com.wosuo.taskrecorder.ui.userme;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.pref.AppPreferencesHelper;
import cn.com.wosuo.taskrecorder.ui.sign.LoginActivity;
import cn.com.wosuo.taskrecorder.util.FinalMap;
import cn.com.wosuo.taskrecorder.viewmodel.TaskViewModel;
import cn.com.wosuo.taskrecorder.vo.User;


public class UserMeFragment extends Fragment {

    private Unbinder unbinder;
    TaskViewModel mTaskViewModel;
    User me;
    private static final String TAG = "个人资料";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView mToolbarTitleTextView;
    @BindView(R.id.my_avatar) ImageView myAvatarImageView;
    @BindView(R.id.logout_btn) Button mLogoutButton;
    @BindView(R.id.big_my_name) TextView mBigMyNameTextView;
    @BindView(R.id.my_group) TextView mMyGroupTextView;
    @BindView(R.id.my_group_row) TableRow mMyGroupTableRow;
    @BindView(R.id.my_name) TextView mMyNameTextView;
    @BindView(R.id.my_email) TextView mMyEmailTextView;
    @BindView(R.id.my_type) TextView mMyTypeTextView;
    @BindView(R.id.my_id) TextView mMyIdTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)requireActivity()).getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayShowTitleEnabled(false);
        mToolbarTitleTextView.setText(TAG);
        Picasso.get().load(R.drawable.nav_icon).into(myAvatarImageView);
        mLogoutButton.setOnClickListener(v -> logout());
        mTaskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        me = mTaskViewModel.getMe();
//        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
//        userViewModel.getUseMe().observe(this, userResource -> {
//            User me = userResource.data;
//            if (me != null)
                UserMeFragment.this.updateUI(me);
//        });
        return view;
    }

    private void logout() {
        AppPreferencesHelper.clearLoginPref();
        AppPreferencesHelper.clearCookiePref();
        mTaskViewModel.resetTaskListRateLimit(me.getUid());
        LoginWithActivity();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void updateUI(User me) {
        mBigMyNameTextView.setText(me.getName());
//        mMyGroupTextView.setText((me.getCompany()== null) ? WITHOUT_NOW: me.getCompany().getName());
        if (me.getCompany()== null) {
            mMyGroupTableRow.setVisibility(View.GONE);
        } else {
            mMyGroupTextView.setText(me.getCompany().getName());
        }
        mMyNameTextView.setText(me.getName());
        mMyEmailTextView.setText(me.getMail());
        mMyTypeTextView.setText(FinalMap.getUserTypeList().get(me.getType()));
        mMyIdTextView.setText(Integer.toString(me.getUid()));
    }

    private void LoginWithActivity() {
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        requireActivity().finish();
        startActivity(intent);
    }
}
