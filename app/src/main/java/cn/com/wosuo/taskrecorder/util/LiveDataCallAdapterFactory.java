package cn.com.wosuo.taskrecorder.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;


import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

import cn.com.wosuo.taskrecorder.api.ApiResponse;
import retrofit2.CallAdapter;
import retrofit2.CallAdapter.Factory;
import retrofit2.Retrofit;

public final class LiveDataCallAdapterFactory extends Factory {

    @Nullable
    public CallAdapter get(@NonNull Type returnType, @NonNull Annotation[] annotations,
                           @NonNull Retrofit retrofit) {
        if (!Objects.equals(Factory.getRawType(returnType), LiveData.class)) {
            return null;
        } else if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalArgumentException("returnType must be parameterized");
        } else {
            Type observableType = Factory.getParameterUpperBound(0,
                    (ParameterizedType) returnType);
            Class rawOservableType = Factory.getRawType(observableType);
            if (!Objects.equals(rawOservableType, ApiResponse.class)) {
                throw new IllegalArgumentException("type must be a resource");
            } else if(!(observableType instanceof ParameterizedType)) {
                throw new IllegalArgumentException("resource must be parameterized");
            } else {
                Type bodyType = getParameterUpperBound(0, (ParameterizedType) observableType);
                return new LiveDataCallAdapter(bodyType);
            }
        }
    }
}
