package cn.com.wosuo.taskrecorder.di.modules;

import cn.com.wosuo.taskrecorder.api.BigkeerService;
import cn.com.wosuo.taskrecorder.di.UserScope;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class BigkeerModule {

    @Provides
    @UserScope
    public BigkeerService providesGitHubInterface(Retrofit retrofit) {
        return retrofit.create(BigkeerService.class);
    }
}
