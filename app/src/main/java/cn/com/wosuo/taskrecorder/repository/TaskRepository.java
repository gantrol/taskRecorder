package cn.com.wosuo.taskrecorder.repository;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;


import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.com.wosuo.taskrecorder.AppExecutors;
import cn.com.wosuo.taskrecorder.BasicApp;
import cn.com.wosuo.taskrecorder.api.ApiResponse;
import cn.com.wosuo.taskrecorder.api.BigkeerService;
import cn.com.wosuo.taskrecorder.api.HttpUtil;
import cn.com.wosuo.taskrecorder.db.AppDatabase;
import cn.com.wosuo.taskrecorder.db.LocCenterPointDao;
import cn.com.wosuo.taskrecorder.db.PhotoDao;
import cn.com.wosuo.taskrecorder.db.TaskDao;
import cn.com.wosuo.taskrecorder.db.TrackDao;
import cn.com.wosuo.taskrecorder.util.FinalMap;
import cn.com.wosuo.taskrecorder.util.RateLimiter;
import cn.com.wosuo.taskrecorder.util.Resource;
import cn.com.wosuo.taskrecorder.vo.ArrayResult;
import cn.com.wosuo.taskrecorder.vo.BigkeerResponse;
import cn.com.wosuo.taskrecorder.vo.LocCenterPoint;
import cn.com.wosuo.taskrecorder.vo.PhotoResult;
import cn.com.wosuo.taskrecorder.vo.Task;
import cn.com.wosuo.taskrecorder.vo.Track;
import cn.com.wosuo.taskrecorder.vo.Tracks;
import cn.com.wosuo.taskrecorder.vo.User;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static cn.com.wosuo.taskrecorder.api.Urls.PHOTO_FILE;
import static cn.com.wosuo.taskrecorder.api.Urls.TASK_COOR;
import static cn.com.wosuo.taskrecorder.api.Urls.TASK_ID;
import static cn.com.wosuo.taskrecorder.api.Urls.TASK_STATUS;
import static cn.com.wosuo.taskrecorder.api.Urls.TASK_TRACK;
import static cn.com.wosuo.taskrecorder.api.Urls.TASK_TRACK_DATA;
import static cn.com.wosuo.taskrecorder.api.Urls.TASK_X;
import static cn.com.wosuo.taskrecorder.api.Urls.TASK_Y;
import static cn.com.wosuo.taskrecorder.api.Urls.fieldStrings.USER_ID;
import static cn.com.wosuo.taskrecorder.ui.UiString.TASK_LIST;

/**
 * Repository handling the work with tasks.
 */
public class TaskRepository {
//    TODO: 3RateLimit need to be smart set.

    private static TaskRepository sInstance;
    private BigkeerService mBigkeerService;
    private AppDatabase mDatabase;
    private TaskDao mTaskDao;
    private PhotoDao mPhotoDao;
    private LocCenterPointDao mLocCenterPointDao;
    private TrackDao mTrackDao;
    private AppExecutors mAppExecutors;
    private RateLimiter<Integer> taskListRateLimit = new RateLimiter<>(3, TimeUnit.MINUTES);
    private RateLimiter<Integer> photoResultRateLimit = new RateLimiter<>(3, TimeUnit.MINUTES);
    private RateLimiter<Integer> centerLocRateLimit = new RateLimiter<>(3, TimeUnit.MINUTES);
    private RateLimiter<Integer> tracksRateLimit = new RateLimiter<>(3, TimeUnit.MINUTES);

    private RateLimiter<LiveData<Task>> taskRateLimit = new RateLimiter<>(30, TimeUnit.SECONDS);
    private final static List<String> sTaskType = FinalMap.getTaskTypeList();


    private TaskRepository(final AppDatabase database) {
        mAppExecutors = new AppExecutors();
        mDatabase = database;
        mTaskDao = database.taskDao();
        mPhotoDao = database.photoDao();
        mLocCenterPointDao = database.locCenterPointDao();
        mTrackDao = database.trackDao();
        mBigkeerService = BasicApp.getBigkeerService();
    }

