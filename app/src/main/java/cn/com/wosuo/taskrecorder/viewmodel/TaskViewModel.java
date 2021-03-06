package cn.com.wosuo.taskrecorder.viewmodel;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;
import androidx.lifecycle.MediatorLiveData;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import cn.com.wosuo.taskrecorder.BasicApp;
import cn.com.wosuo.taskrecorder.db.AppDatabase;
import cn.com.wosuo.taskrecorder.pref.AppPreferencesHelper;
import cn.com.wosuo.taskrecorder.repository.UserRepository;
import cn.com.wosuo.taskrecorder.util.DateUtil;
import cn.com.wosuo.taskrecorder.util.FinalMap;
import cn.com.wosuo.taskrecorder.util.Resource;
import cn.com.wosuo.taskrecorder.vo.LocCenterPoint;
import cn.com.wosuo.taskrecorder.vo.PhotoResult;
import cn.com.wosuo.taskrecorder.vo.PhotoUpload;
import cn.com.wosuo.taskrecorder.vo.Task;
import cn.com.wosuo.taskrecorder.repository.TaskRepository;
import cn.com.wosuo.taskrecorder.vo.Tracks;
import cn.com.wosuo.taskrecorder.vo.User;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static cn.com.wosuo.taskrecorder.util.FinalStrings.TaskField.TASK_DESCRIPTION;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TaskField.TASK_TYPE;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.UserField.ADMIN_GROUP;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.UserField.GROUP_GROUP;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.UserField.MANAGER_GROUP;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TaskField.TASK_ASSIGNEE;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TaskField.TASK_TITLE;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.UserField.USER_GROUP;

public class TaskViewModel extends AndroidViewModel {
    private TaskRepository mTaskRepository;
    private UserRepository mUserRepository;
//    private BasicApp mBasicApp;
    private static final List<String> sUserType = FinalMap.getUserTypeList();
    static final int admin = sUserType.indexOf(ADMIN_GROUP);
    static final int user = sUserType.indexOf(USER_GROUP);
    static final int group = sUserType.indexOf(GROUP_GROUP);
    static final int manage = sUserType.indexOf(MANAGER_GROUP);
    /**
     * Application context should be passed to ViewModel. If activity context passed and configuration changed then activity will be destroyed,
     * which means MemoryLeak occurs because there is no activity to reference.
     *
     * @param application 应用
     */
    @Inject
    public TaskViewModel(@NonNull Application application, TaskRepository taskRepository) {
        super(application);
        AppDatabase mDatabase = ((BasicApp)application).getDatabase();
        mTaskRepository = taskRepository;
        mUserRepository = UserRepository.getInstance(mDatabase);
    }

    public TaskViewModel(@NonNull Application application) {
        super(application);
        AppDatabase mDatabase = ((BasicApp)application).getDatabase();
        mTaskRepository = TaskRepository.getInstance(mDatabase);
        mUserRepository = UserRepository.getInstance(mDatabase);
    }

    /**
     * All the activities has to reference to ViewModel, not the mTaskRepository. {@link TaskRepository}
     * So we must create methods for ViewModel that match the mTaskRepository methods
     * @param Task 任务类{@link Task}
     */
    public void insert(Task Task) {
        mTaskRepository.insert(Task);
    }

    public void update(Task Task) {
        mTaskRepository.update(Task);
    }

    public void delete(Task Task) {
        mTaskRepository.delete(Task);
    }

    public void deleteAllTasks() {
        mTaskRepository.deleteAllTasks();
    }

    /**
     * 根据用户类型做加载判断
     *
     *
     */
    public LiveData<Resource<List<Task>>> getCurrentUserAllTasks() {
        LiveData<Resource<List<Task>>> result;
        User me = AppPreferencesHelper.getCurrentUser();
        if (me == null) {
            result = new MediatorLiveData<>();
        } else {
            int userType = me.getType();
            int currentUserId = me.getUid();
            if (userType == user){
                result = mTaskRepository.getUserTasks(currentUserId);
            } else if (userType == group){
                result = mTaskRepository.getCompanyTasks(currentUserId);
            } else if (userType == manage) {
                result = mTaskRepository.getManagerTasks(currentUserId);
            } else if (userType == admin) {
                result = mTaskRepository.getAllTasks(currentUserId);
            } else {
                result = new MediatorLiveData<>();
            }
        }
        return result;
    }

    public LiveData<Task> getTasksById(int taskID){
        return mTaskRepository.getTask(taskID);
    }

    public void setTaskStatus(int taskID, int status, Callback callback){
        mTaskRepository.patchTaskStatus(taskID, status, callback);
    }

