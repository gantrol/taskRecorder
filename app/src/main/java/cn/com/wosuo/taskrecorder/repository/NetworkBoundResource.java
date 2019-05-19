package cn.com.wosuo.taskrecorder.repository;


import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import cn.com.wosuo.taskrecorder.AppExecutors;
import cn.com.wosuo.taskrecorder.api.ApiEmptyReponse;
import cn.com.wosuo.taskrecorder.api.ApiErrorResponse;
import cn.com.wosuo.taskrecorder.api.ApiResponse;
import cn.com.wosuo.taskrecorder.api.ApiSussessResponse;
import cn.com.wosuo.taskrecorder.util.Resource;

// ResultType: Type for the Resource data.
// RequestType: Type for the API response.
public abstract class NetworkBoundResource<ResultType, RequestType> {
    // Called to save the result of the API response into the database.
    private final MediatorLiveData<Resource<ResultType>> result;
    private final AppExecutors appExecutors;

    @MainThread
    public NetworkBoundResource(@NonNull AppExecutors appExecutors){
        this.appExecutors = appExecutors;
        result = new MediatorLiveData<>();
        result.setValue(Resource.loading(null));
        final LiveData<ResultType> dbSource = this.loadFromDb();
        result.addSource(dbSource, data -> {
            result.removeSource(dbSource);
            if (shouldFetch(data)){
                fetchFromNetwork(dbSource);
            } else {
                result.addSource(dbSource, o ->
                        result.setValue(Resource.success(data)));
            }
        });
    }

    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {

        final LiveData<ApiResponse<RequestType>> apiResponse = this.createCall();
        result.addSource(dbSource, newData ->
                result.setValue(Resource.loading(newData)));
        result.addSource(apiResponse, response -> {
            result.removeSource(apiResponse);
            result.removeSource(dbSource);
            if (response instanceof ApiEmptyReponse) {
                appExecutors.mainThread().execute(()
                        -> result.addSource(loadFromDb(), newData
                        -> result.setValue(Resource.success(newData))));
            } else if (response instanceof ApiSussessResponse){
                appExecutors.diskIO().execute(() -> {
                    saveCallResult(((ApiSussessResponse<RequestType>) response).getBody());
                    appExecutors.mainThread().execute(() -> result.addSource(loadFromDb(),
                            newData -> result.setValue(Resource.success(newData))));
                });

            } else if (response instanceof ApiErrorResponse) {
                onFetchFailed();
                result.addSource(dbSource, newData ->
                        result.setValue(Resource.error(
                                ((ApiErrorResponse<RequestType>) response).getErrorMessage(),
                                newData)
                        ));
            }
        });
    }

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    @MainThread
    protected void onFetchFailed(){}


    public final LiveData<Resource<ResultType>> getAsLiveData() {
        MediatorLiveData<Resource<ResultType>> result = this.result;
        if (result == null)
            throw new RuntimeException("null cannot be cast to non-null type " +
                    "androidx.lifecycle.LiveData<Resource<ResultType>>");
        return result;
    }

    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract boolean shouldFetch(@Nullable ResultType data);

    // Called to get the cached data from the database.
    @NonNull @MainThread
    protected abstract LiveData<ResultType> loadFromDb();


    @MainThread
    public abstract LiveData<ApiResponse<RequestType>> createCall();


    // Called to create the API call.
//    @NonNull @MainThread
//    protected abstract LiveData<ApiResponse<RequestType>> createCall();

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
}
