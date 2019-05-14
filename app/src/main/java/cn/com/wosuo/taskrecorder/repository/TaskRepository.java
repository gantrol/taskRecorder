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
import cn.com.wosuo.taskrecorder.api.BigkeerSerivice;
import cn.com.wosuo.taskrecorder.api.HttpUtil;
import cn.com.wosuo.taskrecorder.db.AppDatabase;
import cn.com.wosuo.taskrecorder.db.TaskDao;
import cn.com.wosuo.taskrecorder.util.FinalMap;
import cn.com.wosuo.taskrecorder.util.RateLimiter;
import cn.com.wosuo.taskrecorder.util.Resource;
import cn.com.wosuo.taskrecorder.vo.ArrayResult;
import cn.com.wosuo.taskrecorder.vo.BigkeerResponse;
import cn.com.wosuo.taskrecorder.vo.Task;
import cn.com.wosuo.taskrecorder.vo.User;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static cn.com.wosuo.taskrecorder.api.Urls.PHOTO_FILE;
import static cn.com.wosuo.taskrecorder.api.Urls.PHOTO_SUBID;
import static cn.com.wosuo.taskrecorder.api.Urls.TASK_COOR;
import static cn.com.wosuo.taskrecorder.api.Urls.TASK_ID;
import static cn.com.wosuo.taskrecorder.api.Urls.TASK_STATUS;
import static cn.com.wosuo.taskrecorder.api.Urls.TASK_TRACK;
import static cn.com.wosuo.taskrecorder.api.Urls.TASK_TRACK_DATA;
import static cn.com.wosuo.taskrecorder.api.Urls.TASK_X;
import static cn.com.wosuo.taskrecorder.api.Urls.TASK_Y;
import static cn.com.wosuo.taskrecorder.ui.UiString.TASK_LIST;

/**
 * Repository handling the work with tasks and comments.
 */
/**
 * Repository handling the work with tasks.
 */
public class TaskRepository {
//    TODO: 3RateLimit need to be smart set.

    private static TaskRepository sInstance;
    private BigkeerSerivice mBigkeerSerivice;
    private AppDatabase mDatabase;
    private TaskDao mTaskDao;
    private AppExecutors mAppExecutors;
    private RateLimiter<String> taskListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    private RateLimiter<LiveData<Task>> taskRateLimit = new RateLimiter<>(30, TimeUnit.SECONDS);
    private final static List<String> sTaskType = FinalMap.getTaskTypeList();


    private TaskRepository(final AppDatabase database) {
        mAppExecutors = new AppExecutors();
        mDatabase = database;
        mTaskDao = database.taskDao();
        mBigkeerSerivice = BasicApp.getBigkeerService();
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
    public LiveData<Resource<List<Task>>> getAllTasks() {
        return (new NetworkBoundResource<List<Task>, BigkeerResponse<ArrayResult<Task>>>(mAppExecutors) {
            @Override
            protected void saveCallResult(@NonNull BigkeerResponse<ArrayResult<Task>> item) {
                saveTasksUsersInDaoWithUid(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Task> data) {
                return data == null || data.isEmpty() || taskListRateLimit.shouldFetch(TASK_LIST);
            }

            @NonNull
            @Override
            protected LiveData<List<Task>> loadFromDb() {
                return mTaskDao.getAllTasks();
            }

            @Override
            public LiveData<ApiResponse<BigkeerResponse<ArrayResult<Task>>>> createCall() {
                return mBigkeerSerivice.getAllTasks();
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
                return data == null || data.isEmpty() || taskListRateLimit.shouldFetch(TASK_LIST);
            }

            @NonNull
            @Override
            protected LiveData<List<Task>> loadFromDb() {
                return mTaskDao.getCompanyTasks(id);
            }

            @Override
            public LiveData<ApiResponse<BigkeerResponse<ArrayResult<Task>>>> createCall() {
                return mBigkeerSerivice.getCompanyTasks(id);
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
                return data == null || data.isEmpty() || taskListRateLimit.shouldFetch(TASK_LIST);
            }

            @NonNull
            @Override
            protected LiveData<List<Task>> loadFromDb() {
                return mTaskDao.getManagerTasks(id);
            }

            @Override
            public LiveData<ApiResponse<BigkeerResponse<ArrayResult<Task>>>> createCall() {
                return mBigkeerSerivice.getManagerTasks(id);
            }

            @Override
            protected void onFetchFailed() {
                taskListRateLimit.reset(TASK_LIST);
            }

        }).getAsLiveData();
    }

    public LiveData<Resource<List<Task>>> getUserTasks(User user) {
        return (new NetworkBoundResource<List<Task>, BigkeerResponse<ArrayResult<Task>>>(mAppExecutors) {
            @Override
            protected void saveCallResult(@NonNull BigkeerResponse<ArrayResult<Task>> item) {
                List<Task> tasks = item.getResult().getData();
                for (Task task : tasks) {
                    task.setExecutor(user.getUid());
                    saveTaskUsersInDaoWithUid(task);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Task> data) {
                return data == null || data.isEmpty() || taskListRateLimit.shouldFetch(TASK_LIST);
            }

            @NonNull
            @Override
            protected LiveData<List<Task>> loadFromDb() {
                return mTaskDao.getUserTasks(user.getUid());
            }

            @Override
            public LiveData<ApiResponse<BigkeerResponse<ArrayResult<Task>>>> createCall() {
                return mBigkeerSerivice.getUserTasks();
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
        return mBigkeerSerivice.postImage(fpart, taskBody, typeBody, timeBody, locBody, descBody);
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
     * @param taskID
     * @param x 坐标系，
     * @param y
     * @param coordinate 默认为2，百度坐标系
     * @param callback
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
}