    /**
     *
     * @param taskID id of task
     * @param x mLongitude
     * @param y mLatitude
     * @param coordinate 0, 1, 2
     * @param callback put callback
     */
    public void setTaskCenterPointCoordinate(
            int taskID, double x, double y, int coordinate, Callback callback){
        mTaskRepository.putCenterPointCoordinate(taskID, x, y, coordinate, callback);
    }

    public void setTaskCenterPointCoordinateLocally(int taskID, double mLongitude,
                                                    double mLatitude, int coordinate){
        mTaskRepository.setCenterPointCoordinateLocally(taskID, mLongitude, mLatitude, coordinate);
    }

    public void addTaskTrack(String trackData, int taskID, Callback callback){
        mTaskRepository.postTaskTrack(trackData, taskID, callback);
    }

    public Call<ResponseBody> uploadPhoto(PhotoUpload photoUpload, double longitude, double latitude, int taskID) {
        String photoPath = photoUpload.getPath();
//        TODO: move to ImageCompress void qualityCompass; https://blog.csdn.net/chenliguan/article/details/54409442
        photoUpload.setTime(DateUtil.getUnixTimestamp());
        photoUpload.setLocationStr(String.format(Locale.CHINA,
                "{positionX:%f, positionY:%f, coordinate:2}", longitude, latitude));
        File photo = new File(photoPath);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bmp = BitmapFactory.decodeFile(photoPath);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        do {
            bmp.compress(Bitmap.CompressFormat.JPEG, 70, bos);
//            TODO: https://stackoverflow.com/questions/25719620/how-to-solve-java-lang-outofmemoryerror-trouble-in-android
//            https://bricolsoftconsulting.com/copying-exif-metadata-using-sanselan/
            try {
                FileOutputStream fos = new FileOutputStream(photo);
                fos.write(bos.toByteArray());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (bos.toByteArray().length / 1024 > 1000);
//
        int type = photoUpload.getSubID();
        long time = photoUpload.getTime();
        String locationStr = photoUpload.getLocationStr();
        String desciption = photoUpload.getDescription();
        return mTaskRepository.postPhoto(photo, type, time, locationStr, desciption, taskID);
    }

    public Call<ResponseBody> createTask(String title, int assigneeID, int type, String detail){
        return mTaskRepository.createTask(
                getTaskInfoBody(title, assigneeID, type, detail)
        );
    }

    public Call<ResponseBody> editTask (int taskID, String title, int assigneeID, int type, String detail){
        return mTaskRepository.editTask(taskID, getTaskInfoBody(title, assigneeID, type, detail));
    }

    public RequestBody getTaskInfoBody (String title, int assigneeID, int type, String detail) {
        return new FormBody.Builder()
                .add(TASK_TITLE, title)
                .add(TASK_ASSIGNEE, Integer.toString(assigneeID))
                .add(TASK_TYPE, Integer.toString(type))
                .add(TASK_DESCRIPTION, detail)
                .build();
    }

    public LiveData<Resource<List<PhotoResult>>> getPhotoResultsByTaskID(int taskID, boolean isFirst){
        return mTaskRepository.getPhotoResultsByTaskID(taskID, isFirst);
    }

    public LiveData<Resource<LocCenterPoint>> getLocCenterPointByTaskID(int taskID){
        return mTaskRepository.getLocCenterPointByTaskID(taskID);
    }

    public LiveData<Resource<List<Tracks>>> getTracksByTaskID(int taskID){
        return mTaskRepository.getTracksByTaskID(taskID);
    }

    public void updateTaskInfo(String title, int assignee_id, int type, String detail, int taskID){
        mTaskRepository.updateTaskInfo(title, assignee_id, type, detail, taskID);
    }

    public void updateTaskStatus(int taskID, int status){
        mTaskRepository.updateTaskStatus(taskID, status);
    }

    public Call<ResponseBody> companyRemoteAddExecutor(int taskID, int executorID){
        return mTaskRepository.companyRemoteAddExecutor(taskID, executorID);
    }
    public Call<ResponseBody> companyRemoteDeleteExecutor(int taskID, int executorID){
        return mTaskRepository.companyRemoteDeleteExecutor(taskID, executorID);
    }

    public LiveData<Resource<User>> getUser(int id){
        return mUserRepository.getUserByID(id);
    }

    public User getLocalUser(int id) {
        return mUserRepository.getLocalUser(id);
    }

    @Deprecated
    public User getMe() {
//        TODO: it should be appear in UserViewModel!!
        return AppPreferencesHelper.getCurrentUser();
    }

    public void resetTaskListRateLimit(int userID){
        mTaskRepository.resetTaskListRateLimit(userID);
    }

    public void resetPhotoListRateLimit(int taskID){
        mTaskRepository.resetPhotoListRateLimit(taskID);
    }
}
