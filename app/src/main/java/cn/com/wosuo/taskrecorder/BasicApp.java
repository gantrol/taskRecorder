/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.com.wosuo.taskrecorder;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;

import cn.com.wosuo.taskrecorder.api.BigkeerService;
import cn.com.wosuo.taskrecorder.api.HttpUtil;
import cn.com.wosuo.taskrecorder.db.AppDatabase;
import cn.com.wosuo.taskrecorder.db.TaskDao;
import cn.com.wosuo.taskrecorder.db.UserDao;
import cn.com.wosuo.taskrecorder.repository.TaskRepository;
import cn.com.wosuo.taskrecorder.repository.UserRepository;
import cn.com.wosuo.taskrecorder.util.LiveDataCallAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static cn.com.wosuo.taskrecorder.api.Urls.HostString.BASE_URL;

/**
 * Android Application class. Used for accessing singletons.
 */
public class BasicApp extends Application {

    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppExecutors = new AppExecutors();
        SDKInitializer.initialize(this);
        BasicApp.context = getApplicationContext();
    }

    @Deprecated
    private static Context context;

    public static BigkeerService getBigkeerService(){
        return BigkeerServiceHolder.sInstance;
    }

    private static class BigkeerServiceHolder {
        private static final BigkeerService sInstance = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(HttpUtil.getHTTPClient())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(BigkeerService.class);
    }

    public static LocationClient getLocationClient(Activity activity){
        return new LocationClient(activity);
    }

    @Deprecated
    public static Context getAppContext() {
        return BasicApp.context;
    }

    @Deprecated
    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(mAppExecutors);
    }

    public AppDatabase getDatabase(Context context) {
        return AppDatabase.getInstance(context);
    }

    public UserRepository getUserRepository() {
        return UserRepository.getInstance(getDatabase());
    }

    public TaskRepository getTaskRepository() {
        return TaskRepository.getInstance(getDatabase());
    }



}
