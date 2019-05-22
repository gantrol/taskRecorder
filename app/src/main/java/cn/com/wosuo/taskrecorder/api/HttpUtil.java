package cn.com.wosuo.taskrecorder.api;


import cn.com.wosuo.taskrecorder.pref.AppPreferencesHelper;
import cn.com.wosuo.taskrecorder.util.CookieJarHelper;
import okhttp3.Callback;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static cn.com.wosuo.taskrecorder.api.Urls.HostString.BASE_URL;

public class HttpUtil {

    private static OkHttpClient httpClient = null;
    private static CookieJar cookieJar = null;

    private static CookieJar getCookieJar(){
        if (cookieJar == null){
            cookieJar = new CookieJarHelper(AppPreferencesHelper.getCookiePref());
        }
        return cookieJar;
    }

    public static OkHttpClient getHTTPClient() {
        if (httpClient == null) {
            httpClient = new OkHttpClient.Builder()
                    .cookieJar(getCookieJar()).build();
        }
        return httpClient;
    }

    @Deprecated
    public static void POST(String address, RequestBody requestBody, Callback callback) {
        Request request = new Request.Builder()
                .url(getAbsoluteUrl(address))
                .post(requestBody)
                .build();
        getHTTPClient().newCall(request).enqueue(callback);
    }

    public static void PATCH(String address, RequestBody requestBody, Callback callback) {
        Request request = new Request.Builder()
                .url(getAbsoluteUrl(address))
                .patch(requestBody)
                .build();
        getHTTPClient().newCall(request).enqueue(callback);
    }

    public static void PUT(String address, RequestBody requestBody, Callback callback) {
        Request request = new Request.Builder()
                .url(getAbsoluteUrl(address))
                .put(requestBody)
                .build();
        getHTTPClient().newCall(request).enqueue(callback);
    }


    @Deprecated
    public static void GET(String address, Callback callback) {
        Request request = new Request.Builder()
                .url(getAbsoluteUrl(address))
                .build();
        getHTTPClient().newCall(request).enqueue(callback);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}