    public static TaskRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (TaskRepository.class) {
                if (sInstance == null) {
                    sInstance = new TaskRepository(database);
                }
            }
        }
        return sInstance;
    }

    /**
     * Get the list of tasks from the database and get notified when the data changes.
     */
    public LiveData<Resource<List<Task>>> getAllTasks(int currentUserId) {
        return (new NetworkBoundResource<List<Task>, BigkeerResponse<ArrayResult<Task>>>(mAppExecutors) {
            @Override
            protected void saveCallResult(@NonNull BigkeerResponse<ArrayResult<Task>> item) {
                saveTasksUsersInDaoWithUid(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Task> data) {
                return data == null || data.isEmpty() || taskListRateLimit.shouldFetch(currentUserId);
            }

            @NonNull
            @Override
            protected LiveData<List<Task>> loadFromDb() {
                return mTaskDao.getAllTasks();
            }

            @Override
            public LiveData<ApiResponse<BigkeerResponse<ArrayResult<Task>>>> createCall() {
                return mBigkeerService.getAllTasks();
            }

            @Override
            protected void onFetchFailed() {
                taskListRateLimit.reset(TASK_LIST);
            }
        }).getAsLiveData();
    }

    public LiveData<Resource<List<Task>>> getCompanyTasks(int id){
        return (new NetworkBoundResource<List<Task>, BigkeerResponse<ArrayResult<Task>>>(mAppExecutors) {
            @Override
            protected void saveCallResult(@NonNull BigkeerResponse<ArrayResult<Task>> item) {
                saveTasksUsersInDaoWithUid(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Task> data) {
                return data == null || data.isEmpty() || taskListRateLimit.shouldFetch(id);
            }

            @NonNull
            @Override
            protected LiveData<List<Task>> loadFromDb() {
                return mTaskDao.getCompanyTasks(id);
            }

            @Override
            public LiveData<ApiResponse<BigkeerResponse<ArrayResult<Task>>>> createCall() {
                return mBigkeerService.getCompanyTasks(id);
            }

            @Override
            protected void onFetchFailed() {
                taskListRateLimit.reset(TASK_LIST);
            }

        }).getAsLiveData();
    }

    public LiveData<Resource<List<Task>>> getManagerTasks(int id) {
        return (new NetworkBoundResource<List<Task>, BigkeerResponse<ArrayResult<Task>>>(mAppExecutors) {
            @Override
            protected void saveCallResult(@NonNull BigkeerResponse<ArrayResult<Task>> item) {
                saveTasksUsersInDaoWithUid(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Task> data) {
                return data == null || data.isEmpty() || taskListRateLimit.shouldFetch(id);
            }

            @NonNull
            @Override
            protected LiveData<List<Task>> loadFromDb() {
                return mTaskDao.getManagerTasks(id);
            }

            @Override
            public LiveData<ApiResponse<BigkeerResponse<ArrayResult<Task>>>> createCall() {
                return mBigkeerService.getManagerTasks(id);
            }

            @Override
            protected void onFetchFailed() {
                taskListRateLimit.reset(TASK_LIST);
            }

        }).getAsLiveData();
    }

    public LiveData<Resource<List<Task>>> getUserTasks(int currentUserId) {
        return (new NetworkBoundResource<List<Task>, BigkeerResponse<ArrayResult<Task>>>(mAppExecutors) {
            @Override
            protected void saveCallResult(@NonNull BigkeerResponse<ArrayResult<Task>> item) {
                List<Task> tasks = item.getResult().getData();
                for (Task task : tasks) {
                    task.setExecutor(currentUserId);
                    saveTaskUsersInDaoWithUid(task);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Task> data) {
                return data == null || data.isEmpty() || taskListRateLimit.shouldFetch(currentUserId);
            }

            @NonNull
            @Override
            protected LiveData<List<Task>> loadFromDb() {
                return mTaskDao.getUserTasks(currentUserId);
            }

            @Override
            public LiveData<ApiResponse<BigkeerResponse<ArrayResult<Task>>>> createCall() {
                return mBigkeerService.getUserTasks();
            }

            @Override
            protected void onFetchFailed() {
                taskListRateLimit.reset(TASK_LIST);
            }

        }).getAsLiveData();
    }

    public LiveData<Task> getTask(int id) {
        return mDatabase.taskDao().getTask(id);
    }
//    public LiveData<Resource<Task>> getTask(int id) {
//        return (new NetworkBoundResource<Task, BigkeerResponse<Task>>(mAppExecutors) {
//            @Override
//            protected void saveCallResult(@NonNull BigkeerResponse<Task> item) {
//                Task task = item.getResult();
//                saveTaskUsersInDaoWithUid(task);
//            }
//
//            @Override
//            protected boolean shouldFetch(@Nullable Task data) {
//                return data == null || taskRateLimit.shouldFetch(loadFromDb());
//            }
//
//            @NonNull
//            @Override
//            protected LiveData<Task> loadFromDb() {
//                return mTaskDao.getTask(id);
//            }
//
//            @Override
//            public LiveData<ApiResponse<BigkeerResponse<Task>>> createCall() {
//                return mBigkeerSerivice.getTaskByID(id);
//            }
//        }
//        ).getAsLiveData();
//    }

    public LiveData<Resource<List<PhotoResult>>> getPhotoResultsByTaskID(int taskID){
        return (new NetworkBoundResource<List<PhotoResult>, BigkeerResponse<List<PhotoResult>>>(mAppExecutors) {
            @Override
            protected void saveCallResult(@NonNull BigkeerResponse<List<PhotoResult>> item) {
                List<PhotoResult> photoResults = item.getResult();
                mPhotoDao.insertAll(photoResults);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<PhotoResult> data) {
                return data == null || data.isEmpty() || photoResultRateLimit.shouldFetch(taskID);
            }

            @NonNull
            @Override
            protected LiveData<List<PhotoResult>> loadFromDb() {
                return mPhotoDao.getPhotoResultByTaskID(taskID);
            }

            @Override
            public LiveData<ApiResponse<BigkeerResponse<List<PhotoResult>>>> createCall() {
                return mBigkeerService.getPhotoResultsByTaskID(taskID);
            }

            @Override
            protected void onFetchFailed() {
                taskListRateLimit.reset(TASK_LIST);
            }
        }).getAsLiveData();
    }

    @Deprecated
    public LiveData<Resource<LocCenterPoint>> getLocCenterPointByTaskID(int taskID){
        return (new NetworkBoundResource<LocCenterPoint, LocCenterPoint>(mAppExecutors){
            @Override
            protected void saveCallResult(@NonNull LocCenterPoint item) {
                mLocCenterPointDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable LocCenterPoint data) {
                return data == null || centerLocRateLimit.shouldFetch(taskID);
            }

            @NonNull
            @Override
            protected LiveData<LocCenterPoint> loadFromDb() {
                return mLocCenterPointDao.getLocCenterPointByTaskID(taskID);
            }

            @Override
            public LiveData<ApiResponse<LocCenterPoint>> createCall() {
                return mBigkeerService.getLocCenterPointByTaskID(taskID);
            }

            @Override
            protected void onFetchFailed() {
                taskListRateLimit.reset(TASK_LIST);
            }
        }).getAsLiveData();
    }

    @Deprecated
    public LiveData<Resource<List<Tracks>>> getTracksByTaskID(int taskID){
        return (new NetworkBoundResource<List<Tracks>, BigkeerResponse<List<Tracks>>>(mAppExecutors) {
            @Override
            protected void saveCallResult(@NonNull BigkeerResponse<List<Tracks>> item) {
                List<Tracks> tracks = item.getResult();
                if (!(tracks == null || tracks.isEmpty())) {
                    mTrackDao.insertAll(tracks);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Tracks> data) {
                return data == null || data.isEmpty() || tracksRateLimit.shouldFetch(taskID) ;
            }

            @NonNull
            @Override
            protected LiveData<List<Tracks>> loadFromDb() {
                return mTrackDao.getTracksByTaskID(taskID);
            }

            @Override
            public LiveData<ApiResponse<BigkeerResponse<List<Tracks>>>> createCall() {
                return mBigkeerService.getTracksByTaskID(taskID);
            }

            @Override
            protected void onFetchFailed() {
                taskListRateLimit.reset(TASK_LIST);
            }
        }).getAsLiveData();
    }

    public Call<ResponseBody> postPhoto(File photo, int type, long time,
                                        String locationStr, String desciption, int taskID){
//        @Field("subID") int type,
//        @Field("time") long time, @Field("locationStr") String location,
//        @Field("description") String description
        RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), photo);
        MultipartBody.Part fpart = MultipartBody.Part.createFormData(PHOTO_FILE, photo.getName(), fbody);
        RequestBody typeBody = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(type));
        RequestBody timeBody = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(time));
        RequestBody taskBody = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(taskID));
        RequestBody locBody = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(locationStr));
        RequestBody descBody = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(desciption));
        return mBigkeerService.postImage(fpart, taskBody, typeBody, timeBody, locBody, descBody);
    }

    public Call<ResponseBody> createTask(RequestBody requestBody){
        return mBigkeerService.createTask(requestBody);
    }

    public void postTaskTrack(String trackData, int taskID, Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add(TASK_TRACK_DATA, trackData)
                .add(TASK_ID, String.valueOf(taskID))
                .build();
        HttpUtil.POST(TASK_TRACK, requestBody, callback);
    }

    public void patchTaskStatus(int taskID, int status, Callback callback){
//        TODO: Update database in successful callback
        RequestBody requestBody = new FormBody.Builder()
                .add(TASK_STATUS, Integer.toString(status))
                .build();
        HttpUtil.PATCH("Task/" + taskID + "/status", requestBody, callback);
    }

    /**
     *
     * @param taskID 任务编号
     * @param x 经度，
     * @param y 纬度,
     * @param coordinate 默认为2，百度坐标系
     * @param callback 回调
     */
    public void putCenterPointCoordinate(int taskID, double x, double y, int coordinate, Callback callback){
        //        TODO: Update database in successful callback
        RequestBody requestBody = new FormBody.Builder()
                .add(TASK_X, String.valueOf(x))
                .add(TASK_Y, String.valueOf(y))
                .add(TASK_COOR, String.valueOf(coordinate))
                .build();
        HttpUtil.PUT("Explore/" + taskID, requestBody, callback);
    }

    /**
     * All CRUD operation should be done in background thread.
     * Room database do not allow to do database operation on main thread,
     * so we have to create {@link AsyncTask classes for each crud operation.
     */
    public void insert(Task task) {
        new InsertTaskAsyncTask(mTaskDao).execute(task);
    }

    public void update(Task task) {
        new UpdateTaskAsyncTask(mTaskDao).execute(task);
    }

    public void delete(Task task) {
        new DeleteTaskAsyncTask(mTaskDao).execute(task);
    }

    public void deleteAllTasks() {
        new DeleteAllTasksAsyncTask(mTaskDao).execute();
    }

    private static class InsertTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        private InsertTaskAsyncTask(TaskDao dao) {
            this.taskDao = dao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insert(tasks[0]);
            return null;
        }
    }

    private static class UpdateTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        private UpdateTaskAsyncTask(TaskDao dao) {
            this.taskDao = dao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.update(tasks[0]);
            return null;
        }
    }

    private static class DeleteTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        private DeleteTaskAsyncTask(TaskDao dao) {
            this.taskDao = dao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.delete(tasks[0]);
            return null;
        }
    }

    private static class DeleteAllTasksAsyncTask extends AsyncTask<Void, Void, Void> {
        private TaskDao taskDao;

        private DeleteAllTasksAsyncTask(TaskDao dao) {
            this.taskDao = dao;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            taskDao.deleteAllTasks();
            return null;
        }
    }

