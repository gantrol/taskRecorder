package cn.com.wosuo.taskrecorder.api;

import androidx.lifecycle.LiveData;


import java.util.List;

import cn.com.wosuo.taskrecorder.vo.ArrayResult;
import cn.com.wosuo.taskrecorder.vo.BigkeerResponse;
import cn.com.wosuo.taskrecorder.vo.GroupInfoResult;
import cn.com.wosuo.taskrecorder.vo.LocCenterPoint;
import cn.com.wosuo.taskrecorder.vo.PhotoResult;
import cn.com.wosuo.taskrecorder.vo.Task;
import cn.com.wosuo.taskrecorder.vo.Tracks;
import cn.com.wosuo.taskrecorder.vo.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

import static cn.com.wosuo.taskrecorder.api.Urls.ExploreApi.GET_LOC_EXPLORE;
import static cn.com.wosuo.taskrecorder.api.Urls.PhotoApi.PHOTO;
import static cn.com.wosuo.taskrecorder.api.Urls.TaskApi.PUT_EDIT_TASK;
import static cn.com.wosuo.taskrecorder.api.Urls.UserApi.COMPANY_GET_USERS_BY_GROUP;
import static cn.com.wosuo.taskrecorder.api.Urls.TaskApi.COMPANY_ID_IN_TASK;
import static cn.com.wosuo.taskrecorder.api.Urls.TaskApi.GET_COMPANY_TASKS;
import static cn.com.wosuo.taskrecorder.api.Urls.TaskApi.GET_MANAGER_TASKS;
import static cn.com.wosuo.taskrecorder.api.Urls.TaskApi.GET_OR_CREATE_TASKS;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.PhotoField.PHOTO_DESC;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.PhotoField.PHOTO_LOCATION;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.PhotoField.PHOTO_SUBID;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.PhotoField.PHOTO_TASKID;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.PhotoField.PHOTO_TIME;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TaskField.TASK_ID;
import static cn.com.wosuo.taskrecorder.api.Urls.UserApi.USER;
import static cn.com.wosuo.taskrecorder.api.Urls.UserApi.USER_GET_USERS_BY_GROUP;
import static cn.com.wosuo.taskrecorder.api.Urls.TaskApi.GET_USER_TASKS;
import static cn.com.wosuo.taskrecorder.api.Urls.TaskApi.MANAGER_ID_IN_TASK;

public interface BigkeerService {
//    Login, Signup
    @GET("Session")
    Call<ResponseBody> getSession();

    @FormUrlEncoded
    @POST(Urls.LoginSignUpApi.LOGIN)
    LiveData<ApiResponse<BigkeerResponse<User>>>
    login(@Field("user") String username, @Field("password") String password);

//    @POST("User")
//    Call<ResponseBody> signup();

//    public static final String patchUser = "User";

//    get User or Users
    @GET(Urls.UserApi.GET_USER_ME)
    LiveData<ApiResponse<BigkeerResponse<User>>> getUserMe();

    @GET(USER)
    LiveData<ApiResponse<BigkeerResponse<ArrayResult<User>>>> getAllUsers();

    @GET(COMPANY_GET_USERS_BY_GROUP)
    LiveData<ApiResponse<BigkeerResponse<GroupInfoResult>>>
    companyGetUsersInGroup();

    @GET(USER_GET_USERS_BY_GROUP)
    LiveData<ApiResponse<BigkeerResponse<GroupInfoResult>>>
    userGetUsersInGroup();

//    @GET(USER_GET_USERS_BY_GROUP)
//    LiveData<ApiResponse<BigkeerResponse<GroupInfoResult>>> companyGetUsersInGroup();


    @GET("User/search/{query}")
    Call<ResponseBody> searchUserByQuery(@Path("query") String query);

