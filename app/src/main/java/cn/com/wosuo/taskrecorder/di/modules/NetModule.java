package cn.com.wosuo.taskrecorder.di.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import cn.com.wosuo.taskrecorder.db.AppDatabase;
import cn.com.wosuo.taskrecorder.util.CookieJarHelper;
import cn.com.wosuo.taskrecorder.util.LiveDataCallAdapterFactory;
import dagger.Module;
import dagger.Provides;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {

    String mBaseUrl;

    // Constructor needs one parameter to instantiate.
    public NetModule(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    // Dagger will only look for methods annotated with @Provides
//    @Provides
//    @Singleton
//    // Application reference must come from AppModule.class
//    SharedPreferences providesLoginSharedPreferences(Application application) {
//        return PreferenceManager.getDefaultSharedPreferences(application);
//    }

    @Provides
    @Singleton
    AppDatabase providesDatabase(Application application){
        return AppDatabase.getInstance(application);
    }

    @Provides
    @Singleton
    SharedPreferences providesCookiesSharedPreferences(Application application) {
        return application.getBaseContext().getSharedPreferences(
                "CookiePersistence", Context.MODE_PRIVATE);
    }

//    @Provides
//    @Singleton
//    Cache provideOkHttpCache(Application application) {
//        int cacheSize = 10 * 1024 * 1024; // 10 MiB
//        Cache cache = new Cache(application.getCacheDir(), cacheSize);
//        return cache;
//    }

//    @Provides
//    @Singleton
//    Gson provideGson() {
//        GsonBuilder gsonBuilder = new GsonBuilder();
////        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
//        return gsonBuilder.create();
//    }

    @Provides
    @Singleton
    CookieJar provideCookieJar (SharedPreferences cookiePref) {
        return new CookieJarHelper(cookiePref);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(CookieJar cookieJar) {
        return new OkHttpClient.Builder()
                .cookieJar(cookieJar).build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(mBaseUrl)
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(okHttpClient)
                .build();
        return retrofit;
    }
}