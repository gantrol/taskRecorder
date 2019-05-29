package cn.com.wosuo.taskrecorder.di.components;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import cn.com.wosuo.taskrecorder.db.AppDatabase;
import cn.com.wosuo.taskrecorder.di.modules.AppModule;
import cn.com.wosuo.taskrecorder.di.modules.NetModule;
import dagger.Component;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {
    // downstream components need these exposed
    SharedPreferences sharedPreferences();
    AppDatabase appDatabase();
    CookieJar cookieJar();
    OkHttpClient okHttpClient();
    Retrofit retrofit();
}