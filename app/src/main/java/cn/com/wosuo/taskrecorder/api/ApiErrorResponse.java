package cn.com.wosuo.taskrecorder.api;

import androidx.annotation.NonNull;

public class ApiErrorResponse<T> extends ApiResponse<T> {
    @NonNull
    private final String errorMessage;
    @NonNull
    public final String getErrorMessage() {
        return this.errorMessage;
    }

    public <T>ApiErrorResponse(@NonNull String  errorMessage){
        this.errorMessage = errorMessage;
    }
}
