package cn.com.wosuo.taskrecorder.ui.sign;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.api.HttpUtil;
import cn.com.wosuo.taskrecorder.util.JsonParser;
import cn.com.wosuo.taskrecorder.util.Pair;
import cn.com.wosuo.taskrecorder.vo.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

import static cn.com.wosuo.taskrecorder.util.FinalStrings.FAIL;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.INVALIDREQ;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.MISSINGREQ;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.NICKNAME;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.PASSWORD;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.RMAIL;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.SOON;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.SUCCESS;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.USERNAME;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.YOU_LOGIN_SUCCESS;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.YOU_SIGNUP;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.YOU_SIGNUP_FAILED;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.YOU_SIGNUP_SUCCESS;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.signupAPI;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";


    @BindView(R.id.input_name) EditText _nameText;
    @BindView(R.id.input_nickname) EditText _nicknameText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;


    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

//        _signupButton.setOnClickListener(v -> signup());

        _loginLink.setOnClickListener(v -> {
            // Finish the registration screen and return to the Login activity
            finish();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @OnClick(R.id.btn_signup)
    public void signup() {
        Log.d(TAG, YOU_SIGNUP);

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(SOON);
        progressDialog.show();

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String nickname = _nicknameText.getText().toString();

        // curl -X POST "https://land.bigkeer.cn/api/User" -H "accept: text/plain"
        // -H "x-captcha: saf" -H "Content-Type: multipart/form-data"
        // -F "UserName=asfd" -F "Password=123456" -F "Mail=aaa@qq.com" -F "NickName=kkk"
        //
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        formBodyBuilder.add(USERNAME, name)
                .add(PASSWORD, password)
                .add(RMAIL, email)
                .add(NICKNAME, nickname);
        HttpUtil.POST(signupAPI, formBodyBuilder.build(), new Callback() {

            @Override
            public void onFailure(@Nullable Call call, @Nullable IOException e) {
                onSignupFailed();
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(@Nullable Call call, @Nullable Response response) throws IOException {
                String responseData = response.body().string();
                progressDialog.dismiss();
                Pair<Integer, User> codeAndMe = JsonParser.parseLoginJson(responseData);
                int status_code = codeAndMe.a;
                User me = codeAndMe.b;
                switch (status_code) {
                    case SUCCESS:
                        Log.d(TAG, YOU_SIGNUP_SUCCESS);
                        onSignupSuccess();
                        break;
                    case FAIL:
                        onSignupFailed();
                        break;
                    case MISSINGREQ:
                        onSignupFailed("Missing request");
                        break;
                    case INVALIDREQ:
                        onSignupFailed("Invalid request");
                        break;
                    default:
                        break;
                }
            }
        });
    }


    public void onSignupSuccess(){
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(() ->
                runOnUiThread(() ->{
                    setResult(RESULT_OK, null);
                    finish();
                    _signupButton.setEnabled(true);
                    Toast.makeText(getBaseContext(), YOU_SIGNUP_SUCCESS, Toast.LENGTH_LONG).show();
                }), 100);
        finish();
    }

    public void onSignupFailed() {
        onSignupFailed("Failed");
    }

    public void onSignupFailed(String message) {
        final String message_final;
        if (message == null){
            message_final = YOU_SIGNUP_FAILED;
        } else {
            message_final = message;
        }
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(() ->
                runOnUiThread(() -> {
                    _signupButton.setEnabled(true);
                    Toast.makeText(getBaseContext(), message_final, Toast.LENGTH_LONG).show();
                }), 100);
    }

    public boolean validate() {
//        TODO: 2 归入Final类中
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 15) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}