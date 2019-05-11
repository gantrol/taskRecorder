package cn.com.wosuo.taskrecorder.ui.start;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import cn.com.wosuo.taskrecorder.MainActivity;
import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.pref.AppPreferencesHelper;
import cn.com.wosuo.taskrecorder.ui.sign.LoginActivity;
import cn.com.wosuo.taskrecorder.api.HttpUtil;
import cn.com.wosuo.taskrecorder.util.JsonParser;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static cn.com.wosuo.taskrecorder.util.FinalStrings.SUCCESS;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.getUserMeAPI;

public class StartActivity extends AppCompatActivity{

    private SharedPreferences LoginPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        int userType = AppPreferencesHelper.getCurrentUserLoginState();
        if (userType == LoginMode.LOGGED_IN_MODE_LOGGED_OUT.getType()) {
            startLogin();
        } else {
            openMainActivity();
        }
    }

    private void startLogin() {
        HttpUtil.GET(getUserMeAPI, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                openLoginActivity();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // 成功，登录成功；失败，输入账号密码。
                String responseData = response.body().string();
                int status_code = (int) JsonParser.parseLoginJson(responseData).a;
                if (status_code != SUCCESS)
                    openLoginActivity();
                else
                    openMainActivity();
            }
        });
    }

    public void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
