package cn.com.wosuo.taskrecorder.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;
import androidx.lifecycle.MediatorLiveData;


import java.util.List;

import cn.com.wosuo.taskrecorder.BasicApp;
import cn.com.wosuo.taskrecorder.db.AppDatabase;
import cn.com.wosuo.taskrecorder.pref.AppPreferencesHelper;
import cn.com.wosuo.taskrecorder.repository.UserRepository;
import cn.com.wosuo.taskrecorder.util.FinalMap;
import cn.com.wosuo.taskrecorder.util.Resource;
import cn.com.wosuo.taskrecorder.vo.Task;
import cn.com.wosuo.taskrecorder.repository.TaskRepository;
import cn.com.wosuo.taskrecorder.vo.User;
import okhttp3.Callback;

import static cn.com.wosuo.taskrecorder.util.FinalStrings.ADMIN_GROUP;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.GROUP_GROUP;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.MANAGER_GROUP;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.USER_GROUP;

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
    public TaskViewModel(@NonNull Application application) {
        super(application);
        AppDatabase mDatabase = ((BasicApp)application).getDatabase();
        mTaskRepository = TaskRepository.getInstance(mDatabase);
        mUserRepository = UserRepository.getInstance(mDatabase);
    }

    /**
     * All the activities has to reference to ViewModel, not the mTaskRepository. {@link TaskRepository}
     * So we must create methods for ViewModel that match the mTaskRepository methods
     * @param Task
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
            result = new MediatorLiveData<>();  // TODO: 3为UI设立空白页面
        } else {
            int userType = me.getType();
            int currentUserId = me.getUid();
            if (userType == user){
                result = mTaskRepository.getUserTasks(me);
            } else if (userType == group){
                result = mTaskRepository.getCompanyTasks(currentUserId);
            } else if (userType == manage) {
                result = mTaskRepository.getManagerTasks(currentUserId);
            } else if (userType == admin) {
                result = mTaskRepository.getAllTasks();
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

    public void setTaskCenterPointCoordinate(
            int taskID, double x, double y, int coordinate, Callback callback){
        mTaskRepository.putCenterPointCoordinate(taskID, x, y, coordinate, callback);
    }

    public void addTaskTrack(String trackData, int taskID, Callback callback){
        mTaskRepository.postTaskTrack(trackData, taskID, callback);
    }

    public LiveData<Resource<User>> getUser(int id){
        return mUserRepository.getUserByID(id);
    }
}
