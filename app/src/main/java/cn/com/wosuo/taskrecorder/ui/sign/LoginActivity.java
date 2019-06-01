package cn.com.wosuo.taskrecorder.ui.sign;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.com.wosuo.taskrecorder.AppExecutors;
import cn.com.wosuo.taskrecorder.BasicApp;
import cn.com.wosuo.taskrecorder.MainActivity;
import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.pref.AppPreferencesHelper;
import cn.com.wosuo.taskrecorder.api.HttpUtil;
import cn.com.wosuo.taskrecorder.util.FinalStrings;
import cn.com.wosuo.taskrecorder.util.JsonParser;
import cn.com.wosuo.taskrecorder.util.Pair;
import cn.com.wosuo.taskrecorder.viewmodel.UserViewModel;
import cn.com.wosuo.taskrecorder.vo.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static cn.com.wosuo.taskrecorder.api.Urls.LoginSignUpApi.LOGIN;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.ResourceField.FAIL;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.ResourceField.INVALIDREQ;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.ResourceField.MISSINGREQ;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.ResourceField.SOON;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.ResourceField.SUCCESS;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.ToastShowField.YOU_LOGIN_FAILED;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.ToastShowField.YOU_LOGIN_SUCCESS;

public class LoginActivity extends AppCompatActivity {

//    TODO:3 网络不畅？登录失败的提示应该更多？
//    TODO:2 验证码？
    //TODO:？踢下线与退出登录时清除进程列表的功能
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static final String PLEASELOGIN = "请登录";
    private static final String REMEMBER_PASSWORD = "remember_password";
    private static final String AUTO_LOGIN = "auto_login";
    private static final String EMAIL = "email";
    private static final String EMAIL_OR_NAME = "email_or_name";
    private static final String PASSWORD = "password";
    private static final String USER_STRING = "user";


    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;
    @BindView(R.id.remenberPasswordCheckBox) CheckBox _remenberPasswordCheckBox;
    @BindView(R.id.autoLoginCheckbox) CheckBox _autoLoginCheckbox;
    private ProgressDialog progressDialog;
    private SharedPreferences prefLoginSetting;
    private SharedPreferences.Editor editor;
    private UserViewModel userViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        prefLoginSetting = AppPreferencesHelper.getLoginPref();
        boolean isRemember = prefLoginSetting.getBoolean(REMEMBER_PASSWORD, false);
        boolean isAutoLogin = prefLoginSetting.getBoolean(AUTO_LOGIN, false);
        //isRemember
        if (isRemember){
            String emailOrName = prefLoginSetting.getString(EMAIL_OR_NAME, "");
            String password = prefLoginSetting.getString(PASSWORD, "");
            _emailText.setText(emailOrName);
            _passwordText.setText(password);
            _remenberPasswordCheckBox.setChecked(true);
            if (isAutoLogin){
                _loginButton.setEnabled(false);
                _autoLoginCheckbox.setChecked(true);
                login(emailOrName, password);
            }
        }
    }

    @OnClick(R.id.btn_login)
    void login(){
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();
        login(email, password);
    }

    void login(String email, String password) {
        //TODO: 3 setEnabled(false)表现得不明显；
        _loginButton.setEnabled(false);
        Log.d(TAG, "Login");

        if (!validate(email, password)) {
            onLoginFailed(null);
            return;
        }

        //TODO: 4更改progressDialog的样式，使之更美观
        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(SOON);
        progressDialog.show();

//        userViewModel.loginByPassword(email, password);

        RequestBody requestBody = new FormBody.Builder()
                .add(PASSWORD, password)
                .add(USER_STRING, email)
                .build();

        HttpUtil.POST(LOGIN, requestBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                离线登录：利用pref存储的账号密码
                prefLoginSetting = AppPreferencesHelper.getLoginPref();
                if (email.equals(prefLoginSetting.getString(EMAIL_OR_NAME, null))&&
                        password.equals(prefLoginSetting.getString(PASSWORD, null))){
                    Log.d(TAG, "Offline Login Successed");
                    onLoginSuccess();
                } else {
                    onLoginFailed(null);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body().string();
                progressDialog.dismiss();
                Pair<Integer, User> codeAndMe = JsonParser.parseLoginJson(responseData);
                int statusCode = codeAndMe.a;
                User me = codeAndMe.b;
                if (me != null) {
                    AppPreferencesHelper.saveUser(me);
                    ((BasicApp)getApplication()).getUserRepository().insert(me);
                }
                switch (statusCode) {
                    case SUCCESS:
                        Log.d(TAG, "Successed");
                        onLoginSuccess();
                        break;
                    case FAIL:
                        onLoginFailed(null);
                        break;
                    case MISSINGREQ:
                        onLoginFailed("Missing request");
                        break;
                    case INVALIDREQ:
                        onLoginFailed("Invalid request");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @OnClick(R.id.link_signup)
    void startSignup(){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
    }

    @OnCheckedChanged(R.id.remenberPasswordCheckBox)
    void RememberUnchecked(boolean isChecked){
        if (!isChecked) _autoLoginCheckbox.setChecked(false);
    }

    @OnCheckedChanged(R.id.autoLoginCheckbox)
    void AutoLoginChecked(boolean isChecked){
        if (isChecked) _remenberPasswordCheckBox.setChecked(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    protected void onStop() {
        prefLoginSetting = AppPreferencesHelper.getLoginPref();
        editor = prefLoginSetting.edit();
        if (_remenberPasswordCheckBox.isChecked()){
            String email = _emailText.getText().toString();
            String password = _passwordText.getText().toString();
            editor.putBoolean(REMEMBER_PASSWORD, true);
            editor.putString(EMAIL_OR_NAME, email);
            editor.putString(PASSWORD, password);
        } else {
            editor.putBoolean(REMEMBER_PASSWORD, false);
            editor.remove(EMAIL_OR_NAME);
            editor.remove(PASSWORD);
        }

        if (_autoLoginCheckbox.isChecked()){
            editor.putBoolean(AUTO_LOGIN, true);
        } else {
            editor.putBoolean(AUTO_LOGIN, false);
        }
        editor.apply();

        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // By default we just finish the Activity and log them in automatically
                String password = data.getStringExtra(FinalStrings.LoginSignUpField.PASSWORD);
                String email = data.getStringExtra(FinalStrings.LoginSignUpField.EMAIL);
                AppExecutors mAppExecutors = new AppExecutors();
                mAppExecutors.mainThread().execute(() -> {
                    Toast.makeText(getBaseContext(), PLEASELOGIN, Toast.LENGTH_SHORT).show();
                    _passwordText.setText(password);
                    _emailText.setText(email);
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void onLoginSuccess(){
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(() -> runOnUiThread(() -> {
            _loginButton.setEnabled(true);
            Toast.makeText(getBaseContext(), YOU_LOGIN_SUCCESS, Toast.LENGTH_LONG).show();
        }), 100);
        openMainActivity();
    }

    public void onLoginFailed(String message) {
        final String message_final;
        if (message == null){
            message_final = YOU_LOGIN_FAILED;
        } else {
            message_final = message;
        }
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(() -> runOnUiThread(() -> {
            _loginButton.setEnabled(true);
            Toast.makeText(getBaseContext(), message_final, Toast.LENGTH_LONG).show();
        }), 100);


    }

    public boolean validate(String email, String password) {
//        TODO: Move to util file folder;
        boolean valid = true;

//        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        if (email.isEmpty() || email.length() < 3) {
            _emailText.setError(getResources().getString(R.string.valid_name));
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 15) {
            _passwordText.setError(getResources().getString(R.string.valid_password));
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}