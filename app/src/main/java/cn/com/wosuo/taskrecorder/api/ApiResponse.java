package cn.com.wosuo.taskrecorder.api;


import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Response;

// A generic class that contains data and status about loading this data.
public abstract class ApiResponse<T> {
    @NonNull
    public ApiErrorResponse create(@NonNull Throwable error){
        return new ApiErrorResponse(error.getMessage());
    }

    @NonNull
    public ApiResponse<T> create(@NonNull Response<T> response){
        ApiResponse<T> apiResponse;
        if (response.isSuccessful()){
            T body = response.body();
            if (body == null || response.code() == 204){
                apiResponse = new ApiEmptyReponse<T>();
            } else {
                Headers header = response.headers();
                apiResponse = new ApiSussessResponse<T>(body);
            }
        } else {
            ResponseBody responseBody = response.errorBody();
            String msg = null;
            try{
                if (responseBody != null){
                    msg = responseBody.string();
                }
            } catch (IOException ioe){
                ioe.printStackTrace();
            }
            String errMsg = (msg == null || msg.isEmpty()) ? response.message() : msg;
            apiResponse = new ApiErrorResponse<>(errMsg);
        }
        return apiResponse;
    }
}