//    TODO: 更新操作中的网络与数据库的处理？
    public void updateTaskStatus(int taskID, int status){
        mTaskDao.updateStatus(taskID, status);
    }

    public Call<ResponseBody> companyRemoteAddExecutor(int taskID, int userID){
        RequestBody requestBody = new FormBody.Builder()
                .add(USER_ID, String.valueOf(userID))
                .build();
        return mBigkeerService.companyAddExecutor(taskID, requestBody);
    }

    public Call<ResponseBody> companyRemoteDeleteExecutor(int taskID, int userID){
        return mBigkeerService.companyDeleteExecutor(taskID, userID);
    }

//    public void companyLocalDeleteExecutor(int taskID, int userID){
//        mTaskDao.getUserTasks()
//    }
//
//    public Call<ResponseBody> companyDeleteExecutor(int taskID, int userID){
//        return mBigkeerService.companyDeleteExecutor(taskID, userID);
//    }

    public LiveData<Task> loadTask(final int taskId) {
        return mDatabase.taskDao().getTask(taskId);
    }

    public LiveData<List<Task>> searchTasks(String query) {
        return mDatabase.taskDao().searchAllTasks(query);
    }

    private void saveTasksUsersInDaoWithUid(BigkeerResponse<ArrayResult<Task>> item) {
        List<Task> tasks = item.getResult().getData();
        for (Task task: tasks){
            saveTaskUsersInDaoWithUid(task);
        }
    }

    private void saveTaskUsersInDaoWithUid(Task task) {
        if (task.getAssignee() != null)
            task.setAssignee_id(task.getAssignee().getUid());
        if (task.getAssigner() != null)
            task.setAssigner_id(task.getAssigner().getUid());
        mTaskDao.insert(task);
    }

    public void resetTaskListRateLimit(int userID) {
        taskListRateLimit.reset(userID);
    }
}