package cn.com.wosuo.taskrecorder.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import cn.com.wosuo.taskrecorder.BasicApp;
import cn.com.wosuo.taskrecorder.pref.AppPreferencesHelper;
import cn.com.wosuo.taskrecorder.repository.UserRepository;
import cn.com.wosuo.taskrecorder.util.Resource;
import cn.com.wosuo.taskrecorder.vo.Task;
import cn.com.wosuo.taskrecorder.vo.User;
import java.util.List;

import static cn.com.wosuo.taskrecorder.viewmodel.TaskViewModel.admin;
import static cn.com.wosuo.taskrecorder.viewmodel.TaskViewModel.group;
import static cn.com.wosuo.taskrecorder.viewmodel.TaskViewModel.manage;
import static cn.com.wosuo.taskrecorder.viewmodel.TaskViewModel.user;

public class UserViewModel extends AndroidViewModel {
    private UserRepository repository;

    /**
     * Application context should be passed to ViewModel. If activity context passed and configuration changed then activity will be destroyed,
     * which means MemoryLeak occurs because there is no activity to reference.
     *
     * @param application 应用
     */
    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = UserRepository.getInstance(((BasicApp)application).getDatabase());
    }

    /**
     * All the activities has to reference to ViewModel, not the repository. {@link UserRepository}
     * So we must create methods for ViewModel that match the repository methods
     */
    public void insert(User User) {
        repository.insert(User);
    }

    public void update(User User) {
        repository.update(User);
    }

    public void delete(User User) {
        repository.delete(User);
    }

    public void deleteAllUsers() {
        repository.deleteAllUsers();
    }



    public LiveData<Resource<List<User>>> getAllUsers() {
        LiveData<Resource<List<User>>> result = new MediatorLiveData<>();  // TODO: 3为UI设立空白页面
        User me = AppPreferencesHelper.getCurrentUser();
        if (me != null) {
            int userType = me.getType();
            if (userType == user && me.getCompany() != null) {
                User company = me.getCompany();
                result = repository.getUsersInCompany(me.getType(), company.getUid());
            } else if (userType == group) {
                result = repository.getUsersInCompany(me.getType(), me.getUid());
            } else if (userType == manage) {
                result = repository.getCompanys();
            } else if (userType == admin) {
                result = repository.getAllUsers();
            }
        }
        return result;
    }

}
