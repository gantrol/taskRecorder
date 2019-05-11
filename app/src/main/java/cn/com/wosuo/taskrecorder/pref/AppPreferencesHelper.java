package cn.com.wosuo.taskrecorder.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import cn.com.wosuo.taskrecorder.BasicApp;
import cn.com.wosuo.taskrecorder.db.UserConverter;
import cn.com.wosuo.taskrecorder.ui.start.LoginMode;
import cn.com.wosuo.taskrecorder.vo.User;

import static cn.com.wosuo.taskrecorder.util.FinalStrings.LOGIN_PREF;

public class AppPreferencesHelper {
//    TODO: https://gist.github.com/idish/f46a8327da7f293f943a5bda31078c95
//     https://stackoverflow.com/questions/50649014/livedata-with-shared-preferences
    private static final String TAG = "AppPreferencesHelper";

    public static SharedPreferences getLoginPref(){
        return PreferenceManager.getDefaultSharedPreferences(BasicApp.getAppContext());
    }

    public static SharedPreferences getLoginPref(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SharedPreferences getCookiePref(){
        return BasicApp.getAppContext().getSharedPreferences(
                "CookiePersistence", Context.MODE_PRIVATE);
    }

    public static User getCurrentUser(){
        User me = null;
        String userJson = getLoginPref().getString(LOGIN_PREF, null);
        if (userJson != null) {
            me = UserConverter.toUserEntity(userJson);
        }
        return me;
    }

    public static int getCurrentUserID(){
        User me = getCurrentUser();
        int ID = -1;
        if (me != null)
            ID = me.getUid();
        return ID;
    }

    public static int getCurrentUserLoginState(){
        int logoutState = LoginMode.LOGGED_IN_MODE_LOGGED_OUT.getType();
        String userJson = getLoginPref().getString(LOGIN_PREF, null);
        if (userJson != null) {
            User me = getCurrentUser();
            if (me != null) logoutState = me.getType();
        }
        return logoutState;
    }

    public static void saveUser(User user){
        String currentUser = UserConverter.toJson(user);
        SharedPreferences.Editor editor = getLoginPref().edit();
        editor.putString(LOGIN_PREF, currentUser);
        editor.apply();
    }

    public static void clearLoginPref(){
        SharedPreferences.Editor editor = getLoginPref().edit();
        editor.clear();
        editor.apply();
    }

    public static void clearCookiePref(){
        SharedPreferences.Editor editor = getCookiePref().edit();
        editor.clear();
        editor.apply();
    }
}
