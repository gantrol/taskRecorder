package cn.com.wosuo.taskrecorder.util;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import static cn.com.wosuo.taskrecorder.api.Urls.HostString.API;
import static cn.com.wosuo.taskrecorder.api.Urls.HostString.API_HOST;
import static cn.com.wosuo.taskrecorder.api.Urls.LoginSignUpApi.LOGIN;

public class CookieJarHelper implements CookieJar {
    //  https://tsuharesu.com/dev/handling-cookies-with-okhttp.html
    private static final String TAG = "CookieJarHelper";

    private static final HttpUrl DefaultUrl = new HttpUrl.Builder()
            .scheme("https")
            .host(API_HOST)
            .addPathSegment(API)
            .addPathSegment(LOGIN)
            .build();

    private SharedPreferences cookieStore;

    public CookieJarHelper(SharedPreferences cookieStore_first) {
        this.cookieStore = cookieStore_first;
    }

    @Override
    public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
        SharedPreferences.Editor editor = cookieStore.edit();
        for (Cookie cookie : cookies) {
            editor.putString(url.toString(), cookie.toString());
            Log.d(TAG, cookies.toString());
        }
        editor.apply();
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = new ArrayList<>();
        if (cookieStore == null) {
            return cookies;
        }
        if (cookieStore.getString(url.toString(), null) != null) {
            cookies = getCookieFromPref(url);
        } else if (cookieStore.getString(DefaultUrl.toString(), null) != null) {
            cookies = getCookieFromPref(DefaultUrl);
        }
        return cookies;
    }

    private List<Cookie> getCookieFromPref(HttpUrl url) {
        List<Cookie> cookies = new ArrayList<>();
        String[] splitCookies = cookieStore.getString(url.toString(), null).split("[,;]");
        for (String cookie : splitCookies) {
            cookies.add(Cookie.parse(url, cookie.trim()));
        }
        return cookies;
    }
}
