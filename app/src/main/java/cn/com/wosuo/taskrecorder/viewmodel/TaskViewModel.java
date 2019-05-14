package cn.com.wosuo.taskrecorder.viewmodel;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.exifinterface.media.ExifInterface;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;
import androidx.lifecycle.MediatorLiveData;


import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

import cn.com.wosuo.taskrecorder.BasicApp;
import cn.com.wosuo.taskrecorder.db.AppDatabase;
import cn.com.wosuo.taskrecorder.pref.AppPreferencesHelper;
import cn.com.wosuo.taskrecorder.repository.UserRepository;
import cn.com.wosuo.taskrecorder.util.DateUtil;
import cn.com.wosuo.taskrecorder.util.FinalMap;
import cn.com.wosuo.taskrecorder.util.Resource;
import cn.com.wosuo.taskrecorder.vo.PhotoUpload;
import cn.com.wosuo.taskrecorder.vo.Task;
import cn.com.wosuo.taskrecorder.repository.TaskRepository;
import cn.com.wosuo.taskrecorder.vo.User;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

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

    public Call<ResponseBody> uploadPhoto(PhotoUpload photoUpload, double longitude, double latitude, int taskID) {
//        TODO: compress photo. long file.length(); or Bitmap.getByteCount()
//         .compress
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
//        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), photo);
//        MultipartBody.Part photoPart = MultipartBody.Part.
//                createFormData("file", photo.getName(), fileReqBody);
        return mTaskRepository.postPhoto(photo, type, time, locationStr, desciption, taskID);
    }

    public LiveData<Resource<User>> getUser(int id){
        return mUserRepository.getUserByID(id);
    }
}
