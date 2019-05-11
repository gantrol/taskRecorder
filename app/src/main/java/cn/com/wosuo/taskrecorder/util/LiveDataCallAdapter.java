package cn.com.wosuo.taskrecorder.util;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;


import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.com.wosuo.taskrecorder.api.ApiResponse;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

public final class LiveDataCallAdapter<R> implements CallAdapter<R, LiveData<ApiResponse<R>>> {
    private final Type responseType;


    public LiveDataCallAdapter(@NonNull Type responseType) {
        this.responseType = responseType;
    }

    @NonNull
    public Type responseType() {
        return this.responseType;
    }

    @NonNull
    public LiveData<ApiResponse<R>> adapt(@NonNull final Call<R> call) {
        return (new LiveData<ApiResponse<R>>() {
            private AtomicBoolean started = new AtomicBoolean(false);

            protected void onActive() {
                super.onActive();
                if (started.compareAndSet(false, true)) {
                    ApiResponse<R> apiResponse = new ApiResponse<R>() {};
                    call.enqueue((new Callback<R>() {
                        public void onResponse(@NonNull Call<R> callx, @NonNull Response<R> response) {
                            postValue(apiResponse.create(response));
                        }

                        public void onFailure(@NonNull Call<R> callx, @NonNull Throwable throwable) {
                            postValue(apiResponse.create(throwable));
                        }
                    }));
                }

            }
        });
    }


}
