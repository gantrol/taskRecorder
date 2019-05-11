package cn.com.wosuo.taskrecorder;

import org.junit.Test;

import androidx.test.core.app.ActivityScenario;

import org.junit.Assert;

import cn.com.wosuo.taskrecorder.ui.sign.LoginActivity;

public class LoginActivityTest {
    @Test
    public void emailValidtor_CorrectEmailSimple_ReturnTrue(){
        new Runnable() { @Override public void run() {
//            https://stackoverflow.com/questions/40618803/android-app-crashes-when-launched-in-debug-mode
            ActivityScenario<LoginActivity> login = ActivityScenario.launch(LoginActivity.class);
            login.onActivity(activity -> {
                Assert.assertTrue(activity.validate("name@email.com", "123456"));

    });
        }};
    }
}