    @GET("User/{uid}")
    LiveData<ApiResponse<BigkeerResponse<User>>> getUserById(@Path("uid") int uid);

//    get task or tasks
    /**
     * adminGetTasks
     * @return all tasks
     */
    @GET("Task/all")
    LiveData<ApiResponse<BigkeerResponse<ArrayResult<Task>>>>
    getAllTasks();

    @GET("Task/{taskID}")
    LiveData<ApiResponse<BigkeerResponse<Task>>>
    getTaskByID(@Path(TASK_ID) int taskID);

    @GET("Task/all")
    LiveData<ApiResponse<BigkeerResponse<ArrayResult<Task>>>>
    adminGetTasks();

    @GET(GET_USER_TASKS)
    LiveData<ApiResponse<BigkeerResponse<ArrayResult<Task>>>>
    getUserTasks();

    @GET(GET_COMPANY_TASKS)
    LiveData<ApiResponse<BigkeerResponse<ArrayResult<Task>>>>
    getCompanyTasks(@Path(COMPANY_ID_IN_TASK) int companyID);

    @GET(GET_MANAGER_TASKS)
    LiveData<ApiResponse<BigkeerResponse<ArrayResult<Task>>>>
    getManagerTasks(@Path(MANAGER_ID_IN_TASK) int managerID);

    @POST(GET_OR_CREATE_TASKS)  // manager
    Call<ResponseBody> createTask();
//    { "code": 0, "message": null, "result": (taskId)}

    @PUT(PUT_EDIT_TASK)  // manager
    Call<ResponseBody> managerEditTask(@Path(TASK_ID) int taskID, @Body RequestBody requestBody);

    @PATCH("Task/{taskID}/status")  // manager, company
    Call<ResponseBody> patchTaskStatus();

    @PATCH("Task/{taskID}/result")  // company
    Call<ResponseBody> patchTaskResult();

    @POST("Assign/{taskID}")  // company
    Call<ResponseBody> companyAssignExecutor();

    @POST("Assign/{taskID}")
    Call<ResponseBody> companyAddExecutor(@Path(TASK_ID) int taskID, @Body RequestBody requestBody);

    @DELETE("Assign/{taskID}/{userID}")
    Call<ResponseBody> companyDeleteExecutor(@Path(TASK_ID) int taskID, @Path("userID") int userID);

    @GET("Track/{trackID}")
    Call<ResponseBody> getTrack(@Path("trackID") int trackID);

    @GET("Track/task/{taskID}")
    Call<ResponseBody> getTrackByTask(@Path("tasckID") int trackID);

    @POST("Track")  // user, return trackID
    Call<ResponseBody> userCreateTrack();

    @DELETE("Track/{trackID}")
    Call<ResponseBody> deleteTrack(@Path("trackID") int trackID);


    @GET("Photo/task/{taskID}")
    LiveData<ApiResponse<BigkeerResponse<List<PhotoResult>>>>
    getPhotoResultsByTaskID(@Path(TASK_ID) int taskID);


    @POST(GET_OR_CREATE_TASKS)
    Call<ResponseBody> createTask(@Body RequestBody requestBody);

    @Multipart
    @POST(PHOTO)
//    how to upload a file: https://github.com/square/retrofit/issues/1063
    Call<ResponseBody> postImage(@Part MultipartBody.Part file,
                                 @Part(PHOTO_TASKID) RequestBody taskID,
                                 @Part(PHOTO_SUBID) RequestBody type,
                                 @Part(PHOTO_TIME) RequestBody time,
                                 @Part(PHOTO_LOCATION) RequestBody location,
                                 @Part(PHOTO_DESC) RequestBody description);

    @GET(GET_LOC_EXPLORE)
    LiveData<ApiResponse<LocCenterPoint>>
    getLocCenterPointByTaskID(@Path(TASK_ID) int taskID);


    @GET("Track/task/{taskID}")
    LiveData<ApiResponse<BigkeerResponse<List<Tracks>>>>
    getTracksByTaskID(@Path(TASK_ID) int taskID);
}
