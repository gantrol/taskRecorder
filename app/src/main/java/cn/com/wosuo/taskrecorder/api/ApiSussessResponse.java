package cn.com.wosuo.taskrecorder.api;

public class ApiSussessResponse<T> extends ApiResponse<T> {
    private T body;
    public ApiSussessResponse (T body){
        this.body = body;
    }

    public T getBody() {
        return body;
    }
}
