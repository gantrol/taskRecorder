package cn.com.wosuo.taskrecorder.ui.userme;

import android.content.SharedPreferences;

import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

import cn.com.wosuo.taskrecorder.vo.User;

import static cn.com.wosuo.taskrecorder.util.FinalStrings.LOGIN_PREF;

@Deprecated
public class UserMeViewModel extends ViewModel {
    private String UserEntityId;
    private User me;

    public void init(SharedPreferences sharedPreferences) {
        String UserEntityJson = sharedPreferences.getString(LOGIN_PREF, null);
        Gson gson = new Gson();
        me = gson.fromJson(UserEntityJson, User.class);
    }

    public User getUserEntity() {
        return me;
    }
}
